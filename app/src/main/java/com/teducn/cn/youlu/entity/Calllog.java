package com.teducn.cn.youlu.entity;

/**
 * Created by pjy on 2017/7/5.
 * 用来封装通话的日志信息
 */

public class Calllog {
    private int _id;//通话日志的主键
    private String name;//通话的联系人的姓名
    private long date;//通话时间
    private String dateStr;//格式化后的一个通话时间
    private int photo_Id;//头像编号
    private String number;//电话号码
    private int type;//类型编号

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

    public int getPhoto_Id() {
        return photo_Id;
    }

    public void setPhoto_Id(int photo_Id) {
        this.photo_Id = photo_Id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Calllog() {
    }

    public Calllog(int _id, String name, long date, String dateStr, int photo_Id, String number, int type) {
        this._id = _id;
        this.name = name;
        this.date = date;
        this.dateStr = dateStr;
        this.photo_Id = photo_Id;
        this.number = number;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Calllog{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", dateStr='" + dateStr + '\'' +
                ", photo_Id=" + photo_Id +
                ", number='" + number + '\'' +
                ", type=" + type +
                '}';
    }
}
