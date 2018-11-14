package com.enjoywater.retrofit.response;

import com.enjoywater.model.Error;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("error")
    private Error error;
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private JsonElement data;

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public JsonElement getData() {
        return data;
    }
}
