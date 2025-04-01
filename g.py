def lambda_handler(event, context):

    tainted = event["exploit_code-stdlib"]

    # ruleid: tainted-code-stdlib-aws-lambda
    eval(tainted)
    # ruleid: tainted-code-stdlib-aws-lambda
    exec(tainted)
