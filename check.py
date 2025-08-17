import subprocess
import sys

# Vulnerable
user_input = "foo && cat /etc/passwd" # value supplied by user
subprocess.call("grep -R {} .".format(user_input), shell=True)

# Vulnerable
user_input = "cat /etc/passwd" # value supplied by user
subprocess.run(["bash", "-c", user_input], shell=True)

# Not vulnerable
user_input = "cat /etc/passwd" # value supplied by user
subprocess.Popen(['ls', '-l', user_input])

# Not vulnerable
subprocess.check_output('ls -l dir/')
