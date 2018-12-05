package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {
    @SerializedName("key")
    private int key;
    @SerializedName("districtId")
    private String districtId;
    @SerializedName("provinceId")
    private String cityId;
    @SerializedName("wardId")
    private String wardId;
    @SerializedName("phone")
    private String phone;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;

    public Address() {
    }

    protected Address(Parcel in) {
        key = in.readInt();
        districtId = in.readString();
        cityId = in.readString();
        wardId = in.readString();
        phone = in.readString();
        name = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(key);
        dest.writeString(districtId);
        dest.writeString(cityId);
        dest.writeString(wardId);
        dest.writeString(phone);
        dest.writeString(name);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
