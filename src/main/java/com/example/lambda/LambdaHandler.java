package com.example.lambda;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

/**
 * AWS Lambda Handler
 * 
 * This class serves as the entry point for AWS Lambda.
 * It extends FunctionInvoker from Spring Cloud Function AWS adapter.
 * 
 * Configure this as the handler in AWS Lambda:
 * Handler: com.example.lambda.LambdaHandler::handleRequest
 */
public class LambdaHandler extends FunctionInvoker {
    // FunctionInvoker handles the invocation automatically
    // No additional code needed here
}
