import subprocess
output1 = subprocess.check_output(f"nslookup2 {my_domain}", shell=False, encoding='UTF-8')
