package com.ff.wxzs.views.adpaters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.ff.wxzs.R;
import com.ff.wxzs.domain.AppModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/2/24.
 */

public class AppsListAdapter extends GeneralAdapter<AppModel> {

    public AppsListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_apps_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppModel appModel = dataInfos.get(i);
        holder.rlItem.setTag(appModel);
        holder.tvTitle.setText(appModel.name);
        holder.ivIcon.setImageDrawable(appModel.icon);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.icon)
        ImageView ivIcon;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            MaterialRippleLayout.on(rlItem)
                    .rippleColor(ContextCompat.getColor(context, R.color.primary))
                    .rippleAlpha(0.2f)
                    .rippleHover(true)
                    .rippleDuration(200)
                    .create();
        }

        @OnClick({R.id.item})
        public void onClick(View view) {
            onItemClickListener.onClick(view);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view);

        void onLongClick(View view);
    }
}
