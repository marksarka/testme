import subprocess # nosemgrep
output = subprocess.check_output(f"nslookup2 {my_domain}", shell=True, encoding='UTF-8') # nosemgrep

a=""
