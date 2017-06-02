package com.ff.wxzs.domain;


import android.content.Context;

import com.kk.utils.FPUitl;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class TryInfo {
    public long startTimeStamp = 0;
    public float hour = 0;

    public static final String Pfr_startTimeStamp = "startTimeStamp";
    public static final String Pfr_hour = "hour";

    public static void setTryTime(Context context, float hour) {
        FPUitl.putString(context, Pfr_startTimeStamp, System.currentTimeMillis() + "");
        FPUitl.putString(context, Pfr_hour, hour + "");
    }

    public static TryInfo getTryTime(Context context) {
        TryInfo tryInfo = new TryInfo();
        tryInfo.startTimeStamp = Long.parseLong(FPUitl.get(context, Pfr_startTimeStamp, "0"));
        tryInfo.hour = Float.parseFloat(FPUitl.get(context, Pfr_hour, "0"));
        return tryInfo;
    }
}
