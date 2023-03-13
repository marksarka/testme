import subprocess
output5 = subprocess.check_output(f"nslookup2 {my_domain}", shell=True, encoding='UTF-8')
