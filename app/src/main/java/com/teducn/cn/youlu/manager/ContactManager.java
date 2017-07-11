package com.teducn.cn.youlu.manager;

/**
 * Created by tarena on 2017/7/10.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.LruCache;
import android.util.SparseArray;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.constant.IURI;
import com.teducn.cn.youlu.entity.Calllog;
import com.teducn.cn.youlu.entity.Contact;
import com.teducn.cn.youlu.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjy on 2017/6/30.
 */

public class ContactManager {
    //用来缓存联系人对象的集合
    public static SparseArray<Contact> contactCache = new SparseArray<>();
    //最近最少使用的缓存集合
    /*在LruCache中缓存的数据是强引用
      缓存在这种集合中的数据比较可靠
      不会时不时当垃圾被回收掉
      我可设计这个缓存空间的大小
      当缓存满的时候，集合内部实现最近最少使用
      的算法，将最近最少使用的数据清除掉为新缓存
      的数据腾出应有的空间。
     */
    public static int maxSize = (int) Runtime.getRuntime().maxMemory() / 8;
    public static LruCache<Integer, Bitmap> photoCache = new LruCache<Integer, Bitmap>(maxSize) {

        //解决计算缓存图片大小的方法
        @Override
        protected int sizeOf(Integer key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }
    };

    public static List<Contact> getAllContacts(Context context) {
        List<Contact> contacts = new ArrayList<>();
        //获得内容解析器
        ContentResolver resolver = context.getContentResolver();
        //content://com.tanfa.aaa
        Uri contact_Uri = ContactsContract.Contacts.CONTENT_URI;
        String[] project = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.PHOTO_ID};
        Cursor cursor = resolver.query(contact_Uri, project, null, null, null);

