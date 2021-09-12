package com.example.calendar03.http;

public class HttpException extends Exception {
    int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }
}
