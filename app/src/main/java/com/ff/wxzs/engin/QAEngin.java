package com.ff.wxzs.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.QAListInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import rx.Observable;

/**
 * Created by zhangkai on 2017/2/20.
 */

public class QAEngin extends BaseEngin<ResultInfo<QAListInfo>> {
    public QAEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.QA_LIST_URL;
    }

    public Observable<ResultInfo<QAListInfo>> getQAList() {
        return rxpost(new TypeReference<ResultInfo<QAListInfo>>(){}.getType(), null, true, true, true);
    }
}
