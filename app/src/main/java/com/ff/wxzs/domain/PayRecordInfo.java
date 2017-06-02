package com.ff.wxzs.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/2/6.
 */

public class PayRecordInfo {
    public String order_sn = "";

    public String status = "";

    public String status_msg = "";

    @JSONField(name = "goods_title")
    public String desp = "";

    public String money = "";

    public String rmb_money = "";

    public String finish_time = "";

    @JSONField(name = "payway_title")
    public String pay_way_title = "";
}
