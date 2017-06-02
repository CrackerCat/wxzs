package com.ff.wxzs.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.PayRecordListInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;


import rx.Observable;

/**
 * Created by zhangkai on 2017/2/20.
 */

public class PayRecordEngin extends BaseEngin<ResultInfo<PayRecordListInfo>> {
    public PayRecordEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.ORDER_LIST_URL;
    }

    public Observable<ResultInfo<PayRecordListInfo>> getRecords() {
        return rxpost(new TypeReference<ResultInfo<PayRecordListInfo>>() {}.getType(), null, true, true, true);
    }
}
