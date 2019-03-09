package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Product implements Parcelable {
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("createdBy")
    private String createdBy;
    @SerializedName("status")
    private String status;
    @SerializedName("catId")
    private int catId;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("desctiption")
    private String desctiption;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("deliveryFee")
    private int deliveryFee;
    @SerializedName("discount")
    private int discount;
    @SerializedName("ask")
    private int ask;
    @SerializedName("bid")
    private int bid;
    @SerializedName("productId")
    private int productId;
    @SerializedName("volume")
    private int volume;
    private boolean isSelected = false;

    public Product() {
    }

    protected Product(Parcel in) {
        updatedAt = in.readString();
        createdAt = in.readString();
        createdBy = in.readString();
        status = in.readString();
        catId = in.readInt();
        thumbnail = in.readString();
        desctiption = in.readString();
        name = in.readString();
        id = in.readInt();
        deliveryFee = in.readInt();
        discount = in.readInt();
        ask = in.readInt();
        bid = in.readInt();
        productId = in.readInt();
        volume = in.readInt();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeString(createdBy);
        dest.writeString(status);
        dest.writeInt(catId);
        dest.writeString(thumbnail);
        dest.writeString(desctiption);
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeInt(deliveryFee);
        dest.writeInt(discount);
        dest.writeInt(ask);
        dest.writeInt(bid);
        dest.writeInt(productId);
        dest.writeInt(volume);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        if (id == 0) id = productId;
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        if (productId == 0) productId = id;
        return productId;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getAsk() {
        return ask;
    }

    public void setAsk(int ask) {
        this.ask = ask;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
