# Dockerfile for AWS Lambda deployment
# This Dockerfile uses the AWS Lambda Java 17 base image
# and packages the Spring Cloud Function application

FROM public.ecr.aws/lambda/java:17

# Copy the shaded JAR to the Lambda task root
COPY target/echo-lambda-1.0.0-SNAPSHOT-aws.jar ${LAMBDA_TASK_ROOT}/lib/

# Set the handler to the Spring Cloud Function adapter
# This allows Spring Cloud Functions to run on AWS Lambda
CMD ["org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"]
