package com.enjoywater.model.Location;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ward implements Parcelable {
    @SerializedName("i")
    private String id;
    @SerializedName("n")
    private String name;
    @SerializedName("t")
    private String type;
    @SerializedName("available")
    private boolean available = false;

    public Ward() {
    }

    protected Ward(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        available = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeByte((byte) (available ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ward> CREATOR = new Creator<Ward>() {
        @Override
        public Ward createFromParcel(Parcel in) {
            return new Ward(in);
        }

        @Override
        public Ward[] newArray(int size) {
            return new Ward[size];
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