        Uri data_Uri = ContactsContract.Data.CONTENT_URI;
        String[] dataProject = new String[]{ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1};
        //遍历游标
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            int _id = cursor.getInt(0);
            //当获得一个联系人的主键时
            //判断缓存中有没有该联系人的数据
            if (contactCache.get(_id) != null) {
                //如果根据联系人的主键查到的联系人的
                //数据存在，则不再从数据库中查找
                //把查到的联系人对象加到集合中
                contacts.add(contactCache.get(_id));
                continue;
            }
            int photoId = cursor.getInt(1);
            //把查得数据设置到联系人的对象中
            contact.set_id(_id);
            contact.setPhotoId(photoId);
            //查询当前的联系人的其它数据(姓名,电话,邮箱,地址)
            String selection = ContactsContract.Data.RAW_CONTACT_ID + "=?";
            String args[] = new String[]{String.valueOf(_id)};
            Cursor dataCursor = resolver.query(data_Uri, dataProject, selection, args, null);
            while (dataCursor.moveToNext()) {
                String mimeType = dataCursor.getString(0);
                String data1 = dataCursor.getString(1);
                if (IURI.MIMETYPE_ADDRESS.equals(mimeType)) {
                    //数据是地址
                    contact.setAddress(data1);
                } else if (IURI.MIMETYPE_EMAIL.equals(mimeType)) {
                    //数据是电子邮箱
                    contact.setEmail(data1);
                } else if (IURI.MIMETYPE_NAME.equals(mimeType)) {
                    contact.setName(data1);
                } else if (IURI.MIMETYPE_PHONE.equals(mimeType)) {
                    contact.setPhone(data1);
                }
            }
            dataCursor.close();
            //查到的联系人的对象保存到缓存集合
            contactCache.put(_id, contact);
            //把联系人加到集合中
            contacts.add(contact);
        }
        //关闭游标释放资源
        cursor.close();
        return contacts;
    }

    /**
     * 根据头像的编号查联系人的头像
     *
     * @param context
     * @param photoId
     * @return
     */
    public static Bitmap getPhotoByPhotoId(Context context, int photoId) {
        //每次获得一个联系人的头像的时候先从缓存中找
        //缓存中如果没有的话再从数据库里找
        Bitmap bitmap = photoCache.get(photoId);
        if (bitmap == null) {
            if (photoId == 0) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contact);
            } else {
                ContentResolver resolver = context.getContentResolver();
                Uri uri = ContactsContract.Data.CONTENT_URI;
                String[] project = new String[]{ContactsContract.Data.DATA15};
                String where = ContactsContract.Data._ID + "=?";
                String args[] = new String[]{String.valueOf(photoId)};
                Cursor cursor = resolver.query(uri, project, where, args, null);
                if (cursor.moveToNext()) {
                    byte[] photos = cursor.getBlob(0);
                    //把数据转换成bitmap
                    bitmap = BitmapFactory.decodeByteArray(photos, 0, photos.length);

                    if (bitmap != null) {
                        photoCache.put(photoId, bitmap);
                    }
                }
            }
        }
        return bitmap;
    }

    /**
     * 清空缓存数据
     */
    public static void clearCache(Contact contact) {
        contactCache.remove(contact.get_id());
        photoCache.remove(contact.getPhotoId());
    }

    /**
     * 删除联系人的执行逻辑
     * 1.通过访问内容提供者来执行删除联系的操作
     * 2.当我们要删除联系人时只需删除联系人的一个帐户
     * 当帐户被删除的时候此帐户下所有的数据会同步被删除
     * 另外如果这个帐户对应的联系人没有其它的帐户的话，
     * 联系人数据也会被删除
     *
     * @param context
     * @param contact
     */
    public static void deleteContactById(Context context, Contact contact) {
        //获得访问帐户的内容提供者的URI
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        //获得内容解析器
        ContentResolver resolver = context.getContentResolver();
        String where = ContactsContract.RawContacts._ID + "=?";
        String args[] = new String[]{String.valueOf(contact.get_id())};
        resolver.delete(uri, where, args);
    }

    public static List<Calllog> getAllCallLog(Context context) {
        List<Calllog> calllogs = new ArrayList<>();
        try {

            //获得内容解析器
            ContentResolver resolver = context.getContentResolver();
            //获得访问通话记录的Uri
            Uri uri = CallLog.Calls.CONTENT_URI;
            String projection[] = new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.TYPE,
                    CallLog.Calls.DATE,
            };
            String order = CallLog.Calls.DATE + " desc";
            Cursor cursor = resolver.query(uri, projection, null, null, order);
            while (cursor.moveToNext()) {
                //提取游标指针指向的当前记录
                int _id = cursor.getInt(0);
                String number = cursor.getString(1);
                int type = cursor.getInt(2);
                long date = cursor.getLong(3);

                //封装一个通话记录对象
                Calllog calllog = new Calllog(_id, null, date, null, 0, number, type);
                //根据电话号码查询联系人的姓名
                String name = getNameByNumber(context, number);
                calllog.setName(name);
                //根据电话号码查联系人的头像的编号
                int photoId = getPhotoIdByNumber(context, number);
                calllog.setPhoto_Id(photoId);
                calllog.setDateStr(DateUtil.formatDate(date));
                //把日志对象加到集合中
                calllogs.add(calllog);
            }
            cursor.close();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        return calllogs;
    }

    /**
     * 根据联系人的电话号码查询联系人的姓名
     *
     * @param context
     * @param number  电话号码
     * @return
     */
    public static String getNameByNumber(Context context, String number) {
        String name = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        ContentResolver resolver = context.getContentResolver();
        String[] projects = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = resolver.query(uri, projects, null, null, null);

        if (cursor.moveToNext()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    /**
     * 根据电话号码查联系人的头像编号
     *
     * @param context
     * @param number
     * @return
     */
    public static int getPhotoIdByNumber(Context context, String number) {
        int photoId = 0;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        ContentResolver resolver = context.getContentResolver();
        String[] projects = new String[]{ContactsContract.PhoneLookup.PHOTO_ID};
        Cursor cursor = resolver.query(uri, projects, null, null, null);
        if (cursor.moveToNext()) {
            photoId = cursor.getInt(0);
        }
        cursor.close();
        return photoId;
    }

    /**
     * 删除通话日志
     *
     * @param context
     * @param calllog
     */
    public static void deleteCallLogById(Context context, Calllog calllog) {
        try {
            ContentResolver resolver = context.getContentResolver();
            //获得Uri
            Uri uri = CallLog.Calls.CONTENT_URI;
            String where = CallLog.Calls._ID + "=?";
            String[] args = new String[]{String.valueOf(calllog.get_id())};
            resolver.delete(uri, where, args);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }
}
