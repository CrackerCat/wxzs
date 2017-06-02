package com.ff.wxzs.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class QAListInfo {
    public List<QAInfo> getQaInfoList() {
        return qaInfoList;
    }

    public void setQaInfoList(List<QAInfo> qaInfoList) {
        this.qaInfoList = qaInfoList;
    }

    @JSONField(name = "qa_list")
    private List<QAInfo> qaInfoList;

    /**
     * Created by zhangkai on 2017/3/1.
     */

    public static class GoodInfo {
        public String getVipid() {
            return vipid;
        }

        public void setVipid(String vipid) {
            this.vipid = vipid;
        }

        private String vipid;
        private int id;
        private String add_id;
        private String title;
        private String price;
        private String real_price;
        private String desp;
        private String pay_name;

        public String getPay_name() {
            return pay_name;
        }

        public void setPay_name(String pay_name) {
            this.pay_name = pay_name;
        }

        public String getWxpayway() {
            if (wxpayway == null || wxpayway.isEmpty()) {
                wxpayway = "ipaynow";
            }
            return wxpayway;
        }

        public void setWxpayway(String wxpayway) {
            this.wxpayway = wxpayway;
        }

        public String getAlipayway() {
            if (alipayway == null || alipayway.isEmpty()) {
                alipayway = "alipay";
            }
            return alipayway;
        }

        public void setAlipayway(String alipayway) {

            this.alipayway = alipayway;
        }

        private String wxpayway;
        private String alipayway;

        private String alias;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdd_id() {
            return add_id;
        }

        public void setAdd_id(String add_id) {
            this.add_id = add_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReal_price() {
            return real_price;
        }

        public void setReal_price(String real_price) {
            this.real_price = real_price;
        }

        public String getDesp() {
            return desp;
        }

        public void setDesp(String desp) {
            this.desp = desp;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

    }
}
