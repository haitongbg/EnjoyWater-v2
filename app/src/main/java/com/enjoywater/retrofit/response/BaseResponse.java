package com.enjoywater.retrofit.response;

import com.enjoywater.model.Error;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("error")
    private Error error;
    @SerializedName("success")
    private boolean success;

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
