package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LevelInfo implements Parcelable {
    @SerializedName("icon")
    private String icon;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;
    @SerializedName("level")
    private int level;

    public LevelInfo() {
    }

    protected LevelInfo(Parcel in) {
        icon = in.readString();
        description = in.readString();
        name = in.readString();
        level = in.readInt();
    }

    public static final Creator<LevelInfo> CREATOR = new Creator<LevelInfo>() {
        @Override
        public LevelInfo createFromParcel(Parcel in) {
            return new LevelInfo(in);
        }

        @Override
        public LevelInfo[] newArray(int size) {
            return new LevelInfo[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(icon);
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeInt(level);
    }
}
