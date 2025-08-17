import ast


def lambda_handler(event, context):

    tainted = event["exploit_code-stdlib"]

    # ruleid: tainted-code-stdlib-aws-lambda
    eval(tainted)
    # ruleid: tainted-code-stdlib-aws-lambda
    exec(tainted)

    # ok: tainted-code-stdlib-aws-lambda
    ast.literal_eval(tainted)

    # ok: tainted-code-stdlib-aws-lambda
    eval("clean")
    # ok: tainted-code-stdlib-aws-lambda
    exec("clean")

    # ok: tainted-code-stdlib-aws-lambda
    ast.literal_eval("clean")
