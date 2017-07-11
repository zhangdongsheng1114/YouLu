package com.teducn.cn.youlu.entity;

/**
 * Created by tarena on 2017/7/10.
 */

public class SMS {

    private int _id;//短信的主键编号
    private String body;//短信的主体
    private long date;//收发的时间
    private String dateStr;//格式化的时间
    private int type;//短信的类型1为收到的短信2为发出的短信
    private String address;//电话号码
    private int photo_Id;//头像编号


    public SMS() {
        super();
    }


    public SMS(int _id, String body, long date, String dateStr, int type, String address, int photo_Id) {
        this._id = _id;
        this.body = body;
        this.date = date;
        this.dateStr = dateStr;
        this.type = type;
        this.address = address;
        this.photo_Id = photo_Id;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoto_Id() {
        return photo_Id;
    }

    public void setPhoto_Id(int photo_Id) {
        this.photo_Id = photo_Id;
    }

    @Override
    public String toString() {
        return "SMS{" +
                "_id=" + _id +
                ", body='" + body + '\'' +
                ", date=" + date +
                ", dateStr='" + dateStr + '\'' +
                ", type=" + type +
                ", address='" + address + '\'' +
                ", photo_Id=" + photo_Id +
                '}';
    }
}
