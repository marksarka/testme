import subprocess
output = subprocess.check_output(f"nslookup2 {my_domain}", shell=False, encoding='UTF-8')

MY_AWS_SECRET="AKIAIOSFODNN7EXAMPLE"
