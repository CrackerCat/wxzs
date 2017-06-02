package com.ff.wxzs.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class GoodListInfo  {
    public List<QAListInfo.GoodInfo> getGoodInfoList() {
        return goodInfoList;
    }

    public void setGoodInfoList(List<QAListInfo.GoodInfo> goodInfoList) {
        this.goodInfoList = goodInfoList;
    }

    @JSONField(name = "vip_list")
    private List<QAListInfo.GoodInfo> goodInfoList;
}
