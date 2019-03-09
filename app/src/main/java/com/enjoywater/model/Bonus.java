package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Bonus implements Parcelable {
    @SerializedName("userInfo")
    private User.UserInfo userInfo;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("type")
    private String type;
    @SerializedName("orderId")
    private int orderId;
    @SerializedName("total")
    private int total;
    @SerializedName("coin")
    private int coin;
    @SerializedName("recipient")
    private int recipient;
    @SerializedName("userId")
    private int userId;
    @SerializedName("id")
    private int id;
    boolean isLoadmore = false;

    public Bonus() {
    }

    public Bonus(boolean isLoadmore) {
        this.isLoadmore = isLoadmore;
    }

    protected Bonus(Parcel in) {
        userInfo = in.readParcelable(User.UserInfo.class.getClassLoader());
        updatedAt = in.readString();
        createdAt = in.readString();
        type = in.readString();
        orderId = in.readInt();
        total = in.readInt();
        coin = in.readInt();
        recipient = in.readInt();
        userId = in.readInt();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userInfo, flags);
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeString(type);
        dest.writeInt(orderId);
        dest.writeInt(total);
        dest.writeInt(coin);
        dest.writeInt(recipient);
        dest.writeInt(userId);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bonus> CREATOR = new Creator<Bonus>() {
        @Override
        public Bonus createFromParcel(Parcel in) {
            return new Bonus(in);
        }

        @Override
        public Bonus[] newArray(int size) {
            return new Bonus[size];
        }
    };

    public User.UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User.UserInfo userInfo) {
        this.userInfo = userInfo;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
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
