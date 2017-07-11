package com.teducn.cn.youlu.manager;

/**
 * Created by tarena on 2017/7/10.
 */

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.teducn.cn.youlu.entity.Conversation;
import com.teducn.cn.youlu.entity.SMS;
import com.teducn.cn.youlu.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjy on 2017/7/7.
 */

public class SMSManager {
    // 短信会话Uri
    public static final Uri CONVERSATION_URI = Uri.parse("content://mms-sms/conversations");
    // 短信Uri 对应的ContentProvider会协调处理短信的收件箱和发件箱
    public static final Uri SMS_URI = Uri.parse("content://sms");
    // 短信发件箱:
    public static final Uri SMS_SEND_URI = Uri.parse("content://sms/sent");
    // 短信收件箱:
    public static final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");

    public static final String SENT_SMS = "com.tarena.karen.youlu.SENT_SMS";

    public static void getConversationColums(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CONVERSATION_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getColumnCount();
            for (int i = 0; i < count; i++) {
                Log.i("TAG:", cursor.getColumnName(i) + ":" + cursor.getString(i));
            }
        }
        cursor.close();
    }

    public static List<Conversation> getAllConversations(Context context) {
        List<Conversation> conversations = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        String[] projects = new String[]{"thread_id", "address", "body", "read", "date"};
        String order = "date desc";
        Cursor cursor = resolver.query(CONVERSATION_URI, projects, null, null, order);
        while (cursor.moveToNext()) {
            int thread_id = cursor.getInt(0);
            String address = cursor.getString(1);
            String body = cursor.getString(2);
            int read = cursor.getInt(3);
            long date = cursor.getLong(4);

            String name = ContactManager.getNameByNumber(context, address);
            int photo_Id = ContactManager.getPhotoIdByNumber(context, address);
            String dateStr = DateUtil.formatDate(date);

            Conversation conversation = new Conversation(thread_id, name, address, body, date, dateStr, read, photo_Id);
            conversations.add(conversation);
        }
        return conversations;
    }

    public static void getSMSColumns(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(SMS_URI, null, null, null, null);
        if (cursor.moveToNext()) {
            int count = cursor.getColumnCount();
            for (int i = 0; i < count; i++) {
                Log.i("TAG:SMS", cursor.getColumnName(i) + ":" + cursor.getString(i));
            }
        }
        cursor.close();
    }

    public static List<SMS> getSMSSesByThreadId(Context context, int thread_Id) {
        List<SMS> smses = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        String[] projects = new String[]{"_id", "address", "date", "type", "body"};
        String where = "thread_id=?";
        String[] args = new String[]{String.valueOf(thread_Id)};
        String order = "date asc";//对查询结果升序排序
        Cursor cursor = resolver.query(SMS_URI, projects, where, args, order);
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String address = cursor.getString(1);
            long date = cursor.getLong(2);
            int type = cursor.getInt(3);
            String body = cursor.getString(4);

            SMS sms = new SMS();

            sms.set_id(_id);
            sms.setAddress(address);
            sms.setBody(body);
            sms.setType(type);
            sms.setDate(date);
            sms.setDateStr(DateUtil.formatDate2(date));
            sms.setPhoto_Id(ContactManager.getPhotoIdByNumber(context, address));

            smses.add(sms);
        }
        cursor.close();
        return smses;
    }

    /**
     * 当收到短信时做短信的解析处理
     *
     * @return
     */
    public static SMS onReceiveSMS(
            Context context,
            Intent intent) {
        //以bundle方式获得短信内容
        Bundle bundle = intent.getExtras();
        Object pduses[] = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pduses.length];

        String format = intent.getStringExtra("format");

        for (int i = 0; i < pduses.length; i++) {
            if (Build.VERSION.SDK_INT < 23) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduses[i]);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduses[i], format);
            }
        }
        StringBuilder builder = new StringBuilder();
        String address = "";
        long date = 0;
        //遍历数组获得短信的具体信息
        for (int i = 0; i < messages.length; i++) {
            if (i == 0) {
                //获得电话号码和发送时间
                address = messages[i].getOriginatingAddress();
                date = messages[i].getTimestampMillis();
            }
            builder.append(messages[i].getMessageBody());

        }
        SMS sms = new SMS();

        sms.setBody(builder.toString());
        sms.setDate(date);
        sms.setAddress(address);
        sms.setType(1);
        return sms;
    }

    /**
     * 保存短信的内容到收件箱中
     *
     * @param context   上下文
     * @param sms       短信对象
     * @param thread_id 会话编号
     */
    public static void saveReceiveSMS(
            Context context,
            SMS sms,
            int thread_id) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put("thread_id", thread_id);
        values.put("type", sms.getType());
        values.put("body", sms.getBody());
        values.put("date", sms.getDate());
        values.put("address", sms.getAddress());
        values.put("read", 1);
        resolver.insert(SMS_INBOX_URI, values);
    }

    /**
     * 实现短信的发送
     *
     * @param context 上下文
     * @param message 短信内容
     * @param address 电话号码
     */
    public static void sentSMS(
            Context context,
            String message,
            String address) {
        //获得系统的一个短信服务类
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> messages =
                manager.divideMessage(message);
        for (int i = 0; i < messages.size(); i++) {
            //获得一条短信
            String msg = messages.get(i);

            //参数1是接收短信的联系的电话号码
            //参数2 发送方的电话号码
            //参数3 是短信的内容
            //参数4 是短信发出时发的系统广播的广播意图

            Intent intent = new Intent(SENT_SMS);
            intent.putExtra("body", message);
            intent.putExtra("address", address);

            PendingIntent sentIntent =
                    PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            manager.sendTextMessage(address, null, msg, sentIntent, null);
        }


    }

    /**
     * @param context 上下文
     * @param body    发送的短信的内容
     * @param address 接收方的电话号码
     */
    public static void saveSentSMS(
            Context context,
            String body,
            String address) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put("body", body);
        values.put("address", address);
        values.put("type", 2);
        values.put("date", System.currentTimeMillis());

        resolver.insert(SMS_SEND_URI, values);


    }
}
