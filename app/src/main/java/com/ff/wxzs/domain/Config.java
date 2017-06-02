package com.ff.wxzs.domain;

import android.os.Environment;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class Config {
    public static final String Good_Gen = "0";  //普通
    public static final String Good_Up = "2";   //升级

    public static  int Vip_No = 0;
    public static  int Vip_Signer = 1;
    public static  int Vip_Forver = 2;
    public static  int Vip_EveryForver = 3;
    public static  int Vip_STOP = -100;

    public static float Price_Signer = 8.80f;
    public static float Price_Forver = 18.80f;
    public static float Price_Every_Forver = 28.80f;
    public static float Price_Update_Forver = (Price_Every_Forver - Price_Forver);

    public static String Appname = "微信";
    public static String Price_Signer_Desc = "创建单个" + Appname + "分身";
    public static String Price_Forver_Desc = "创建开无限个" + Appname + "分身";
    public static String Price_Every_Forver_Desc = "任意应用无限制分身如:微信、QQ、任一应用游戏";
    public static String Price_Update_Forver_Desc = "任意应用无限制分身如:微信、QQ、任一应用游戏";

    public static String Price_Signer_Desc_Alias = "创建单个" + Appname + "分身";
    public static String Price_Forver_Desc_Alias = "创建开无限个" + Appname + "分身";
    public static String Price_Every_Forver_Desc_Alias = "任意应用无限制分身如:微信、QQ、任一应用游戏";
    public static String Price_Update_Forver_Desc_Alias = "任意应用无限制分身如:微信、QQ、任一应用游戏";

    public static final String App_Model = "appModel";

    public static String packageName = "com.tencent.mm";
    public static float tryHour = 0.5f;

    public static final boolean DEBUG = false;

    private static String baseUrl = "http://u.wk990.com/api/";

    private static String debugBaseUrl = "http://120.76.202.236:1980/api/";

    public static final String APPID = "?app_id=1";

    public static final String INIT_URL = getBaseUrl() + "index/init" + APPID;

    public static final String PAY_URL = getBaseUrl() + "index/pay"+ APPID;

    public static final String VIP_LIST_URL = getBaseUrl() + "index/vip_list"+ APPID;

    public static final String ORDER_LIST_URL = getBaseUrl() + "index/orders_list"+ APPID;

    public static final String QA_LIST_URL = getBaseUrl() + "index/qa_list"+ APPID;

    public static final String CHECK_URL = getBaseUrl() + "index/orders_check"+ APPID;

    public static final String PAY_WAY_LIST_URL = getBaseUrl() + "index/payway_list"+ APPID;

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }


    public static final String PATH = Environment.getExternalStorageDirectory() + "/ffwxzs";

}
