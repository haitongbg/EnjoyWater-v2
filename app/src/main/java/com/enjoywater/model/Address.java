package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.enjoywater.model.Location.City;
import com.enjoywater.model.Location.District;
import com.enjoywater.model.Location.Ward;

public class Address implements Parcelable{
    private City city;
    private District district;
    private Ward ward;
    private String addressDetail;

    public Address() {
    }

    protected Address(Parcel in) {
        city = in.readParcelable(City.class.getClassLoader());
        district = in.readParcelable(District.class.getClassLoader());
        ward = in.readParcelable(Ward.class.getClassLoader());
        addressDetail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(city, flags);
        dest.writeParcelable(district, flags);
        dest.writeParcelable(ward, flags);
        dest.writeString(addressDetail);
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }
}
