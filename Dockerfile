# Use AWS Lambda Java 17 base image
FROM public.ecr.aws/lambda/java:17

# Copy the built JAR file into the container
# The JAR should be placed in ${LAMBDA_TASK_ROOT} directory
COPY target/*.jar ${LAMBDA_TASK_ROOT}

# Set the CMD to the handler class
# Format: package.ClassName::methodName
# Example: com.example.Handler::handleRequest
CMD [ "com.example.Handler::handleRequest" ]

# Expose port 9000 for local invocation
EXPOSE 9000
