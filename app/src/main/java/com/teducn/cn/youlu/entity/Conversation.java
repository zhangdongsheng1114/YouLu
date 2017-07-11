package com.teducn.cn.youlu.entity;

/**
 * Created by pjy on 2017/7/7.
 */

public class Conversation {
    private int thread_Id;//会话的编号
    private String name;//姓名
    private String number;//电话号码
    private String body;//会话的摘要
    private long date;//会话的日期
    private String dateStr;//格式的日期
    private int read;//读取的状态 1是已读 0是未读的
    private int photoId;//头像编号

    public Conversation() {
    }

    public Conversation(int thread_Id, String name, String number, String body, long date, String dateStr, int read, int photoId) {
        this.thread_Id = thread_Id;
        this.name = name;
        this.number = number;
        this.body = body;
        this.date = date;
        this.dateStr = dateStr;
        this.read = read;
        this.photoId = photoId;
    }

    public int getThread_Id() {
        return thread_Id;
    }

    public void setThread_Id(int thread_Id) {
        this.thread_Id = thread_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "thread_Id=" + thread_Id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", body='" + body + '\'' +
                ", date=" + date +
                ", dateStr='" + dateStr + '\'' +
                ", read=" + read +
                ", photoId=" + photoId +
                '}';
    }
}
