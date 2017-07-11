package com.teducn.cn.youlu.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.SMSAdapter;
import com.teducn.cn.youlu.entity.SMS;
import com.teducn.cn.youlu.manager.SMSManager;

import java.util.List;

public class ChatActivity extends Activity {

    ListView listView_Chat;
    SMSAdapter adapter = null;

    int thread_Id;//会话的编号
    String name; // 联系人姓名
    String address;  // 电话号码

    ImageView imageView_Lef;
    TextView textView_Title;
    ImageView imageView_Right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initialData();
        initialActionBar();
        initialUI();
        setListener();
    }

    private void setListener() {
        imageView_Lef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialActionBar() {
        imageView_Lef = (ImageView) findViewById(R.id.imageView_ActionBar_Left);
        textView_Title = (TextView) findViewById(R.id.textView_ActionBar_Title);
        imageView_Right = (ImageView) findViewById(R.id.imageView_ActionBar_Right);
        imageView_Right.setVisibility(View.INVISIBLE);
        imageView_Lef.setImageResource(R.drawable.ic_back);
        if (TextUtils.isEmpty(name)) {
            textView_Title.setText(address);
        } else {
            textView_Title.setText(name);
        }
    }


    private void initialUI() {
        listView_Chat = (ListView) findViewById(R.id.listView_Chat);
        adapter = new SMSAdapter(this);
        listView_Chat.setAdapter(adapter);

        List<SMS> smses = SMSManager.getSMSSesByThreadId(this, thread_Id);
        adapter.addDatas(smses, true);
    }

    private void initialData() {
        Intent intent = getIntent();
        thread_Id = intent.getIntExtra("thread_Id", 0);
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
    }

    public static class SMSReceiver extends BroadcastReceiver {
        //当广播接收器被激活的时候我们可以判断激活广播接收器的广播意图是什么
        //收到短信的意图动作是
        public static final String RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(
                Context context,
                Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVE_SMS)) {
                //说明是收到短信了
                Log.i("TAG:SMSReceiver", "收到短信了");
                //去进行短信的解析
                SMS sms = SMSManager.onReceiveSMS(context, intent);
            }
        }
    }
}

