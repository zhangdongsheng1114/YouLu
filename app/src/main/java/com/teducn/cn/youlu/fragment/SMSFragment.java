package com.teducn.cn.youlu.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.ConversationAdapter;
import com.teducn.cn.youlu.entity.Conversation;
import com.teducn.cn.youlu.manager.SMSManager;
import com.teducn.cn.youlu.ui.ChatActivity;

import java.util.List;

/**
 * Created by pjy on 2017/6/30.
 */

public class SMSFragment extends BaseFragment {
    ListView listView_Conversation;
    ConversationAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_sm, container, false);
        initialUI();
        setListener();
        return contentView;
    }


    private void setListener() {
        listView_Conversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversation conversation = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("thread_Id", conversation.getThread_Id());
                intent.putExtra("name", conversation.getName());
                intent.putExtra("address", conversation.getNumber());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initialUI() {
        actionBar = (LinearLayout) contentView.findViewById(R.id.actionBar_Conversation);
        initialActionBar(-1, "短消息", -1);

        listView_Conversation = (ListView) contentView.findViewById(R.id.listView_Conversation);
        adapter = new ConversationAdapter(getContext());
        listView_Conversation.setAdapter(adapter);

        List<Conversation> conversationList = SMSManager.getAllConversations(getContext());
        adapter.addDatas(conversationList, true);
    }
}

