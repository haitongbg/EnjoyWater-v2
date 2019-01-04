package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tong Hai on 04/1/2019.
 * Phone: +84388326555
 * Email: haitongvan@vccorp.vn
 */
public class Coupon implements Parcelable {
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("ended")
    private long ended;
    @SerializedName("started")
    private long started;
    @SerializedName("limit")
    private int limit;
    @SerializedName("maxDiscountValue")
    private int maxDiscountValue;
    @SerializedName("value")
    private int value;
    @SerializedName("requireMinOrderValue")
    private int requireMinOrderValue;
    @SerializedName("enabled")
    private boolean enabled;
    @SerializedName("code")
    private String code;
    @SerializedName("type")
    private String type;
    @SerializedName("id")
    private int id;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;

    public Coupon() {
    }

    protected Coupon(Parcel in) {
        updatedAt = in.readString();
        createdAt = in.readString();
        ended = in.readInt();
        started = in.readInt();
        limit = in.readInt();
        maxDiscountValue = in.readInt();
        value = in.readInt();
        requireMinOrderValue = in.readInt();
        enabled = in.readByte() != 0;
        code = in.readString();
        type = in.readString();
        id = in.readInt();
        description = in.readString();
        name = in.readString();
    }

    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
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

    public long getEnded() {
        return ended;
    }

    public void setEnded(long ended) {
        this.ended = ended;
    }

    public long getStarted() {
        return started;
    }

    public void setStarted(long started) {
        this.started = started;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMaxDiscountValue() {
        return maxDiscountValue;
    }

    public void setMaxDiscountValue(int maxDiscountValue) {
        this.maxDiscountValue = maxDiscountValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRequireMinOrderValue() {
        return requireMinOrderValue;
    }

    public void setRequireMinOrderValue(int requireMinOrderValue) {
        this.requireMinOrderValue = requireMinOrderValue;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
        dest.writeLong(ended);
        dest.writeLong(started);
        dest.writeInt(limit);
        dest.writeInt(maxDiscountValue);
        dest.writeInt(value);
        dest.writeInt(requireMinOrderValue);
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeString(code);
        dest.writeString(type);
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeString(name);
    }
}
