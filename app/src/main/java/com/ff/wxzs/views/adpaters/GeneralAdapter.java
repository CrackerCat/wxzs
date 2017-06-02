package com.ff.wxzs.views.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zhangkai on 2017/2/16.
 */

public abstract class GeneralAdapter<T> extends BaseAdapter {
    public Context context;
    protected LayoutInflater inflater;

    public List<T> dataInfos;


    public GeneralAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataInfos != null ? dataInfos.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return dataInfos != null ? dataInfos.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
