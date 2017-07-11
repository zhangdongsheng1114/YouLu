package com.teducn.cn.youlu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.MyFragmentPagerAdapter;
import com.teducn.cn.youlu.fragment.CallLogFragment;
import com.teducn.cn.youlu.fragment.ContactFragment;
import com.teducn.cn.youlu.fragment.DialPadFragment;
import com.teducn.cn.youlu.fragment.SMSFragment;

public class MainActivity extends FragmentActivity {

    ViewPager viewPager_Main;
    MyFragmentPagerAdapter adapter = null;
    CallLogFragment callLogFragment;
    ContactFragment contactFragment;
    DialPadFragment dialPadFragment;
    SMSFragment smsFragment;
    RadioGroup radioGroup_Bottom = null;
    String defaultSmsApp = "";//默认短信应用的包名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialFragments();
        setListener();
        setDefaultSMS();
    }

    private void setDefaultSMS() {
        //获得原有的默认短信应用
        defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
        startActivity(intent);
    }

    private void setListener() {
        //设置radiogroup的状态监听
        //设置viewpager状态监听
        viewPager_Main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //当所选择的页面发生变化时，获得选择页面在集合当中的位置
                //根据位置改变单选按钮的选择
                switch (position) {
                    case 0:
                        //第一个单选按钮被选中
                        radioGroup_Bottom.check(R.id.radioButton_CallLog);
                        break;
                    case 1:
                        //第二个单选按钮被选中
                        radioGroup_Bottom.check(R.id.radioButton_Contact);
                        break;
                    case 2:
                        //第三个单选按钮被选中
                        radioGroup_Bottom.check(R.id.radioButton_DialPad);
                        break;
                    case 3:
                        //第四个单选按钮被选中
                        radioGroup_Bottom.check(R.id.radioButton_SMS);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        radioGroup_Bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton_CallLog:
                        //将当前切换成通话记录页面
                        viewPager_Main.setCurrentItem(0, false);
                        break;
                    case R.id.radioButton_Contact:
                        //将当前切换成联系人页面
                        viewPager_Main.setCurrentItem(1, false);
                        break;
                    case R.id.radioButton_DialPad:
                        //将当前切换成打电话页面
                        viewPager_Main.setCurrentItem(2, false);
                        break;
                    case R.id.radioButton_SMS:
                        //将当前切换成聊天页面
                        viewPager_Main.setCurrentItem(3, false);
                        break;
                }

            }
        });
    }

    private void initialFragments() {
        viewPager_Main = (ViewPager) findViewById(R.id.viewPager_Main);
        radioGroup_Bottom = (RadioGroup) findViewById(R.id.radioGroup_Bottom);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        callLogFragment = new CallLogFragment();
        contactFragment = new ContactFragment();
        dialPadFragment = new DialPadFragment();
        smsFragment = new SMSFragment();
        //把fragment添加到适配器集合中
        adapter.addFragment(callLogFragment);
        adapter.addFragment(contactFragment);
        adapter.addFragment(dialPadFragment);
        adapter.addFragment(smsFragment);
        //将适配器和viewpager进行关联
        viewPager_Main.setAdapter(adapter);
        //将联系人的fragment设置为默认的fragment

        viewPager_Main.setCurrentItem(1, false);
    }

    @Override
    protected void onDestroy() {
        //把原来的系统短信应用还原回默认短信应用
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
        startActivity(intent);
        super.onDestroy();
    }
}
