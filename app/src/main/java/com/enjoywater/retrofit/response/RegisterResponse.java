package com.enjoywater.retrofit.response;

import com.enjoywater.model.Error;
import com.enjoywater.model.User;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("error")
    private Error error;
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private User user;

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }
}
