# Core Functionality & Implementation Details

This document explains the technical implementation of the `photo-manager-lambda` project, focusing on the Java Lambda function and its integration with Amazon S3.

## Lambda Handler: `UploadPhotoHandler`

The core logic resides in [UploadPhotoHandler.java](src/main/java/UploadPhotoHandler.java). It implements the `RequestHandler` interface from the AWS Lambda Java Core library.

### Request Processing
1.  **Event Mapping**: The function accepts an `APIGatewayV2HTTPEvent`, which is the standard event format for AWS HTTP APIs.
2.  **Base64 Decoding**: API Gateway typically sends binary data (like images) as Base64 encoded strings in the `body` field.
    *   The code checks `event.getIsBase64Encoded()`.
    *   If true, it uses `Base64.getDecoder().decode(body)` to retrieve the raw bytes.
3.  **S3 Client**: Uses the **AWS SDK for Java v2** (`S3Client`) configured for the `ap-south-1` region.

### S3 Integration
The upload is performed using the `putObject` method:
*   **Bucket**: `my-file-storage-uploads`
*   **Key**: Currently defaults to `uploaded-image.jpg` (can be extended to be dynamic).
*   **Content-Type**: Hardcoded to `image/jpeg`.

---

## Tech Stack Deep Dive

### Java 17 & AWS SDK v2
We use Java 17 for its performance improvements and modern language features. The AWS SDK v2 is used because it provides:
*   Non-blocking I/O support.
*   Better memory management.
*   Improved startup times compared to v1.

### Maven Shade Plugin (Fat JAR)
Lambda requires all dependencies to be bundled into a single ZIP or JAR file. The `maven-shade-plugin` in `pom.xml` is configured to create a "shaded" JAR (also known as a Fat JAR) which includes:
*   Compiled project classes.
*   AWS SDK dependencies.
*   Jackson (for JSON processing).

---

## Security & IAM
The Lambda function does not use hardcoded AWS credentials. Instead, it relies on an **IAM Execution Role**:
*   **Permission Required**: `s3:PutObject` on `arn:aws:s3:::my-file-storage-uploads/*`.
*   **Trust Relationship**: Allows the `lambda.amazonaws.com` service to assume the role.

This follows the principle of least privilege, ensuring the Lambda can only perform the specific actions required for its task.
