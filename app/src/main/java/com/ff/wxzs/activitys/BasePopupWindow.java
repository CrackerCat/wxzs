package com.ff.wxzs.activitys;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/2/17.
 */

public abstract class BasePopupWindow extends PopupWindow {
    private Activity mContext;
    private ColorDrawable mBackgroundDrawable;


    public BasePopupWindow(Activity context){
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contextView = inflater.inflate(getLayoutID(), null);
        this.setContentView(contextView);
        ButterKnife.bind(this, contextView);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        int aid = getAnimationID();
        if(aid != 0) {
            this.setAnimationStyle(aid);
        }

        Window window = context.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        context.getWindow().setAttributes(lp);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
    }

    public abstract int getAnimationID();

    public abstract int getLayoutID();

    @Override
    public void setContentView(View contentView) {
        if(contentView != null) {
            super.setContentView(contentView);
            setFocusable(true);
            setTouchable(true);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            dismiss();
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if(touchable) {
            if(mBackgroundDrawable == null) {
                mBackgroundDrawable = new ColorDrawable(0x00000000);
            }
            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }



}
