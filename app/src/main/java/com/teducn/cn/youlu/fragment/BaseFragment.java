package com.teducn.cn.youlu.fragment;


import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teducn.cn.youlu.R;


/**
 * Created by pjy on 2017/6/30.
 */

public abstract class BaseFragment extends Fragment {
    protected View contentView;
    protected LinearLayout actionBar;

    public abstract void initialUI();

    protected void initialActionBar(int leftId, String title, int rightId) {
        if (actionBar == null) {
            //如果actionBar属性为空，说明该fragment当中没有actionBar
            return;
        }
        ImageView imageView_Left = (ImageView) actionBar.findViewById(R.id.imageView_ActionBar_Left);
        TextView textView_Title = (TextView) actionBar.findViewById(R.id.textView_ActionBar_Title);
        ImageView imageView_Right = (ImageView) actionBar.findViewById(R.id.imageView_ActionBar_Right);

        if (leftId <= 0) {
            //隐藏控件
            //INVISIBLE GONE
            imageView_Left.setVisibility(View.INVISIBLE);
        } else {
            imageView_Left.setVisibility(View.VISIBLE);
            imageView_Left.setImageResource(leftId);
        }
        if (TextUtils.isEmpty(title)) {
            textView_Title.setVisibility(View.INVISIBLE);
        } else {
            textView_Title.setVisibility(View.VISIBLE);
            textView_Title.setText(title);
        }
        if (rightId <= 0) {
            imageView_Right.setVisibility(View.INVISIBLE);
        } else {
            imageView_Right.setVisibility(View.VISIBLE);
            imageView_Right.setImageResource(rightId);
        }
    }
}
