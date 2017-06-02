package com.ff.wxzs.utils;

import android.content.ComponentName;
import android.content.Intent;

import com.ff.wxzs.activitys.BaseActivity;


/**
 * Created by zhangkai on 2017/3/1.
 */

public class ShareUtil {
    //< 分享
    public static void share(final BaseActivity ctx, String content, int type){

        String activityName = "com.tencent.mm.ui.tools.ShareImgUI";
        if (type == 2) {
            activityName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
        }

        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.tencent.mm", activityName));
        ctx.startActivity(intent);
    }

}
