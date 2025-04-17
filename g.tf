terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.31.0"
    }

  }
  required_version = ">= 1.0"
  backend "s3" {}
}

provider "aws" {
  region = var.aws_region
}

data "terraform_remote_state" "slz" {
  backend = "s3"
  config = {
    bucket = var.remote_state_config_slz["bucket"]
    key    = var.remote_state_config_slz["key"]
    region = var.remote_state_config_slz["region"]
  }
}

# Create SNS Topic 
resource "aws_sns_topic" "terratest_topic" {
  name         = "TerratestTopic"
  display_name = "Terratest Topic"
}

resource "aws_cloudwatch_log_metric_filter" "metric_filters" {
  for_each = { for idx, service in var.services : idx => service }

  log_group_name = each.value.log_group_name
  pattern        = each.value.pattern

  metric_transformation {
    name           = each.value.metric_name
    namespace      = each.value.metric_namespace
    value          = each.value.metric_value
    default_value  = each.value.default_value
  }

  name = each.value.metric_filter_name
}

resource "aws_cloudwatch_metric_alarm" "metric_alarms" {
  for_each = { for idx, service in var.services : idx => service }

  alarm_name          = each.value.alarm_name
  alarm_description   = each.value.alarm_description
  comparison_operator = each.value.comparison_operator
  evaluation_periods  = each.value.evaluation_periods
  metric_name         = each.value.metric_name
  namespace           = each.value.metric_namespace
  period              = each.value.period
  threshold           = each.value.threshold
  statistic           = each.value.statistic
  #  alarm_actions       = each.value.alarm_actions
  alarm_actions       = [aws_sns_topic.terratest_topic.arn]
  treat_missing_data  = each.value.treat_missing_data

  tags = {
    notification_type = each.value.notification_type
    severity          = each.value.severity
    Name              = each.value.alarm_name
  }
}

# # Create AWS Cloudwatch Log Metric Filter for Content Service
# resource "aws_cloudwatch_log_metric_filter" "metric_filter" {
#   log_group_name = var.log_group_name
#   metric_transformation {
#     name          = var.metric_name
#     namespace     = var.metric_namespace
#     value         = var.metric_value
#     default_value = var.default_value
#   }
#   name    = var.metric_filter_name != "" ? var.metric_filter_name : var.alarm_name
#   pattern = var.pattern
# }

# resource "aws_cloudwatch_metric_alarm" "metric_alarm" {
#   alarm_name          = var.alarm_name
#   alarm_description   = var.alarm_description
#   comparison_operator = var.comparison_operator
#   evaluation_periods  = var.evaluation_periods
#   metric_name         = var.metric_name
#   namespace           = var.metric_namespace
#   period              = var.period
#   threshold           = var.threshold
#   statistic           = var.statistic
#   alarm_actions       = [aws_sns_topic.terratest_topic.arn]
#   treat_missing_data  = var.treat_missing_data
#   tags = {
#     notification_type = var.notification_type,
#     severity          = var.severity,
#     Name              = var.alarm_name
#   }
# }


resource "aws_lambda_permission" "with_sns" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.func.function_name
  principal     = "sns.amazonaws.com"
  source_arn    = aws_sns_topic.terratest_topic.arn
}

resource "aws_sns_topic_subscription" "lambda" {
  topic_arn = aws_sns_topic.terratest_topic.arn
  protocol  = "lambda"
  endpoint  = aws_lambda_function.func.arn
}

data "archive_file" "lambda" {
  type        = "zip" 
  source_file = "${path.module}/src/lambda_function.py" 
  output_path = "lambda_function_src.zip"
}

resource "aws_lambda_function" "func" {
  filename      = "lambda_function_src.zip"
  function_name = "lambda_called_from_sns"
  role          = aws_iam_role.default.arn
  handler       = "exports.handler"
  runtime       = "python3.8"
}

resource "aws_iam_role" "default" {
  name = "iam_for_lambda_with_sns"

  # Terraform's "jsonencode" function converts a
  # Terraform expression result to valid JSON syntax.
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_policy" "parameter_store_policy" {
  name        = "parameter_store_access_policy"
  description = "IAM policy to allow access to Parameter Store"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow",
        Action    = [
          "ssm:GetParameter",
          "ssm:GetParameters",
          "ssm:GetParametersByPath"
        ],
        Resource  = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "parameter_store_attachment" {
  role       = aws_iam_role.default.id
  policy_arn = aws_iam_policy.parameter_store_policy.arn
}

