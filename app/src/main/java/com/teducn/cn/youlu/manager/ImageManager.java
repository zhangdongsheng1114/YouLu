package com.teducn.cn.youlu.manager;

/**
 * Created by tarena on 2017/7/10.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

/**
 * Created by pjy on 2017/7/3.
 */

public class ImageManager {
    public static Bitmap formatBitmap(Context context, Bitmap bitmap) {
        //获得头像的高度
        int height = bitmap.getHeight();
        //获得头像的宽度
        int width = bitmap.getWidth();

        Bitmap backBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //创建一个画布
        Canvas canvas = new Canvas(backBitmap);
        //获得圆形的半径
        int r = Math.min(width, height) / 2;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        //画笔抗锯齿的能力
        paint.setAntiAlias(true);

        canvas.drawCircle(width / 2, height / 2, r, paint);
        //设置画笔的模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        //在圆形头像的基础上绘制圆环
        //设置画笔的颜色
        paint.setColor(Color.WHITE);
        //设置画笔的样式
        paint.setStyle(Paint.Style.STROKE);
        //把2dp转换成和当前设备相关的一个绝对的相素
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
        paint.setStrokeWidth(strokeWidth);
        //画圆
        canvas.drawCircle(width / 2, height / 2, r - strokeWidth / 2, paint);
        return backBitmap;
    }
}
