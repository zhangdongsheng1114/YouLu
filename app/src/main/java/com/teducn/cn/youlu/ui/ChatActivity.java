package com.teducn.cn.youlu.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.SMSAdapter;
import com.teducn.cn.youlu.entity.SMS;
import com.teducn.cn.youlu.manager.SMSManager;

import java.util.List;

public class ChatActivity extends Activity {

    static ListView listView_Chat;
    static SMSAdapter adapter = null;

    static int thread_Id;//会话的编号
    String name; // 联系人姓名
    static String address;  // 电话号码

    ImageView imageView_Lef;
    TextView textView_Title;
    ImageView imageView_Right;
    static Context context;

    EditText editText_Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = getApplicationContext();
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
        editText_Message = (EditText) findViewById(R.id.editText_Chat_Content);
        listView_Chat = (ListView) findViewById(R.id.listView_Chat);
        adapter = new SMSAdapter(this);
        listView_Chat.setAdapter(adapter);

        smsRefresh();
    }

    private static void smsRefresh() {
        List<SMS> smses = SMSManager.getSMSSesByThreadId(context, thread_Id);
        adapter.addDatas(smses, true);
        listView_Chat.setSelection(smses.size() - 1);
    }

    private void initialData() {
        Intent intent = getIntent();
        thread_Id = intent.getIntExtra("thread_Id", 0);
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
    }

    public void sent(View view) {
        // 获得editText中的短信内容
        String message = editText_Message.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            // 把这条信息发出去
            SMSManager.sentSMS(this, message, address);
            editText_Message.setText("");
        } else {
            Toast.makeText(this, "输入聊天内容", Toast.LENGTH_SHORT).show();
        }
    }

    public static class SMSReceiver extends BroadcastReceiver {
        //当广播接收器被激活的时候我们可以判断激活广播接收器的广播意图是什么
        //收到短信的意图动作是
        public static final String RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVE_SMS)) {
                //说明是收到短信了
                Log.i("TAG:SMSReceiver", "收到短信了");
                //去进行短信的解析
                SMS sms = SMSManager.onReceiveSMS(context, intent);
                Log.i("TAG", sms.toString());
                // 判断发来短信联系人的电话号码是否和当前联系人一致
                if (sms.getAddress().equals(address)) {
                    // 把短信的内容保存在收件箱中
                    SMSManager.saveReceiveSMS(context, sms, thread_Id);
                    // 把短信的内容要刷新到来聊天界面上
                    smsRefresh();
                }
            } else if (action.equals(SMSManager.SENT_SMS)) {
                Log.i("TAG:sms", "有短信发出去了");
                // 获得发出的短信的内容
                String body = intent.getStringExtra("body");
                String address = intent.getStringExtra("address");
                // 把发出去的短信保存到发件箱中
                Log.i("TAG:sms", body);
                Log.i("TAG:sms", address);
                SMSManager.saveSentSMS(context, body, address);
                // 刷新UI
                smsRefresh();
            }
        }
    }
}

