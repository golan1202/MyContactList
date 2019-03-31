package com.example.mylist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.Serializable;

public class Contact implements Serializable {
    private String name;
    private  String phone;
    transient private Bitmap photo;
    private String email;
    private String address;

    public Contact() {}

    public Contact(String name, String phone, Bitmap photo,String email,String address) {
        this.name = name;
        this.phone = phone;
        this.photo = photo;
        this.email=email;
        this.address=address;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        photo.compress(Bitmap.CompressFormat.JPEG,50,out);
        out.defaultWriteObject();
    }
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        photo= BitmapFactory.decodeStream(in);
        in.defaultReadObject();
    }
}
