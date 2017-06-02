package com.ff.wxzs.views.adpaters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ff.wxzs.R;
import com.ff.wxzs.domain.QAInfo;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class QAAdapter extends BaseExpandableListAdapter {
    public List<QAInfo> dataInfos;
    private Context context;
    protected LayoutInflater inflater;

    public QAAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return dataInfos != null ? dataInfos.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return dataInfos.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return dataInfos.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewGroupHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_qa, viewGroup, false);
            holder = new ViewGroupHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewGroupHolder) view.getTag();
        }
        QAInfo qaInfo = dataInfos.get(i);
        qaInfo.setQid(i);
        holder.rlItem.setTag(qaInfo);
        holder.tvQuestion.setText(Html.fromHtml((i + 1
        ) + "、" + qaInfo.getQuestion()));
        if (qaInfo.isSelected()) {
            holder.ivArrow.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_arrow_top));
            holder.tvQuestion.setFocusable(true);
            holder.tvQuestion.setEllipsize(TextUtils.TruncateAt.valueOf("MARQUEE"));
        } else {
            holder.ivArrow.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_arrow_bottom));
            holder.tvQuestion.setFocusable(false);
            holder.tvQuestion.setEllipsize(TextUtils.TruncateAt.END);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_qa_child, viewGroup, false);
            holder = new ChildHolder(view);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        QAInfo qaInfo = dataInfos.get(i);
        holder.tvAnwser.setText(Html.fromHtml(qaInfo.getAnswer()));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class ViewGroupHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.question)
        TextView tvQuestion;

        @BindView(R.id.arrow)
        ImageView ivArrow;

        public ViewGroupHolder(View view) {
            ButterKnife.bind(this, view);


        }

        @OnClick({R.id.item})
        public void onClick(View view) {
            onItemClickListener.onGroupClick(view);
        }
    }

    class ChildHolder {
        @BindView(R.id.anwser)
        TextView tvAnwser;

        public ChildHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onGroupClick(View view);
    }
}
