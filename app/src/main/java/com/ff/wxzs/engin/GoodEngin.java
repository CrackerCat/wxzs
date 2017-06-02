package com.ff.wxzs.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.GoodListInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import rx.Observable;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class GoodEngin extends BaseEngin<ResultInfo<GoodListInfo>> {
    public GoodEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.VIP_LIST_URL;
    }

    public Observable<ResultInfo<GoodListInfo>> getGoodList() {
        return rxpost(new TypeReference<ResultInfo<GoodListInfo>>(){}.getType(), null, true, true, true);
    }

}
