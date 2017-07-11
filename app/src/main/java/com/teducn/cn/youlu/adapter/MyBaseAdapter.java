package com.teducn.cn.youlu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjy on 2017/7/3.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    //保存要适配的数据对象的集合
    private List<T> datas = new ArrayList<T>();
    public LayoutInflater inflater = null;
    public Context context = null;

    public MyBaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 向适配器集合中添加要适配的数据
     */
    public void addDatas(List<T> list, boolean isClear) {
        if (isClear) {
            datas.clear();
        }
        if (list != null) {
            datas.addAll(list);
            //通适配器绑定的UI进行数据更新
            notifyDataSetChanged();
        }
    }

    //删除所有的数据
    public void removeDatas() {
        datas.clear();
        notifyDataSetChanged();
    }

    public void removeDatas(T t) {
        datas.remove(t);
        notifyDataSetChanged();
    }

    //获得适配器数据
    public List<T> getDatas() {
        return datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

}
