package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {
    @SerializedName("key")
    private int key;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("provinceId")
    private String cityId;
    @SerializedName("districtId")
    private String districtId;
    @SerializedName("wardId")
    private String wardId;
    @SerializedName("address")
    private String address;
    private boolean selected = false;

    public Address() {
    }

    public Address(String name, String phone, String cityId, String districtId, String wardId, String address) {
        this.name = name;
        this.phone = phone;
        this.cityId = cityId;
        this.districtId = districtId;
        this.wardId = wardId;
        this.address = address;
    }

    public Address(String name, String phone, String cityId, String districtId, String address) {
        this.name = name;
        this.phone = phone;
        this.cityId = cityId;
        this.districtId = districtId;
        this.address = address;
    }

    protected Address(Parcel in) {
        key = in.readInt();
        name = in.readString();
        phone = in.readString();
        cityId = in.readString();
        districtId = in.readString();
        wardId = in.readString();
        address = in.readString();
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(key);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(cityId);
        dest.writeString(districtId);
        dest.writeString(wardId);
        dest.writeString(address);
        dest.writeByte((byte) (selected ? 1 : 0));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
