import os

# Vulnerable
user_input = "foo && cat /etc/passwd" # value supplied by user
os.system("grep -R {} .".format(user_input))

# Vulnerable
user_input = "foo && cat /etc/passwd" # value supplied by user
os.popen("ls -l " + user_input)
