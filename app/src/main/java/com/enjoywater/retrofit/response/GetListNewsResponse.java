package com.enjoywater.retrofit.response;

import com.enjoywater.model.Error;
import com.enjoywater.model.News;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetListNewsResponse {
    @SerializedName("error")
    private Error error;
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private ArrayList<News> listNews;

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<News> getListNews() {
        return listNews;
    }
}
