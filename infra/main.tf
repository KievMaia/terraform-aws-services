provider "aws" {
  access_key                  = "test"
  secret_key                  = "test"
  region                      = "us-east-1"

  endpoints {
    ec2 = "http://localhost:4566"
    iam = "http://localhost:4566"
    s3  = "http://s3.localhost.localstack.cloud:4566"
    sts = "http://localhost:4566"
    sqs = "http://localhost:4566"
    sns = "http://localhost:4566"
  }
}

resource "aws_security_group" "securitygroup" {
  name        = "securitygroup"
  description = "Permitir acesso HTTP e acesso a Internet"

  ingress {
    from_port = 80
    to_port   = 80
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 65550
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "servidor" {
  ami           = "ami-12345678"
  instance_type = "t2.nano"
  user_data = file("user_data.sh")
  vpc_security_group_ids = [aws_security_group.securitygroup.id]
}

resource "aws_sqs_queue" "public_api_sqs" {
  name = "public-api-sqs"
}

resource "aws_sns_topic" "public_api_sns" {
  name = "public-api-sns"
}

resource "aws_sns_topic_subscription" "public_api_sns_subscription" {
  topic_arn = aws_sns_topic.public_api_sns.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.public_api_sqs.arn
}

resource "aws_sqs_queue_policy" "public_api_sqs_policy" {
  queue_url = aws_sqs_queue.public_api_sqs.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          AWS = "*"
        }
        Action   = "SQS:SendMessage"
        Resource = aws_sqs_queue.public_api_sqs.arn
        Condition = {
          ArnEquals = {
            "aws:SourceArn" = aws_sns_topic.public_api_sns.arn
          }
        }
      }
    ]
  })
}

resource "aws_s3_bucket" "public_api_s3" {
  bucket = "public-api-s3"
}

resource "aws_s3_bucket_policy" "public_bucket_policy" {
  bucket = aws_s3_bucket.public_api_s3.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = "*"
        Action = [
          "s3:GetObject",
          "s3:PutObject",
          "s3:DeleteObject"
        ]
        Resource = "${aws_s3_bucket.public_api_s3.arn}/*"
      },
    ]
  })
}