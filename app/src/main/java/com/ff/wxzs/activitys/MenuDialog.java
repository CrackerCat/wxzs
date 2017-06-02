package com.ff.wxzs.activitys;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ff.wxzs.R;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by zhangkai on 2017/2/20.
 */

public class MenuDialog extends BaseDialog {
    @BindView(R.id.modify)
    TextView btnModify;

    @BindView(R.id.mshort)
    TextView btnMshort;

    @BindView(R.id.del)
    TextView btnDel;

    public MenuDialog(Context context) {
        super(context);
    }

    @OnClick({R.id.modify, R.id.mshort, R.id.del})
    public void onClick(View view) {
        dismiss();
        if (view.getId() == btnModify.getId()) {
            onItemClickListener.onModify(view);
            return;
        }

        if (view.getId() == btnDel.getId()) {
            onItemClickListener.onDel(view);
            return;
        }

        if (view.getId() == btnMshort.getId()) {
            onItemClickListener.onShort(view);
            return;
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.dialog_menu;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onDel(View view);

        void onShort(View view);

        void onModify(View view);
    }
}
