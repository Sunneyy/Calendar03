package com.example.calendar03.http;

import com.example.calendar03.gson.BaseResult;

public interface ResponseListener<Output> {
    void onSuccess(BaseResult<Output> result);
    void onFailure(Exception e);
}
