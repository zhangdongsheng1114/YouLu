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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.CallLogAdapter;
import com.teducn.cn.youlu.entity.Calllog;
import com.teducn.cn.youlu.manager.ContactManager;
import com.teducn.cn.youlu.manager.DialogManager;

import java.util.List;

/**
 * Created by pjy on 2017/6/30.
 */

public class CallLogFragment extends BaseFragment {
    ListView listView_CallLog = null;
    CallLogAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_call_log, container, false);
        initialUI();
        setListener();
        //实现运行时权限的授权处理
        requestPerm();
        return contentView;
    }

    private void requestPerm() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            //发起授权确认的请求
            requestPermissions(new String[]{Manifest.permission.WRITE_CALL_LOG}, 101);
        } else {
            loadDatas();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(),
                                "只有通过授权,才可以执行相关操作", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                loadDatas();
            } else {
                Toast.makeText(getContext(), "所有的权限经过授权处理后,才可以执行操作",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setListener() {
        listView_CallLog.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Calllog calllog = adapter.getItem(i);
                DialogManager.showDeleteCalllogDialog(getContext(), calllog, adapter);
                return true;
            }
        });
    }

    @Override
    public void initialUI() {
        //初始化标题布局
        actionBar = (LinearLayout) contentView.findViewById(R.id.actionBar_CallLog);
        initialActionBar(-1, "通话记录", -1);

        listView_CallLog = (ListView) contentView.findViewById(R.id.listView_CallLog);
        adapter = new CallLogAdapter(getContext());
        listView_CallLog.setAdapter(adapter);
        // loadDatas();

    }

    private void loadDatas() {
        //获得所有的通话数据
        List<Calllog> calllogs = ContactManager.getAllCallLog(getContext());
        //将通话记录添加到适配器中
        adapter.addDatas(calllogs, true);
    }
}
