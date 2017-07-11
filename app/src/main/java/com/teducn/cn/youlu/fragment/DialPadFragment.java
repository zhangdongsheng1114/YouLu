package com.teducn.cn.youlu.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teducn.cn.youlu.R;
import com.teducn.cn.youlu.adapter.CallLogAdapter;
import com.teducn.cn.youlu.entity.Calllog;
import com.teducn.cn.youlu.manager.ContactManager;
import com.teducn.cn.youlu.manager.MediaManager;

import java.util.List;

/**
 * Created by pjy on 2017/6/30.
 */

public class DialPadFragment extends BaseFragment {
    ListView listView_DialPad;
    ImageView imageView_Left;
    ImageView imageView_Right;
    TextView textView_Title;
    CallLogAdapter adapter = null;
    RelativeLayout relativeLayout_DialPad;
    ImageButton imageButton_Call;
    boolean isAnim = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_dial_pad, container, false);
        initialUI();
        setListener();
        initialDailPad();
        return contentView;
    }

    private void setListener() {
        textView_Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 13) {
                    //删除多余的内容
                    editable.delete(13, editable.length());
                }
            }
        });
        imageView_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = textView_Title.getText().toString();
                if (title.equals("拨打电话")) {
                    return;
                }

                if (title.length() == 1) {
                    textView_Title.setText("拨打电话");
                } else if (title.length() == 5 || title.length() == 10) {
                    textView_Title.setText(title.substring(0, (title.length() - 2)));
                } else {
                    textView_Title.setText(title.substring(0, (title.length() - 1)));
                }
            }
        });

        imageButton_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnim) {
                    return;
                }
                //播放音效
                MediaManager.playMusic(getContext(), R.raw.a);
                //激活系统打电话的模块儿
                Intent intent = new Intent(Intent.ACTION_CALL);
                String phone = textView_Title.getText().toString();
                Uri uri = Uri.parse("tel:" + phone);
                intent.setData(uri);

                startActivity(intent);
            }
        });

    }

    private static String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "*", "#"};

    //创建拨号键盘
    private void initialDailPad() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //获得屏幕的宽度和高度
        int width = metrics.widthPixels / 3;
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55,
                getResources().getDisplayMetrics());
        for (int i = 0; i < keys.length; i++) {
            final TextView key = new TextView(getContext());
            //设置textView上的文本
            key.setText(keys[i]);
            key.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            key.setId(i + 1);
            key.setGravity(Gravity.CENTER);

            //设置按键的宽度高度以及在布局上出现在位置
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
            //当循环变量对3的模不等于0时需要设置右对齐规则
            if (i % 3 != 0) {
                params.addRule(RelativeLayout.RIGHT_OF, i);
            }
            if (i >= 3) {
                params.addRule(RelativeLayout.BELOW, (i - 2));
            }
            //把按键添加到布局上
            relativeLayout_DialPad.addView(key, params);

            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isAnim) {
                        return;
                    }

                    MediaManager.playMusic(getContext(), R.raw.b);
                    //获得标题文本
                    String title = textView_Title.getText().toString();
                    if (title.equals("拨打电话")) {
                        textView_Title.setText(key.getText().toString());
                    } else if (title.length() == 3 || title.length() == 8) {
                        textView_Title.append("-");
                        textView_Title.append(key.getText().toString());
                    } else {
                        textView_Title.append(key.getText().toString());

                    }
                }
            });

        }
    }

    @Override
    public void initialUI() {
        actionBar = (LinearLayout) contentView.findViewById(R.id.actionBar_DialPad);
        initialActionBar(R.drawable.ic_add_icon,
                "拨打电话", R.drawable.ic_backspace);
        imageView_Left = (ImageView) actionBar.findViewById(
                R.id.imageView_ActionBar_Left);
        imageView_Right = (ImageView) actionBar.findViewById(
                R.id.imageView_ActionBar_Right);
        textView_Title = (TextView) actionBar.findViewById(
                R.id.textView_ActionBar_Title);
        relativeLayout_DialPad = (RelativeLayout) contentView.findViewById(
                R.id.relativeLayout_DialPad);
        listView_DialPad = (ListView) contentView.findViewById(
                R.id.listView_DialPad);
        imageButton_Call = (ImageButton) contentView.findViewById(
                R.id.imageView_DialPad_Call);

        textView_Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

        adapter = new CallLogAdapter(getContext());
        listView_DialPad.setAdapter(adapter);
        List<Calllog> calllogList = ContactManager.getAllCallLog(getContext());
        adapter.addDatas(calllogList, true);
    }

    /**
     * 拨号键盘的动画
     */
    public void dialPadAnim() {
        //将动画文件转换成动画对象
        ScaleAnimation animation = (ScaleAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.dialpad_anim);
        //应用动画
        relativeLayout_DialPad.setAnimation(animation);
        //监听动画的状态
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dialPadAnim();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
}
