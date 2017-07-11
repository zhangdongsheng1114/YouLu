package com.teducn.cn.youlu.entity;

/**
 * Created by pjy on 2017/6/30.
 */

public class Contact {
    private int _id;//联系人的主键编号
    private String name;//联系人姓名
    private String email;//电子邮箱
    private String address;//地址
    private String phone;//电话号码0
    private int photoId;//头像编号

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public Contact() {
    }

    public Contact(int _id, String name, String email, String address, String phone, int photoId) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.photoId = photoId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", photoId=" + photoId +
                '}';
    }
}
