package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Notify implements Parcelable {
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("content")
    private String content;
    @SerializedName("body")
    private String body;
    @SerializedName("title")
    private String title;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private String type;
    @SerializedName("userId")
    private int userId;
    @SerializedName("id")
    private int id;
    private boolean isLoadmore = false;

    public Notify() {
    }

    public Notify(boolean isLoadmore) {
        this.isLoadmore = isLoadmore;
    }

    protected Notify(Parcel in) {
        updatedAt = in.readString();
        createdAt = in.readString();
        content = in.readString();
        body = in.readString();
        title = in.readString();
        status = in.readString();
        type = in.readString();
        userId = in.readInt();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeString(content);
        dest.writeString(body);
        dest.writeString(title);
        dest.writeString(status);
        dest.writeString(type);
        dest.writeInt(userId);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notify> CREATOR = new Creator<Notify>() {
        @Override
        public Notify createFromParcel(Parcel in) {
            return new Notify(in);
        }

        @Override
        public Notify[] newArray(int size) {
            return new Notify[size];
        }
    };

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLoadmore() {
        return isLoadmore;
    }

    public void setLoadmore(boolean loadmore) {
        isLoadmore = loadmore;
    }
}
