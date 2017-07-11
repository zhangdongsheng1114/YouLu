package com.teducn.cn.youlu.manager;

/**
 * Created by tarena on 2017/7/10.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.CallLogAdapter;
import com.teducn.cn.youlu.adapter.ContactAdapter;
import com.teducn.cn.youlu.entity.Calllog;
import com.teducn.cn.youlu.entity.Contact;

/**
 * Created by pjy on 2017/7/4.
 */

public class DialogManager {
    public static void showAddContactDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final AlertDialog addDialog = builder.create();
        addDialog.show();

        //解析自定义的对话框布局
        final View dialogView = View.inflate(context, R.layout.inflate_contactadd_dialog_layout, null);
        //将解析出来的View设置给对话框
        addDialog.setContentView(dialogView);


        ImageView imageView_Close = (ImageView) dialogView.findViewById(R.id.imageView_ContactAdd_Close);
        ImageView imageView_Confirm = (ImageView) dialogView.findViewById(R.id.imageView_ContactAdd_Confirm);

        final EditText editText_Name = (EditText) dialogView.findViewById(R.id.editText_ContactAdd_Name);
        final EditText editText_Number = (EditText) dialogView.findViewById(R.id.editText_ContactAdd_Number);
        final EditText editText_Email = (EditText) dialogView.findViewById(R.id.editText_ContactAdd_Email);
        final EditText editText_Postal = (EditText) dialogView.findViewById(R.id.editText_ContactAdd_Postal);
        final EditText editText_Company = (EditText) dialogView.findViewById(R.id.editText_ContactAdd_Company);

        imageView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭对话框
                addDialog.dismiss();
            }
        });
        imageView_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获得联系的信息
                String name = editText_Name.getText().toString();
                String number = editText_Number.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)) {
                    Toast.makeText(
                            context,
                            "姓名或电话号是必填项",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //获得联系人的其它的信息
                String email = editText_Email.getText().toString();
                String postal = editText_Postal.getText().toString();
                String company = editText_Company.getText().toString();
                //激活系统的添加联系人的组件,实现联系人的添加
                //隐式意图和显示意图
                Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT);
                Uri uri = Uri.parse("tel:" + number);
                intent.setData(uri);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, postal);
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);

                context.startActivity(intent);
                addDialog.dismiss();
            }

        });

    }

    public static void showDetailContactDialog(final Context context, final Contact contact) {
        //显示对话框
        //初始化对话框上的控件
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog detailDialog = builder.create();
        detailDialog.show();

        View dialogView = View.inflate(context, R.layout.inflate_contactdedtail_dialog_layout, null);
        detailDialog.setContentView(dialogView);

        ImageView imageView_Edit = (ImageView) dialogView.findViewById(R.id.imageView_ContactDetail_Edit);
        ImageView imageView_Close = (ImageView) dialogView.findViewById(R.id.imageView_ContactDetail_Close);
        ImageView imageView_Header = (ImageView) dialogView.findViewById(R.id.imageView_ContactDetail_Header);

        TextView textView_Name = (TextView) dialogView.findViewById(R.id.textView_ContactDetail_Name);
        TextView textView_Number = (TextView) dialogView.findViewById(R.id.textView_ContactDetail_Number);
        TextView textView_Postal = (TextView) dialogView.findViewById(R.id.textView_ContactDetail_Postal);
        TextView textView_Email = (TextView) dialogView.findViewById(R.id.textView_ContactDetail_Email);

        //获得联系人的头像
        Bitmap header = ContactManager.getPhotoByPhotoId(context, contact.getPhotoId());
        //格式化头像
        header = ImageManager.formatBitmap(context, header);
        //设置头像
        imageView_Header.setImageBitmap(header);

        //设置联系人的姓名
        textView_Name.setText(contact.getName());
        //设置联系人的电话
        textView_Number.setText(contact.getPhone());
        //设置联系人的邮箱
        textView_Email.setText(contact.getEmail());
        //设置联系人的地址
        textView_Postal.setText(contact.getAddress());

        imageView_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailDialog.dismiss();
            }
        });
        imageView_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现联系人的编辑处理
                //在编辑之前清空该联系人在缓存中的数据
                ContactManager.clearCache(contact);
                //激活系统联系人的编辑的组件,实现当前联系人的编辑

                Intent intent = new Intent(Intent.ACTION_EDIT);
                Uri uri = Uri.parse("content://contacts/people/" + contact.get_id());
                intent.setData(uri);

                intent.putExtra("finishActivityOnSaveCompleted", true);

                context.startActivity(intent);
                //关闭当前的对话框
                detailDialog.dismiss();
            }
        });
    }

    //封装一个方法,显示对话框,提示用户要删除联系人
    public static void showDeleteContactDialog(final Context context, final Contact contact, final ContactAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("系统提示");
        builder.setMessage("确定要删除当前的联系人的吗?");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //如果该联系人在缓存中有数据的话先清空缓存
                ContactManager.clearCache(contact);
                //实现删除联系人的方法
                ContactManager.deleteContactById(context, contact);
                //将联系人在适配器中的数据同步删除
                adapter.removeDatas(contact);
                //关闭对话框
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("再想想", null);

        builder.create().show();
    }

    /**
     * 显示删除通话记录的对话框
     *
     * @param context
     * @param calllog 被删除的对象
     * @param adapter 通话记录的适配器
     */
    public static void showDeleteCalllogDialog(final Context context, final Calllog calllog, final CallLogAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("系统提示");
        builder.setMessage("确定要删除当前的通话记录吗?");
        builder.setNegativeButton("再想想", null);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ContactManager.deleteCallLogById(context, calllog);
                adapter.removeDatas(calllog);
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}

