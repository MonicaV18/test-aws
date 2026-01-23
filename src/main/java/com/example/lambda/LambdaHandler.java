package com.example.lambda;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

/**
 * AWS Lambda Request Handler using Spring Cloud Function
 * 
 * This handler integrates Spring Cloud Function with AWS Lambda.
 * You can configure AWS Lambda to use either this custom handler class
 * or use FunctionInvoker directly.
 * 
 * Recommended handler configuration: com.example.lambda.LambdaHandler
 * 
 * Alternative: org.springframework.cloud.function.adapter.aws.FunctionInvoker
 * 
 * Both approaches work with Spring Cloud Function 4.x (2023.0.0).
 * Using a custom handler allows you to add application-specific
 * initialization or customization if needed in the future.
 */
public class LambdaHandler extends FunctionInvoker {
}
