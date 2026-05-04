package com.aditya.smartdental;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Representasi entitas Alat Medis (POJO).
 * Implementasi Materi: Class Model
 */
public class ToolModel implements Parcelable {
    private int id;
    private String name;
    private String type;
    private int usageCount;
    private String status;
    private String imagePath; // New field for image path

    public ToolModel(int id, String name, String type, int usageCount, String status, String imagePath) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.usageCount = usageCount;
        this.status = status;
        this.imagePath = imagePath;
    }

    public ToolModel(String name, String type, int usageCount, String status, String imagePath) {
        this.name = name;
        this.type = type;
        this.usageCount = usageCount;
        this.status = status;
        this.imagePath = imagePath;
    }

    protected ToolModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        usageCount = in.readInt();
        status = in.readString();
        imagePath = in.readString();
    }

    public static final Creator<ToolModel> CREATOR = new Creator<ToolModel>() {
        @Override
        public ToolModel createFromParcel(Parcel in) {
            return new ToolModel(in);
        }

        @Override
        public ToolModel[] newArray(int size) {
            return new ToolModel[size];
        }
    };

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getUsageCount() { return usageCount; }
    public String getStatus() { return status; }
    public String getImagePath() { return imagePath; }
    public void setStatus(String status) { this.status = status; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeInt(usageCount);
        dest.writeString(status);
        dest.writeString(imagePath);
    }
}