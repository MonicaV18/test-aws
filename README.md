# AWS Lambda Function with Spring Boot and Java 17

This project demonstrates a serverless AWS Lambda function built using Spring Boot 3.2 and Java 17. It leverages Spring Cloud Function to seamlessly integrate Spring Boot with AWS Lambda runtime.

**NEW:** Now includes MySQL and Elasticsearch integration with Podman support for local development! 🎉

## Features

- ✅ Java 17
- ✅ Spring Boot 3.2.1
- ✅ Spring Cloud Function 4.1.0
- ✅ AWS Lambda Java Core & Events
- ✅ Maven build with Shade plugin
- ✅ Unit tests with JUnit 5
- ✅ **MySQL database integration**
- ✅ **Elasticsearch integration**
- ✅ **Podman/Docker Compose for local development**
- ✅ **Dual-mode: Works in AWS Lambda AND locally**

## Quick Start - Local Development

**New to this project? Start here:** 👉 [QUICKSTART.md](QUICKSTART.md)

Run the entire stack locally with one command:

```bash
./local-dev.sh start
```

Or use the interactive menu:

```bash
./local-dev.sh
```

📖 **Guides:**
- 🚀 [Quick Start Guide](QUICKSTART.md) - Get started in 5 minutes
- 📖 [Local Development Guide](LOCAL_DEVELOPMENT.md) - Detailed documentation
- ☁️ AWS Lambda Deployment - See below

## Project Structure

```
.
├── Dockerfile                                   # Container image for the app
├── podman-compose.yml                          # Podman compose configuration
├── local-dev.sh                                # Local development helper script
├── LOCAL_DEVELOPMENT.md                        # Local development guide
├── pom.xml                                     # Maven configuration
├── src
│   ├── main
│   │   ├── java/com/example/lambda
│   │   │   ├── LambdaApplication.java              # Spring Boot application
│   │   │   ├── LambdaHandler.java                  # AWS Lambda entry point
│   │   │   ├── Request.java                        # Request POJO
│   │   │   ├── Response.java                       # Response POJO
│   │   │   ├── model/
│   │   │   │   ├── RequestEntity.java              # MySQL entity
│   │   │   │   └── RequestDocument.java            # Elasticsearch document
│   │   │   ├── repository/
│   │   │   │   ├── RequestRepository.java          # JPA repository
│   │   │   │   └── RequestDocumentRepository.java  # ES repository
│   │   │   └── service/
│   │   │       └── RequestProcessingService.java   # Business logic with DB/ES
│   │   └── resources
│   │       ├── application.properties              # Default config
│   │       └── application-local.properties        # Local dev config
│   └── test
│       └── java/com/example/lambda
│           ├── LambdaApplicationTests.java         # Context load test
│           └── ProcessRequestFunctionTests.java    # Function tests
└── README.md
```

## Prerequisites

### For Local Development
- Java 17 or higher
- Maven 3.6 or higher
- Podman and podman-compose (or Docker and docker-compose)

### For AWS Deployment
- AWS CLI (for deployment)
- AWS Account with appropriate permissions

## Building the Project

Build the project using Maven:

```bash
mvn clean package
```

This will create two JAR files in the `target` directory:
- `aws-lambda-spring-boot-1.0.0.jar` - Regular JAR
- `aws-lambda-spring-boot-1.0.0-aws.jar` - Shaded JAR with all dependencies (use this for AWS Lambda)

## Running Tests

Run all tests:

```bash
mvn test
```

## Local Testing

You can test the function locally using the AWS SAM CLI or by creating a simple test harness.

### Example Test Input

```json
{
  "name": "John Doe",
  "message": "Hello from Lambda"
}
```

### Expected Output

```json
{
  "message": "Hello John Doe! Your request has been processed successfully.",
  "status": "SUCCESS",
  "timestamp": 1706024897113
}
```

## Deployment to AWS Lambda

### Step 1: Create Lambda Function

Using AWS CLI:

```bash
aws lambda create-function \
  --function-name spring-boot-lambda \
  --runtime java17 \
  --role arn:aws:iam::YOUR_ACCOUNT_ID:role/lambda-execution-role \
  --handler com.example.lambda.LambdaHandler::handleRequest \
  --zip-file fileb://target/aws-lambda-spring-boot-1.0.0-aws.jar \
  --timeout 30 \
  --memory-size 512 \
  --environment Variables={SPRING_CLOUD_FUNCTION_DEFINITION=processRequest}
```

### Step 2: Update Function Code

After making changes, update the function:

```bash
mvn clean package
aws lambda update-function-code \
  --function-name spring-boot-lambda \
  --zip-file fileb://target/aws-lambda-spring-boot-1.0.0-aws.jar
```

### Step 3: Test the Function

```bash
aws lambda invoke \
  --function-name spring-boot-lambda \
  --payload '{"name":"John Doe","message":"Test"}' \
  response.json

cat response.json
```

## Configuration

### Lambda Handler

The Lambda handler is configured in `LambdaHandler.java`:
- **Handler**: `com.example.lambda.LambdaHandler::handleRequest`

### Spring Cloud Function

The function is defined in `LambdaApplication.java`:
- **Function Name**: `processRequest`
- **Input**: `Request` object
- **Output**: `Response` object

### Application Properties

Configuration in `src/main/resources/application.properties`:
```properties
spring.application.name=aws-lambda-spring-boot
spring.cloud.function.definition=processRequest
```

## Customization

### Adding New Functions

1. Create a new `@Bean` method in `LambdaApplication.java`:

```java
@Bean
public Function<MyRequest, MyResponse> myFunction() {
    return request -> {
        // Your logic here
        return new MyResponse();
    };
}
```

2. Update `application.properties`:

```properties
spring.cloud.function.definition=myFunction
```

### Environment Variables

Set environment variables in AWS Lambda console or via CLI:

```bash
aws lambda update-function-configuration \
  --function-name spring-boot-lambda \
  --environment Variables={SPRING_CLOUD_FUNCTION_DEFINITION=processRequest,LOG_LEVEL=DEBUG}
```

## Performance Considerations

- **Cold Start**: Spring Boot Lambda functions may have longer cold start times. Consider using:
  - AWS Lambda SnapStart (for Java 11+)
  - Provisioned Concurrency
  - Reducing dependencies
  
- **Memory**: Allocate sufficient memory (512MB-1024MB recommended for Spring Boot)

- **Timeout**: Set appropriate timeout (30 seconds recommended minimum)

## Troubleshooting

### Common Issues

1. **ClassNotFoundException**: Ensure you're using the `-aws.jar` shaded artifact
2. **Timeout**: Increase Lambda timeout in function configuration
3. **Out of Memory**: Increase Lambda memory allocation

### Viewing Logs

```bash
aws logs tail /aws/lambda/spring-boot-lambda --follow
```

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.