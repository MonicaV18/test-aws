# test-aws

## AWS Lambda Docker/Podman Container

This repository contains a Dockerfile for running AWS Lambda functions locally using Docker or Podman.

### Prerequisites

- Podman or Docker installed
- Java 17 Lambda function with a built JAR file in the `target/` directory

### Building the Container

```bash
# Using Podman
podman build -t lambda-function .

# Using Docker
docker build -t lambda-function .
```

### Running the Lambda Locally

```bash
# Using Podman
podman run -p 9000:8080 lambda-function

# Using Docker
docker run -p 9000:8080 lambda-function
```

### Testing the Lambda Function

Once the container is running, you can invoke the Lambda function using curl:

```bash
curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" \
  -d '{"key": "value"}'
```

### Configuration

Before building the container, you need to:

1. Build your Lambda function JAR file (should be in `target/` directory)
2. Update the `CMD` in the Dockerfile with your handler class:
   - Format: `package.ClassName::methodName`
   - Example: `com.example.Handler::handleRequest`