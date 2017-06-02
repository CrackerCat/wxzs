package com.ff.wxzs.activitys;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ff.wxzs.R;
import com.ff.wxzs.domain.Config;
import com.kk.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class PayPopupWindow extends BasePopupWindow {
    @BindView(R.id.close)
    ImageView btnClose;

    @BindView(R.id.forver)
    TextView ckForver;

    @BindView(R.id.singer)
    TextView ckSigner;

    @BindView(R.id.every_forver)
    TextView ckEveryForver;


    private int viptype = Config.Vip_No;


    public PayPopupWindow(Activity context) {
        super(context);
        int width = ScreenUtil.getWidth(context);
        int diff = ScreenUtil.dip2px(context, 14);
        this.setWidth(width - diff);
        this.setOutsideTouchable(false);
        ckSigner.setText(Config.Price_Signer + ckSigner.getText().toString() + Config.Price_Signer_Desc);
        ckForver.setText(Html.fromHtml(Config.Price_Forver + ckForver.getText().toString() + Config.Price_Forver_Desc + "<font color=red>(超值)" +
                "</font>"));
        ckEveryForver.setText(Html.fromHtml(Config.Price_Every_Forver + ckEveryForver.getText().toString() + Config
                .Price_Every_Forver_Desc+ "<font" +
                " color=red>(推荐)" +
                "</font>"));
    }

    @Override
    public int getAnimationID() {
        return R.style.menu_anim;
    }

    @Override
    public int getLayoutID() {
        return R.layout.popup_window_pay;
    }

    @OnClick({R.id.close, R.id.forver, R.id.singer, R.id.every_forver})
    public void onClick(View view) {

        if (view.getId() == ckSigner.getId()) {
            dismiss();
            viptype = Config.Vip_Signer;
            view.setTag(viptype);
            onItemClickListener.onSigner(view); //单个分身
            return;
        }

        if (view.getId() == ckForver.getId()) {
            dismiss();
            viptype = Config.Vip_Forver;
            view.setTag(viptype);
            onItemClickListener.onForver(view); //无限分身
            return;
        }

        if (view.getId() == ckEveryForver.getId()) {
            dismiss();
            viptype = Config.Vip_EveryForver;
            view.setTag(viptype);
            onItemClickListener.onEveryForver(view); //所有应用无限分身
            return;
        }

        if (view.getId() == btnClose.getId()) {
            dismiss();
            return;
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onSigner(View view);

        void onForver(View view);

        void onEveryForver(View view);
    }
}
