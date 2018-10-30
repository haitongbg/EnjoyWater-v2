package com.enjoywater.retrofit.response;

import com.enjoywater.model.Error;
import com.enjoywater.model.News;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNewsDetailsResponse {
    @SerializedName("error")
    private Error error;
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private News news;

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public News getNews() {
        return news;
    }
}
