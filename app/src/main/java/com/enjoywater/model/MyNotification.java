package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MyNotification implements Parcelable {
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("status")
    private String status;
    @SerializedName("content")
    private String content;
    @SerializedName("userId")
    private String userId;
    @SerializedName("body")
    private String body;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("id")
    private int id;
    @SerializedName("createdAt")
    private String createdAt;

    public MyNotification() {
    }

    protected MyNotification(Parcel in) {
        updatedAt = in.readString();
        status = in.readString();
        content = in.readString();
        userId = in.readString();
        body = in.readString();
        title = in.readString();
        type = in.readString();
        id = in.readInt();
        createdAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedAt);
        dest.writeString(status);
        dest.writeString(content);
        dest.writeString(userId);
        dest.writeString(body);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeInt(id);
        dest.writeString(createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyNotification> CREATOR = new Creator<MyNotification>() {
        @Override
        public MyNotification createFromParcel(Parcel in) {
            return new MyNotification(in);
        }

        @Override
        public MyNotification[] newArray(int size) {
            return new MyNotification[size];
        }
    };

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
