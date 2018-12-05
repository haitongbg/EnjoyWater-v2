package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User implements Parcelable {
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("enablePaymentBill")
    private boolean enablePaymentBill;
    @SerializedName("status")
    private String status;
    @SerializedName("activated")
    private boolean activated;
    @SerializedName("gender")
    private String gender;
    @SerializedName("group")
    private String group;
    @SerializedName("phone")
    private String phone;
    @SerializedName("emailVerified")
    private boolean emailVerified;
    @SerializedName("id")
    private String id;
    @SerializedName("coin")
    private int coin;
    @SerializedName("refId")
    private String refId;
    @SerializedName("myCode")
    private String myCode;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("address")
    private String address;
    @SerializedName("otherAddress")
    private ArrayList<Address> otherAddress;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("passport")
    private String passport;
    @SerializedName("levelInfo")
    private LevelInfo levelInfo;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("name")
    private String name;

    public User() {
    }

    protected User(Parcel in) {
        updatedAt = in.readString();
        createdAt = in.readString();
        enablePaymentBill = in.readByte() != 0;
        status = in.readString();
        activated = in.readByte() != 0;
        gender = in.readString();
        group = in.readString();
        phone = in.readString();
        emailVerified = in.readByte() != 0;
        id = in.readString();
        coin = in.readInt();
        refId = in.readString();
        myCode = in.readString();
        birthday = in.readString();
        address = in.readString();
        otherAddress = in.createTypedArrayList(Address.CREATOR);
        avatar = in.readString();
        passport = in.readString();
        levelInfo = in.readParcelable(LevelInfo.class.getClassLoader());
        email = in.readString();
        username = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeByte((byte) (enablePaymentBill ? 1 : 0));
        dest.writeString(status);
        dest.writeByte((byte) (activated ? 1 : 0));
        dest.writeString(gender);
        dest.writeString(group);
        dest.writeString(phone);
        dest.writeByte((byte) (emailVerified ? 1 : 0));
        dest.writeString(id);
        dest.writeInt(coin);
        dest.writeString(refId);
        dest.writeString(myCode);
        dest.writeString(birthday);
        dest.writeString(address);
        dest.writeTypedList(otherAddress);
        dest.writeString(avatar);
        dest.writeString(passport);
        dest.writeParcelable(levelInfo, flags);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(name);
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

    public boolean isEnablePaymentBill() {
        return enablePaymentBill;
    }

    public void setEnablePaymentBill(boolean enablePaymentBill) {
        this.enablePaymentBill = enablePaymentBill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getMyCode() {
        return myCode;
    }

    public void setMyCode(String myCode) {
        this.myCode = myCode;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Address> getOtherAddress() {
        return otherAddress;
    }

    public void setOtherAddress(ArrayList<Address> otherAddress) {
        this.otherAddress = otherAddress;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public LevelInfo getLevelInfo() {
        return levelInfo;
    }

    public void setLevelInfo(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
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
}
