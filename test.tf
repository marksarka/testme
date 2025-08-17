resource "aws_instance" "example" {
  ami           = "ami-0c94855ba95c71c99" # Amazon Linux 2
  instance_type = "t2.micro"

  # Assign a public IP address to the instance
  associate_public_ip_address = true

  vpc_security_group_ids = [aws_security_group.example.id]

  tags = {
    Name = "Example EC2 Instance"
  }
}

resource "aws_security_group" "example" {
  name_prefix = "example-"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Allow SSH from anywhere
  }
}
