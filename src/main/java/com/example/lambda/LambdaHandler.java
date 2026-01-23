package com.example.lambda;

import org.springframework.cloud.function.adapter.aws.FunctionInvoker;

/**
 * AWS Lambda Request Handler using Spring Cloud Function
 * 
 * This handler integrates Spring Cloud Function with AWS Lambda.
 * The handler can be configured in AWS Lambda to point to this class.
 * 
 * Handler configuration: org.springframework.cloud.function.adapter.aws.FunctionInvoker
 * 
 * Note: With Spring Cloud Function 4.x, you can use FunctionInvoker directly
 * as the handler in AWS Lambda configuration.
 */
public class LambdaHandler extends FunctionInvoker {
}
