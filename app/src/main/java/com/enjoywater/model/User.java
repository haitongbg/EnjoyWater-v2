package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("refId")
    private String refId;
    @SerializedName("status")
    private String status;
    @SerializedName("passport")
    private String passport;
    @SerializedName("address")
    private String address;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("gender")
    private String gender;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("group")
    private String group;
    @SerializedName("phone")
    private String phone;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;
    @SerializedName("password")
    private String password;
    @SerializedName("levelInfo")
    private LevelInfo levelInfo;

    public User() {
    }

    protected User(Parcel in) {
        createdAt = in.readString();
        updatedAt = in.readString();
        refId = in.readString();
        status = in.readString();
        passport = in.readString();
        address = in.readString();
        birthday = in.readString();
        gender = in.readString();
        avatar = in.readString();
        group = in.readString();
        phone = in.readString();
        email = in.readString();
        username = in.readString();
        name = in.readString();
        id = in.readString();
        password = in.readString();
        levelInfo = in.readParcelable(LevelInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(refId);
        dest.writeString(status);
        dest.writeString(passport);
        dest.writeString(address);
        dest.writeString(birthday);
        dest.writeString(gender);
        dest.writeString(avatar);
        dest.writeString(group);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(password);
        dest.writeParcelable(levelInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LevelInfo getLevelInfo() {
        return levelInfo;
    }

    public void setLevelInfo(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
    }
}
