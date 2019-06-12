package com.example.examproject_mobile_bank.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BankAccount implements Parcelable {

    private int id;
    private String name;
    private User user;
    private int user_id;
    private int holdings;

    public BankAccount(int id, String name, User user, int holdings) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.holdings = holdings;
    }

    public BankAccount(String name, User user, int holdings) {
        this.name = name;
        this.user = user;
        this.holdings = holdings;
    }

    public BankAccount(String name, int holdings) {
        this.name = name;
        this.holdings = holdings;
    }
    public BankAccount(int id, String name, int user_id, int holdings) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.holdings = holdings;
    }


    protected BankAccount(Parcel in) {
        id = in.readInt();
        name = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        user_id = in.readInt();
        holdings = in.readInt();
    }

    public BankAccount(int id, String name, User user, int user_id, int holdings) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.user_id = user_id;
        this.holdings = holdings;
    }

    public BankAccount() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable(user, flags);
        dest.writeInt(user_id);
        dest.writeInt(holdings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
        @Override
        public BankAccount createFromParcel(Parcel in) {
            return new BankAccount(in);
        }

        @Override
        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public int getHoldings() {
        return holdings;
    }

    public int getUser_id() {
        return user_id;
    }

//    @Override
//    public String toString() {
//        return "BankAccount{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", user=" + user +
//                ", user_id=" + user_id +
//                ", holdings=" + holdings +
//                '}';
//    }

        @Override
    public String toString() {
        return name + ". Balance: " + holdings;
    }
}
