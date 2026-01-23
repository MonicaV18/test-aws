# AWS Lambda Function with Spring Cloud Function

This project demonstrates a Java AWS Lambda function using Spring Cloud Function framework and Java 17.

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- AWS Account (for deployment)
- AWS CLI (optional, for deployment)

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/com/example/lambda/
│   │   │   ├── LambdaApplication.java    # Main Spring Boot application
│   │   │   └── LambdaHandler.java        # AWS Lambda handler
│   │   └── resources/
│   │       └── application.properties     # Configuration
│   └── test/
│       └── java/com/example/lambda/
│           └── LambdaApplicationTest.java # Tests
└── pom.xml                                 # Maven configuration
```

## Functions

This project includes two sample functions:

1. **uppercase**: Converts input string to uppercase
2. **reverse**: Reverses the input string

## Building the Project

Build the project using Maven:

```bash
mvn clean package
```

This will create a shaded JAR file in the `target/` directory named `aws-lambda-function-1.0.0-aws.jar`.

## Running Tests

Run the tests using Maven:

```bash
mvn test
```

## Deploying to AWS Lambda

### 1. Upload the JAR

Upload the generated JAR file (`target/aws-lambda-function-1.0.0-aws.jar`) to AWS Lambda.

### 2. Configure the Handler

Set the handler to: `com.example.lambda.LambdaHandler`

### 3. Configure Runtime

- Runtime: Java 17
- Memory: 512 MB (recommended minimum)
- Timeout: 30 seconds (recommended)

### 4. Set Environment Variables (Optional)

To switch between functions, set the environment variable:
- Key: `SPRING_CLOUD_FUNCTION_DEFINITION`
- Value: `uppercase` or `reverse`

## Testing Locally

You can test the functions using the test class or by running the Spring Boot application locally:

```bash
mvn spring-boot:run
```

## Example Input/Output

### Uppercase Function

**Input:**
```json
"hello world"
```

**Output:**
```json
"HELLO WORLD"
```

### Reverse Function

**Input:**
```json
"hello"
```

**Output:**
```json
"olleh"
```

## Technology Stack

- Java 17
- Spring Boot 3.2.1
- Spring Cloud Function 2023.0.0
- AWS Lambda Java Core 1.2.3
- Maven 3.x

## License

This project is provided as-is for demonstration purposes.