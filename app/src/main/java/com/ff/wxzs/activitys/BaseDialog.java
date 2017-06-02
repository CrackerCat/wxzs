package com.ff.wxzs.activitys;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


import com.ff.wxzs.R;

import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/2/20.
 */

public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context){
        super(context, R.style.customDialog);
        View view = LayoutInflater.from(context).inflate(
                getLayoutID(), null);
        ButterKnife.bind(this, view);
        this.setContentView(view);
        this.setCancelable(true);
    }

    public abstract int getLayoutID();

}
