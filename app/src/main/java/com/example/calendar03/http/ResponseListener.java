package com.example.calendar03.http;

public interface ResponseListener<Output> {
    void onSuccess(Output result);
    void onFailure(Exception e);
}
