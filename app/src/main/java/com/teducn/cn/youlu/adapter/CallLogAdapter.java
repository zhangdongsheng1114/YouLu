package com.teducn.cn.youlu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.entity.Calllog;
import com.teducn.cn.youlu.manager.ContactManager;
import com.teducn.cn.youlu.manager.ImageManager;

/**
 * Created by pjy on 2017/7/6.
 */

public class CallLogAdapter extends MyBaseAdapter<Calllog> {
    public CallLogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            //说明适配的数据是第一屏的数据
            //内存中没有可以重用的view对象
            //根据布局文件解析中一个view
            view = inflater.inflate(R.layout.inflate_calllog_item_layout, viewGroup, false);
            //获得所有的控件
            //把获得的控件保存在holder
            viewHolder = new ViewHolder();
            viewHolder.imageView_Photo = (ImageView) view.findViewById(R.id.imageView_CallLog_Header);
            viewHolder.imageView_Warning = (ImageView) view.findViewById(R.id.imageView_CallLog_Warning);
            viewHolder.imageView_Type = (ImageView) view.findViewById(R.id.imageView_CallLog_Going);

            viewHolder.textView_Name = (TextView) view.findViewById(R.id.textView_CallLog_Name);
            viewHolder.textView_Number = (TextView) view.findViewById(R.id.textView_CallLog_Number);
            viewHolder.textView_Date = (TextView) view.findViewById(R.id.textView_CallLog_Date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //获得可以适配的数据对象
        Calllog calllog = getItem(i);
        String name = calllog.getName();
        if (TextUtils.isEmpty(name)) {
            //产生通话记录的联系人的是一个陌生人
            viewHolder.textView_Name.setText("未知号码");
            viewHolder.textView_Name.setTextColor(Color.RED);
            viewHolder.imageView_Warning.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textView_Name.setText(name);
            viewHolder.textView_Name.setTextColor(Color.BLACK);
            viewHolder.imageView_Warning.setVisibility(View.INVISIBLE);
        }
        viewHolder.textView_Number.setText(calllog.getNumber());
        viewHolder.textView_Date.setText(calllog.getDateStr());
        //头像
        int photoId = calllog.getPhoto_Id();
        //根据头像编号查头像
        Bitmap header = ContactManager.getPhotoByPhotoId(context, photoId);
        //头像的格式化处理
        header = ImageManager.formatBitmap(context, header);
        viewHolder.imageView_Photo.setImageBitmap(header);

        if (calllog.getType() == CallLog.Calls.OUTGOING_TYPE) {
            viewHolder.imageView_Type.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageView_Type.setVisibility(View.INVISIBLE);
        }

        return view;
    }


    private class ViewHolder {
        ImageView imageView_Photo;
        TextView textView_Name;
        TextView textView_Number;
        ImageView imageView_Warning;
        ImageView imageView_Type;
        TextView textView_Date;
    }
}
