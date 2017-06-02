package com.ff.wxzs.views.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ff.wxzs.R;
import com.ff.wxzs.domain.QAListInfo;

/**
 * Created by zhangkai on 2017/3/20.
 */

public class VipFunctionAdapter extends GeneralAdapter<QAListInfo.GoodInfo> implements SpinnerAdapter {
    public VipFunctionAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_vip_function, null);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
        QAListInfo.GoodInfo goodInfo = dataInfos.get(position);
        tvTitle.setText("套餐（" + (position + 1) + "）：" + goodInfo.getTitle());
        convertView.setTag(goodInfo);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_drop_vip_function, null);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
        QAListInfo.GoodInfo goodInfo = dataInfos.get(position);
        tvTitle.setText("套餐（" + (position + 1) + "）：" + goodInfo.getTitle());
        convertView.setTag(goodInfo);
        return convertView;
    }
}
