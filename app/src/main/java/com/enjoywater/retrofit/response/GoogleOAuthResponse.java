package com.enjoywater.retrofit.response;

import com.google.gson.annotations.SerializedName;

public class GoogleOAuthResponse {
    @SerializedName("id_token")
    private String id_token;
    @SerializedName("token_type")
    private String token_type;
    @SerializedName("scope")
    private String scope;
    @SerializedName("expires_in")
    private int expires_in;
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("refresh_token")
    private String refresh_token;
    @SerializedName("error")
    private String error;
    @SerializedName("error_description")
    private String error_description;

    public String getId_token() {
        return id_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getScope() {
        return scope;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getError() {
        return error;
    }

    public String getError_description() {
        return error_description;
    }
}
