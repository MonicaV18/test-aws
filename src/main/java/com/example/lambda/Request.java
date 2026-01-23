package com.example.lambda;

/**
 * Request POJO for Lambda function
 */
public class Request {
    private String name;
    private String message;

    public Request() {
    }

    public Request(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
