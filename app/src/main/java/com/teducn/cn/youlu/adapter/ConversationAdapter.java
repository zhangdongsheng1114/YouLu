package com.teducn.cn.youlu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.entity.Conversation;
import com.teducn.cn.youlu.manager.ContactManager;
import com.teducn.cn.youlu.manager.ImageManager;

/**
 * Created by pjy on 2017/7/7.
 */

public class ConversationAdapter extends MyBaseAdapter<Conversation> {

    public static final int READ = 1;
    public static final int UNREAD = 0;

    public ConversationAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.inflate_conversation_item_layout, viewGroup, false);
            holder = new ViewHolder();
            holder.imageView_Header = (ImageView) view.findViewById(R.id.imageView_Conversation_Header);
            holder.imageView_UnRead = (ImageView) view.findViewById(R.id.imageView_Conversation_UnRead);
            holder.imageView_Warning = (ImageView) view.findViewById(R.id.imageView_Conversation_Warning);

            holder.textView_Name = (TextView) view.findViewById(R.id.textView_Conversation_Name);
            holder.textView_Body = (TextView) view.findViewById(R.id.textView_Conversation_Body);
            holder.textView_Date = (TextView) view.findViewById(R.id.textView_Conversation_Date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Conversation conversation = getItem(i);
        String name = conversation.getName();
        if (TextUtils.isEmpty(name)) {
            holder.textView_Name.setText(conversation.getNumber());
            holder.textView_Name.setTextColor(Color.RED);
            holder.imageView_Warning.setVisibility(View.VISIBLE);
        } else {
            holder.textView_Name.setText(name);
            holder.textView_Name.setTextColor(Color.BLACK);
            holder.imageView_Warning.setVisibility(View.INVISIBLE);
        }
        holder.textView_Body.setText(conversation.getBody());
        holder.textView_Date.setText(conversation.getDateStr());

        int read = conversation.getRead();
        if (read == READ) {
            holder.imageView_UnRead.setVisibility(View.INVISIBLE);
        } else if (read == UNREAD) {
            holder.imageView_UnRead.setVisibility(View.VISIBLE);
        }

        int photo_Id = conversation.getPhotoId();
        Bitmap header = ContactManager.getPhotoByPhotoId(context, photo_Id);
        header = ImageManager.formatBitmap(context, header);
        holder.imageView_Header.setImageBitmap(header);

        return view;
    }

    private class ViewHolder {
        ImageView imageView_Header;
        ImageView imageView_UnRead;
        ImageView imageView_Warning;

        TextView textView_Name;
        TextView textView_Body;
        TextView textView_Date;
    }

}
