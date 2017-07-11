package com.teducn.cn.youlu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.teducn.cn.youlu.R;

public class SplashActivity extends Activity {

    ImageView imageView_Splash;
    Animation animation_Splash;//声明一个动画对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView_Splash = (ImageView)
                findViewById(R.id.imageView_Splash);
        //把动画文件转成一个动画对象
        animation_Splash = AnimationUtils.loadAnimation(this, R.anim.splash);
        //将动画对象应用到控件上
        imageView_Splash.setAnimation(animation_Splash);
        //监听动画的状态
        animation_Splash.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束时实现窗口的跳转
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_anim, R.anim.out_anim);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}