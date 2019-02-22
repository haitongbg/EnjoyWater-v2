package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class News implements Parcelable {
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("status")
    private String status;
    @SerializedName("content")
    private String content;
    @SerializedName("image")
    private String image;
    @SerializedName("desctiption")
    private String desctiption;
    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private String id;
    private boolean isLoadmore = false;

    public News() {
    }

    public News(boolean isLoadmore) {
        this.isLoadmore = isLoadmore;
    }

    protected News(Parcel in) {
        updatedAt = in.readString();
        createdAt = in.readString();
        status = in.readString();
        content = in.readString();
        image = in.readString();
        desctiption = in.readString();
        title = in.readString();
        id = in.readString();
        isLoadmore = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeString(status);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(desctiption);
        dest.writeString(title);
        dest.writeString(id);
        dest.writeByte((byte) (isLoadmore ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLoadmore() {
        return isLoadmore;
    }

    public void setLoadmore(boolean loadmore) {
        isLoadmore = loadmore;
    }
}
