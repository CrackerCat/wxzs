package com.ff.wxzs.activitys;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ff.wxzs.R;
import com.ff.wxzs.domain.QAListInfo;
import com.ff.wxzs.views.adpaters.VipFunctionAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/3/20.
 */

public class PayDialog extends BaseDialog {

    @BindView(R.id.sp_vip_function)
    Spinner spinnerVf;

    @BindView(R.id.tv_vip_money)
    TextView tvMoney;

    @BindView(R.id.iv_alipay_selector)
    ImageView ivAlipay;

    @BindView(R.id.iv_wxpay_selector)
    ImageView ivWxpay;

    @BindView(R.id.btn_confirm_pay)
    Button btnPay;

    @BindView(R.id.rl_wxpay)
    RelativeLayout rlWxpay;

    @BindView(R.id.rl_alipay)
    RelativeLayout rlAlipay;

    @BindView(R.id.iv_close)
    ImageView ivClose;

    @BindView(R.id.alline)
    View alline;

    @BindView(R.id.wxline)
    View wxline;

    private QAListInfo.GoodInfo goodInfo;

    public QAListInfo.GoodInfo getGoodInfo() {
        return goodInfo;
    }

    public void setGoodInfo(QAListInfo.GoodInfo goodInfo) {
        this.goodInfo = goodInfo;
        setPrice(goodInfo.getReal_price());
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    private int payType = ALPAY_TYPE;

    public static final int ALPAY_TYPE = 1;
    public static final int WXPAY_TYPE = 2;


    VipFunctionAdapter spinnerAdapter;

    public PayDialog(Context context) {
        super(context);
        spinnerAdapter = new VipFunctionAdapter(context);
        spinnerVf.setAdapter(spinnerAdapter);

        this.setCanceledOnTouchOutside(false);
    }

    public void setDataSource(List<QAListInfo.GoodInfo> goodInfos) {
        if(goodInfos != null && goodInfos.size() > 0){
            setGoodInfo(goodInfos.get(0));
            if(goodInfos.size() > 1){
                spinnerVf.setSelection(1);
                setGoodInfo(goodInfos.get(1));
            }
        }
        spinnerAdapter.dataInfos = goodInfos;
        spinnerAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rl_alipay, R.id.rl_wxpay, R.id.iv_close})
    public void onSelectorClick(View view) {
        if (view.getId() == rlAlipay.getId()) {
            if (payType == ALPAY_TYPE) {
                return;
            }
            payType = ALPAY_TYPE;
            ivWxpay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.pay_select_normal));
            ivAlipay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.pay_select_press));
        } else if (view.getId() == rlWxpay.getId()) {
            if (payType == WXPAY_TYPE) {
                return;
            }
            payType = WXPAY_TYPE;
            ivWxpay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.pay_select_press));
            ivAlipay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.pay_select_normal));
        }

        if(ivClose.getId() == view.getId()){
            dismiss();
            return;
        }
    }

    public void hideWxpay(){
        rlWxpay.setVisibility(View.GONE);
        wxline.setVisibility(View.GONE);
        payType = ALPAY_TYPE;
        ivAlipay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.pay_select_press));
    }

    public void hideAlipay(){
        rlAlipay.setVisibility(View.GONE);
        alline.setVisibility(View.GONE);
        payType = WXPAY_TYPE;
        ivWxpay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.pay_select_press));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        btnPay.setOnClickListener(onClickListener);
    }

    public void setPrice(String price){
        tvMoney.setText(price);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        spinnerVf.setOnItemSelectedListener(onItemSelectedListener);
    }


    @Override
    public int getLayoutID() {
        return R.layout.dialog_open_vip;
    }
}
