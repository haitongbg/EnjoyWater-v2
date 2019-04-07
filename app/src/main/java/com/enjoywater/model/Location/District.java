package com.enjoywater.model.Location;

import android.os.Parcel;
import android.os.Parcelable;

import com.enjoywater.model.Address;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class District implements Parcelable {
    @SerializedName("i")
    private String id;
    @SerializedName("n")
    private String name;
    @SerializedName("t")
    private String type;
    @SerializedName("c")
    private ArrayList<Ward> wards;
    @SerializedName("available")
    private boolean available = false;

    public District() {
    }

    protected District(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        wards = in.createTypedArrayList(Ward.CREATOR);
        available = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeTypedList(wards);
        dest.writeByte((byte) (available ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override
        public District createFromParcel(Parcel in) {
            return new District(in);
        }

        @Override
        public District[] newArray(int size) {
            return new District[size];
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

    public ArrayList<Ward> getWards() {
        return wards;
    }

    public void setWards(ArrayList<Ward> wards) {
        this.wards = wards;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
