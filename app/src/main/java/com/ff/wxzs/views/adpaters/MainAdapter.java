package com.ff.wxzs.views.adpaters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ff.wxzs.R;
import com.ff.wxzs.activitys.MainActivity;
import com.ff.wxzs.domain.AppModel;
import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import com.balysv.materialripple.MaterialRippleLayout;
import com.ff.wxzs.domain.VipInfo;


/**
 * Created by zhangkai on 2017/2/16.
 */

public class MainAdapter extends GeneralAdapter<UserInfo> {

    public AppModel appModel;
    public ListView listView;
    private VipInfo vipInfo;

    public MainAdapter(Context context) {
        super(context);

    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_wxfs, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserInfo userInfo = dataInfos.get(i);
        setVipInfo();

        if (i == 0 && (vipInfo == null || (vipInfo.getType() != Config.Vip_Forver && vipInfo.getType() != Config
                .Vip_EveryForver))) {
            userInfo.setName("免费试用");
            if (userInfo.isCanTry()) {
                holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                userInfo.setDesc("免费分身已过期");
                holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
            }
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle2.setVisibility(View.GONE);
            holder.tvDesc.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
            holder.tvTitle2.setVisibility(View.VISIBLE);
            holder.tvDesc.setVisibility(View.GONE);
            holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        }

        holder.rlItem.setTag(userInfo);
        String name = userInfo.getNickname();
        if (name == null || name.equals("")) {
            name = userInfo.getName();
        }
        holder.tvTitle.setText(name);
        holder.tvTitle2.setText(name);
        holder.tvDesc.setText(userInfo.getDesc());
        holder.ivIcon.setImageDrawable(appModel.icon);
        return convertView;
    }

    public void updateView(String desc, boolean canTry) {
        if (dataInfos == null) {
            return;
        }
        int position = 0;

        int hcount = listView.getHeaderViewsCount();
        int fcount = listView.getFooterViewsCount();
        int fvisiblePos = listView.getFirstVisiblePosition() - hcount;
        int lvisiblePos = listView.getLastVisiblePosition() - fcount;

        View view = null;
        if (position >= fvisiblePos && position <= lvisiblePos) {
            view = (listView.getChildAt(position - fvisiblePos));
        }

        if (view == null) {
            return;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            return;
        }

        UserInfo userInfo = dataInfos.get(0);
        userInfo.setCanTry(canTry);
        userInfo.setDesc(desc);
        holder.tvDesc.setText(desc);
        if (userInfo.isCanTry()) {
            holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.gray_light));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        MainActivity activity = (MainActivity) context;
        vipInfo = activity.getVipInfoWithFP();
    }

    private void setVipInfo(){
        MainActivity activity = (MainActivity) context;
        if (vipInfo == null) {
            vipInfo = activity.getVipInfoWithFP();
        }
    }

    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.title2)
        TextView tvTitle2;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.desc)
        TextView tvDesc;

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

        @OnLongClick({R.id.item})
        public boolean onLongClick(View view) {
            onItemClickListener.onLongClick(view);
            return true;
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
