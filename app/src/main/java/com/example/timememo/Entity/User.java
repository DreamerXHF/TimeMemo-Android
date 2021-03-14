package com.example.timememo.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id = 0;
    private String name;
    private String email;
    private String password;
    private String signature;
    private String photo;

    public User(int id,String name, String email, String password, String signature,String photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.signature = signature;
        this.photo = photo;
    }

    public User(){
        this.id = 0;
        this.name = null;
        this.email = null;
        this.password = null;
        this.signature = null;
        this.photo = null;
    }


    //将对象属性反序列化然后读取出来，注意属性的顺序必须按照序列化属性的顺序
    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        signature = in.readString();
        photo = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //将对象属性进行序列化
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(signature);
        dest.writeString(photo);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", signature='"+ signature +'\'' +
                ", photo='"+ photo +'\'' +
                '}';
    }
}
