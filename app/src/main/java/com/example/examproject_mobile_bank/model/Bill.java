package com.example.examproject_mobile_bank.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Bill implements Parcelable {

    private int id;
    private String b_date;
    private int user;
    private int amount;
    private int account;
    private int target;

    public Bill(int id, int user, String b_date, int amount, int account, int target) {
        this.id = id;
        this.b_date=b_date;
        this.user = user;
        this.amount = amount;
        this.account= account;
        this.target = target;
    }

    protected Bill(Parcel in) {
        id = in.readInt();
        b_date = in.readString();
        user = in.readInt();
        amount = in.readInt();
        account = in.readInt();
        target = in.readInt();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getB_date() {
        return b_date;
    }

    public void setB_date(String b_date) {
        this.b_date = b_date;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(b_date);
        dest.writeInt(user);
        dest.writeInt(amount);
        dest.writeInt(account);
        dest.writeInt(target);
    }
}
