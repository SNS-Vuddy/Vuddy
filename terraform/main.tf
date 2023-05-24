terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "us-east-2"
}

resource "aws_instance" "create_ec2_1" {
  ami           = "ami-0430580de6244e02e"
  instance_type = "t3.micro"

  tags = { Name = "ec2_by_terraform" }
}
