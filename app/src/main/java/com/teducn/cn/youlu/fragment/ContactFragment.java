package com.teducn.cn.youlu.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.ContactAdapter;
import com.teducn.cn.youlu.entity.Contact;
import com.teducn.cn.youlu.manager.ContactManager;
import com.teducn.cn.youlu.manager.DialogManager;

import java.util.List;


/**
 * Created by pjy on 2017/6/30.
 */

public class ContactFragment extends BaseFragment {
    GridView gridView_Contact;
    ContactAdapter adapter = null;
    boolean permissionFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_contact, container, false);
        initialUI();
        setListener();
        requestPerm();
        return contentView;
    }

    //发起运行时权限授权的请求
    private void requestPerm() {
        //判断访问联系人的权限有没有经过有户授权，
        //如果没有则发起授权的请求
        //如果已经授权则直接执行权限约束下的功能
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //如果没有被用户授予访问的权限那要发起授权确认的请求
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, 100);
        } else {
            refreshContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (PackageManager.PERMISSION_GRANTED != grantResults[0]) {
                        Toast.makeText(getContext(), "你必须同意所有的权限,才可以执行权限下的操作", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                //调用授权之后可以执行的操作
                permissionFlag = true;
                refreshContact();
            } else {
                Toast.makeText(getContext(), "你没有执行授权处理", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setListener() {
        //给控件注册项点击事件监听器
        gridView_Contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    //弹出添加联系人的对话框
                    DialogManager.showAddContactDialog(getContext());
                } else {
                    //弹出联系人详情对话框
                    //获得点击的联系人
                    Contact contact = adapter.getItem(i);
                    DialogManager.showDetailContactDialog(getContext(), contact);
                }
            }
        });

        gridView_Contact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获得要删除的联系人
                Contact contact = adapter.getItem(i);
                DialogManager.showDeleteContactDialog(getContext(), contact, adapter);
                return true;
            }
        });

    }

    @Override
    public void initialUI() {
        actionBar = (LinearLayout) contentView.findViewById(R.id.actionBar_Contact);
        initialActionBar(-1, "联系人", R.drawable.ic_search);

        gridView_Contact = (GridView) contentView.findViewById(R.id.gridView_Contact);
        adapter = new ContactAdapter(getContext());

        gridView_Contact.setAdapter(adapter);
        //快捷键Ctrl+ALT+M实现方法的重构
        //refreshContact();

    }

    private void refreshContact() {
        //获得所有的联系人的数据
        List<Contact> contacts = ContactManager.getAllContacts(getContext());
        Contact contact = new Contact(-1, "添加联系人", null, null, null, 0);
        //把新增加到联系人添加到集合中
        contacts.add(0, contact);
        adapter.addDatas(contacts, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (permissionFlag) {
            refreshContact();
        }
    }
}
