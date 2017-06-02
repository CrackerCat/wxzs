package com.ff.wxzs.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kk.utils.LogUtil;

import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/2/16.
 */

public abstract  class BaseView extends LinearLayout {
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public void initViews(Context context) {
        try {
            inflate(context, getLayoutId(), this);
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg(this.getClass().getSimpleName()+"初始化失败->" + e);
        }
    }

    public abstract int getLayoutId();
}
