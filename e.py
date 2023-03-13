import asyncio

# Vulnerable
user_input = "cat /etc/passwd" # value supplied by user
loop = asyncio.new_event_loop()
# This is similar to the standard library subprocess.Popen class called with shell=True
loop.subprocess_shell(asyncio.SubprocessProtocol, user_input)

# Vulnerable
user_input = "cat /etc/passwd" # value supplied by user
asyncio.subprocess.create_subprocess_shell(user_input)
