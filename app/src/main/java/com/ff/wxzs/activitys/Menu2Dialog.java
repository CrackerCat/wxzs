package com.ff.wxzs.activitys;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ff.wxzs.R;
import com.ff.wxzs.domain.Config;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/2/24.
 */

public class Menu2Dialog extends BaseDialog {

    @BindView(R.id.create)
    TextView btnCreate;

    @BindView(R.id.update)
    TextView btnUpdate;

    public Menu2Dialog(Context context) {
        super(context);
        btnUpdate.setText(Html.fromHtml((Config.Price_Every_Forver - Config.Price_Forver) + btnUpdate.getText().toString() + Config
                .Price_Update_Forver_Desc + "<font " +
                "color=red>(推荐)</font>"));
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_menu2;
    }

    @OnClick({R.id.create, R.id.update})
    public void onClick(View view) {
        dismiss();
        if (view.getId() == btnCreate.getId()) {
            onItemClickListener.onCreate(view);
            return;
        }

        if (view.getId() == btnUpdate.getId()) {
            onItemClickListener.onUpdate(view);
            return;
        }

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onCreate(View view);

        void onUpdate(View view);
    }
}
