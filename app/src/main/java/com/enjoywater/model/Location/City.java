package com.enjoywater.model.Location;

import android.os.Parcel;
import android.os.Parcelable;

import com.enjoywater.model.Address;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class City implements Parcelable {
    @SerializedName("i")
    private String id;
    @SerializedName("n")
    private String name;
    @SerializedName("t")
    private String type;
    @SerializedName("c")
    private ArrayList<District> districts;
    @SerializedName("available")
    private boolean available = false;

    public City() {
    }

    protected City(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        districts = in.createTypedArrayList(District.CREATOR);
        available = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeTypedList(districts);
        dest.writeByte((byte) (available ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<District> getDistricts() {
        return districts;
    }

    public void setDistricts(ArrayList<District> districts) {
        this.districts = districts;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
