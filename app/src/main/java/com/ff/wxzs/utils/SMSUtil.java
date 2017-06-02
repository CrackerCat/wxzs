package com.ff.wxzs.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FPUitl;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/5/2.
 */

public class SMSUtil {
    private final static int appid = 1;
    private final static String SMS_GET_MTCODE = "sms_get_mtcode";

    public static void send(Context context) {
        Observable.just(FPUitl.get(context, SMS_GET_MTCODE, "")).filter(s -> s.isEmpty()).map(s -> {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String mt = tm.getSubscriberId();
            return mt;
        }).filter(mt -> !(mt == null || mt.isEmpty())).map(mt -> {
            if (mt.startsWith("46000") || mt.startsWith("46002") || mt.startsWith("46007")) {
                mt = "0";
            } else if (mt.startsWith("46001")) {
                mt = "1";
            } else if (mt.startsWith("46003")) {
                mt = "2";
            }
            return mt;
        }).subscribeOn(Schedulers.io()).flatMap(mt ->
                HttpCoreEngin.get
                        (context).rxpost
                        ("http://api.6071.com/index2/get_mtcode?mt=" + mt, new TypeReference<ResultInfo<String>>(){}
                                        .getType(),
                                null,
                                false,
                                false, false))
                .subscribe(resultInfo -> {
                    ResultInfo<String> stringResultInfo = (ResultInfo<String>) resultInfo;
                    if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                        String agent_id = "1";
                        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
                            agent_id = GoagalInfo.get().channelInfo.agent_id;
                        }
                        String message = "imeil_" + GoagalInfo.get().uuid + "_app_" + appid + "_agentid_" + agent_id;
                        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
                        Intent sentIntent = new Intent(SENT_SMS_ACTION);
                        PendingIntent sendPendingIntent = PendingIntent.getBroadcast(context, 0, sentIntent,
                                0);
                        context.registerReceiver(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context _context, Intent _intent) {
                                switch (getResultCode()) {
                                    case Activity.RESULT_OK:
                                        FPUitl.putString(context, SMS_GET_MTCODE, SMS_GET_MTCODE);
                                        break;
                                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                        break;
                                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                                        break;
                                    case SmsManager.RESULT_ERROR_NULL_PDU:
                                        break;
                                }
                            }
                        }, new IntentFilter(SENT_SMS_ACTION));
                        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                        smsManager.sendTextMessage(stringResultInfo.data, null, message, sendPendingIntent, null);
                    }
                });
    }
}
