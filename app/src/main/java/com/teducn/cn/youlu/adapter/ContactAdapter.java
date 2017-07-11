package com.teducn.cn.youlu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.entity.Contact;
import com.teducn.cn.youlu.manager.ContactManager;
import com.teducn.cn.youlu.manager.ImageManager;

/**
 * Created by pjy on 2017/7/3.
 */

public class ContactAdapter extends MyBaseAdapter<Contact> {
    public ContactAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            //如果view为null说明我适配的数据项是显示在第一屏的数据项
            //需要从布局文件中解析布局获得一个view
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.inflate_contact_item_layout, viewGroup, false);
            holder.imageView_Header = (ImageView) view.findViewById(R.id.imageView_Contact_Header);
            holder.textView_Name = (TextView) view.findViewById(R.id.textView_Contact_Name);
            //把holder存入view.tag里
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //获得要适配器的数据对象
        Contact contact = getItem(i);
        holder.textView_Name.setText(contact.getName());
        if (i == 0) {
            holder.imageView_Header.setImageResource(R.drawable.ic_add_contact);
        } else {
            int photoId = contact.getPhotoId();
            Bitmap header = ContactManager.getPhotoByPhotoId(context, photoId);
            //格式化头像为圆形头像
            header = ImageManager.formatBitmap(context, header);
            holder.imageView_Header.setImageBitmap(header);
        }

        return view;
    }

    private class ViewHolder {
        ImageView imageView_Header = null;
        TextView textView_Name = null;
    }
}
