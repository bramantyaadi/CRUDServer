package com.example.crudserver.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Result implements Parcelable {

    private int code;
    private String message;

    @Nullable
    private ArrayList<Item> items;

    @Nullable
    private Item item;

    public Result(int code, String message, @Nullable ArrayList<Item> items, @Nullable Item item) {
        this.code = code;
        this.message = message;
        this.items = items;
        this.item = item;
    }

    protected Result(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
        this.items = in.createTypedArrayList(Item.CREATOR);
        this.item = in.readParcelable(Item.class.getClassLoader());
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(@Nullable ArrayList<Item> items) {
        this.items = items;
    }

    @Nullable
    public Item getItem() {
        return item;
    }

    public void setItem(@Nullable Item item) {
        this.item = item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//
//    }
}
