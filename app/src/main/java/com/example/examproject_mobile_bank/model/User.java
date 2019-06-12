package com.example.examproject_mobile_bank.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    private int id;
    private String name;
    private String phone;
    private String mail;
    private String street;
    private String city;
    private String zip;
    private String dep;
    private String b_day;

    public User() {
    }

    public User(int id, String name, String phone, String mail, String street, String city, String zip, String dep, String b_day) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.dep = dep;
        this.b_day = b_day;
    }


    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phone = in.readString();
        mail = in.readString();
        street = in.readString();
        city = in.readString();
        zip = in.readString();
        dep = in.readString();
        b_day = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getDep() {
        return dep;
    }

    public int getId() {
        return id;
    }

    public String getB_day() {
        return b_day;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", dep='" + dep + '\'' +
                ", b_day='" + b_day + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(mail);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(zip);
        dest.writeString(dep);
        dest.writeString(b_day);
    }
}
