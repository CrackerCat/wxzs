package com.ff.wxzs.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class PayRecordListInfo {
    @JSONField(name = "orders_list")
    private List<PayRecordInfo> recordInfoList;

    public List<PayRecordInfo> getRecordInfoList() {
        return recordInfoList;
    }

    public void setRecordInfoList(List<PayRecordInfo> recordInfoList) {
        this.recordInfoList = recordInfoList;
    }

}
