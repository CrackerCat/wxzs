package com.ff.wxzs.activitys;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ff.wxzs.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class SharePopupWindow extends BasePopupWindow {
    @BindView(R.id.weichat1)
    ImageView btnFriend;

    @BindView(R.id.weichat2)
    ImageView btnQuan;

    @BindView(R.id.cancel)
    TextView tvCancel;

    @BindView(R.id.desc)
    TextView tvDesc;

    public SharePopupWindow(Activity context) {
        super(context);
        this.setOutsideTouchable(true);
    }

    @Override
    public int getAnimationID() {
        return R.style.share_anim;
    }

    @Override
    public int getLayoutID() {
        return R.layout.popup_window_share;
    }

    @OnClick({R.id.weichat1, R.id.weichat2, R.id.cancel})
    public void onClick(View view) {
        dismiss();
        if (view.getId() == btnFriend.getId()) {
            onItemClickListener.onFriend(view);
            return;
        }

        if (view.getId() == btnQuan.getId()) {
            onItemClickListener.onQuan(view);
            return;
        }

        if (view.getId() == tvCancel.getId()) {
            onItemClickListener.onCancel(view);
            return;
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onCancel(View view);

        void onFriend(View view);

        void onQuan(View view);
    }

    public void setDesc(String hour) {
        tvDesc.setText(tvDesc.getText().toString().replace("6", hour));
    }
}
