package com.teducn.cn.youlu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.entity.SMS;
import com.teducn.cn.youlu.manager.ContactManager;
import com.teducn.cn.youlu.manager.ImageManager;

/**
 * Created by pjy on 2017/7/10.
 */

public class SMSAdapter extends MyBaseAdapter<SMS> {

    public static final int LEFT = 1;  // 左边布局编号
    public static final int RIGHT = 2;  // 右边布局编号

    public SMSAdapter(Context context) {
        super(context);
    }


    @Override
    public int getItemViewType(int position) {
        int type = getItem(position).getType();
        return type - 1;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //获得要适配的数据对象
        SMS sms = getItem(i);
        int type = getItemViewType(i);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            if (type == LEFT) {
                view = inflater.inflate(R.layout.inflate_sms_left_layout, viewGroup, false);
            } else if (type == RIGHT) {
                view = inflater.inflate(R.layout.inflate_sms_right_layout, viewGroup, false);
            }
            holder.textView_Date = (TextView) view.findViewById(R.id.textView_SMS_Date);
            holder.imageView_Header = (ImageView) view.findViewById(R.id.imageView_SMS_Header);
            holder.textView_Body = (TextView) view.findViewById(R.id.textView_SMS_Body);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView_Date.setText(sms.getDateStr());
        holder.textView_Body.setText(sms.getBody());
        if (type == LEFT) {
            //发来的短信
            int photo_id = sms.getPhoto_Id();
            Bitmap header = ContactManager.getPhotoByPhotoId(context, photo_id);
            header = ImageManager.formatBitmap(context, header);
            holder.imageView_Header.setImageBitmap(header);
        } else if (type == RIGHT) {
            //发出的短信显示联系人默认头像
            Bitmap header = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_contact_selected);
            header = ImageManager.formatBitmap(context, header);
            holder.imageView_Header.setImageBitmap(header);
        }
        return view;
    }

    public class ViewHolder {
        TextView textView_Date;
        ImageView imageView_Header;
        TextView textView_Body;
    }
}
