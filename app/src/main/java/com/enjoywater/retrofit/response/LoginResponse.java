package com.enjoywater.retrofit.response;

import com.enjoywater.model.Error;
import com.enjoywater.model.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("error")
    private Error error;
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private Data data;

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("token")
        private String token;
        @SerializedName("userInfo")
        private User user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
