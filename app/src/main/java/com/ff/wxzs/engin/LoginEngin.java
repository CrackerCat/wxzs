package com.ff.wxzs.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.LoginDataInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;

import rx.Observable;

/**
 * Created by zhangkai on 2017/2/20.
 */

public class LoginEngin extends BaseEngin<ResultInfo<LoginDataInfo>> {
    public LoginEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.INIT_URL;
    }

    public Observable<ResultInfo<LoginDataInfo>> rxGetInfo(){
        return rxpost(new TypeReference<ResultInfo<LoginDataInfo>>(){}.getType(), null, true, true, true);
    }
}
