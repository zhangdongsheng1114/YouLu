package com.teducn.cn.youlu.manager;

/**
 * Created by tarena on 2017/7/10.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by pjy on 2017/7/7.
 */

public class MediaManager {
    public static SoundPool soundPool = null;
    /**
     * 播放音效方法
     *
     * @param context
     * @param resId
     */
    public static void playMusic(Context context, int resId) {
        if (soundPool == null) {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }
        //给音效池注册一个加载监听
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                //一旦该方法被回调说明音乐已经加载完成
                //可以执行音乐的播放了
                soundPool.play(i, 1.0f, 1.0f, 1, 0, 1.0f);
            }
        });
        //音乐资源的加载
        soundPool.load(context, resId, 1);

    }
    public static void release(){
        if(soundPool!=null){
            soundPool.release();
            soundPool=null;
        }
    }
}
