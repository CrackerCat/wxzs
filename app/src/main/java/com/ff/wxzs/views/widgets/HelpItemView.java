package com.ff.wxzs.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ff.wxzs.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class HelpItemView extends BaseView {


    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.qqnumber)
    TextView tvQQ;


    public HelpItemView(Context context) {
        super(context);

    }

    public HelpItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.item);
        CharSequence title = a.getText(R.styleable.item_text);
        if(title != null){
            tvTitle.setText(title.toString());
            tvTitle.setTextColor(a.getColor(R.styleable.item_color, 0X333333));
        }
        Drawable iconSrc = a.getDrawable(R.styleable.item_src);
        if (iconSrc != null) {
            ivIcon.setImageDrawable(iconSrc);
        }
    }

    public void setQQOnClickListener(View.OnClickListener listener){
        tvQQ.setVisibility(VISIBLE);
        tvQQ.setOnClickListener(listener);
    }

    public void setQQ(String qq){
        tvQQ.setText(qq);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_help_item;
    }
}
