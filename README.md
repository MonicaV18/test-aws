# AWS Lambda Echo Function with Spring Cloud Function

This project demonstrates a Java AWS Lambda function using Spring Cloud Function and Java 17.

## Features

- **Spring Boot 3.2.1** with **Java 17**
- **Spring Cloud Function** for serverless deployment
- AWS Lambda deployment using `FunctionInvoker`
- Echo function that takes a string input and returns a greeting

## Function Details

- **Function Name**: `echo`
- **Handler**: `org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest`
- **Input**: String (e.g., `"World"`)
- **Output**: String (e.g., `"Hello, World"`)

## Maven Dependencies

The key dependencies used in this project:

```xml
<!-- Spring Boot Starter - Provides core Spring Boot functionality -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>

<!-- Spring Cloud Function AWS Adapter - Enables Spring Cloud Functions on AWS Lambda -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-function-adapter-aws</artifactId>
</dependency>
```

## Building the Project

Build the project using Maven:

```bash
mvn clean package
```

This will create a shaded JAR file: `target/echo-lambda-1.0.0-SNAPSHOT-aws.jar`

## Running Tests

Run the unit tests:

```bash
mvn test
```

## Deployment to AWS Lambda

### Option 1: Direct JAR Upload

1. Build the project: `mvn clean package`
2. Upload `target/echo-lambda-1.0.0-SNAPSHOT-aws.jar` to AWS Lambda
3. Configure the Lambda function:
   - **Runtime**: Java 17
   - **Handler**: `org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest`
   - **Memory**: 512 MB (minimum recommended)
   - **Timeout**: 30 seconds

### Option 2: Docker Deployment

1. Build the Docker image:
   ```bash
   docker build -t echo-lambda .
   ```

2. Tag the image for AWS ECR:
   ```bash
   docker tag echo-lambda:latest <account-id>.dkr.ecr.<region>.amazonaws.com/echo-lambda:latest
   ```

3. Push to ECR:
   ```bash
   aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com
   docker push <account-id>.dkr.ecr.<region>.amazonaws.com/echo-lambda:latest
   ```

4. Create Lambda function from container image in AWS Console

## Testing Locally with AWS Lambda Runtime API

You can test the Lambda function locally using the AWS Lambda Runtime Interface Emulator (RIE).

### Prerequisites

- Docker installed
- Built the shaded JAR: `mvn clean package`

### Steps to Test Locally

1. **Build the Docker image**:
   ```bash
   docker build -t echo-lambda .
   ```

2. **Run the container with the Lambda Runtime Interface Emulator**:
   ```bash
   docker run -p 9000:8080 echo-lambda
   ```

3. **Invoke the function** (in a separate terminal):
   ```bash
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" \
     -d '"World"'
   ```

   Expected output:
   ```
   "Hello, World"
   ```

4. **Test with different inputs**:
   ```bash
   # Test with "Lambda"
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" \
     -d '"Lambda"'
   # Output: "Hello, Lambda"

   # Test with "AWS"
   curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" \
     -d '"AWS"'
   # Output: "Hello, AWS"
   ```

### Alternative: Using AWS SAM CLI

If you have AWS SAM CLI installed, you can also test locally:

```bash
# Start the local Lambda environment
sam local start-lambda

# Invoke the function
aws lambda invoke --function-name echo \
  --endpoint-url http://127.0.0.1:3001 \
  --payload '"World"' \
  response.json
cat response.json
```

## Project Structure

```
.
├── Dockerfile                          # Docker configuration for AWS Lambda
├── pom.xml                             # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/example/lambda/
│   │   │   └── EchoLambdaApplication.java   # Main application class
│   │   └── resources/
│   │       └── application.properties       # Spring configuration
│   └── test/
│       └── java/com/example/lambda/
│           └── EchoLambdaApplicationTest.java  # Unit tests
└── README.md                          # This file
```

## Configuration

The function is configured in `application.properties`:

```properties
spring.cloud.function.definition=echo
```

This tells Spring Cloud Function which bean to expose for Lambda invocation.

## License

This project is open source and available under the MIT License.