# Photo Manager Lambda

This is a serverless Java backend application using **AWS Lambda** and **Amazon S3** to handle photo uploads from an API Gateway.

## Documentation

*   [Workflow & Architecture](./WORKFLOW.md) - Detailed breakdown of the serverless request flow.
*   [Core Functionality](./CORE_FUNCTIONALITY.md) - Implementation details of the Java Lambda and S3 integration.

## Overview

The application is an AWS Lambda function that receives an HTTP request (via API Gateway), reads an image payload (often Base64 encoded), and uploads that image directly into an Amazon S3 bucket (`my-file-storage-uploads`).

## AWS Services & Libraries Used

1. **AWS Lambda**: A serverless compute service that runs code in response to events and automatically manages the underlying compute resources. Here, it runs our Java code whenever an HTTP request is made.
2. **Amazon API Gateway (V2)**: Acts as the "front door" for the web application, routing HTTP requests from users to the Lambda function.
3. **Amazon S3 (Simple Storage Service)**: An object storage service used to store and retrieve any amount of data. This project uses it to store the uploaded photo.
4. **AWS SDK for Java v2**: The official AWS library used to interact with AWS services (specifically `software.amazon.awssdk:s3` in this project).
5. **AWS Lambda Java Core & Events**: Libraries provided by AWS to write Lambda functions in Java and handle specific event types like API Gateway HTTP events.

## Prerequisites

- Java 17
- Maven
- An AWS Account with IAM permissions to deploy Lambda functions and access S3.
- An S3 bucket named `my-file-storage-uploads` in the `ap-south-1` region.

## Building and Packaging

This project uses the `maven-shade-plugin` to build a "fat jar" (an executable jar containing all dependencies), which is required for AWS Lambda.

Run the following command to build the project:

```bash
mvn clean package
```

This will generate a `.jar` file inside the `target/` directory.

## Deployment

1. Go to the AWS Management Console and navigate to **Lambda**.
2. Create a new function using the Java 17 runtime.
3. Upload the fat JAR generated in the `target/` directory.
4. Set the handler information to `UploadPhotoHandler::handleRequest`.
5. Attach an IAM Role to the Lambda function that grants `s3:PutObject` permission for the `my-file-storage-uploads` bucket.
6. Configure an API Gateway (HTTP API) as a trigger for this Lambda function.

## How It Works (Workflow)

1. A client sends an HTTP POST request to the API Gateway URL with an image in the request body.
2. API Gateway forwards this request to the AWS Lambda function.
3. The Lambda function (`UploadPhotoHandler`) checks if the body is Base64 encoded. If so, it decodes it back into binary bytes.
4. The Lambda function creates a `PutObjectRequest` and uploads the bytes to the S3 bucket using the AWS SDK.
5. If successful, it returns a success message.
