package com.ff.wxzs;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.TryInfo;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FPUitl;
import com.kk.utils.FileUtil;
import com.lody.virtual.client.core.VirtualCore;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhangkai on 16/9/22.
 */
public class WxzsApplication extends Application {

    public static final String Pfr_main = "main";

    @Override
    public void onCreate() {
        super.onCreate();

        //错误捕获
        if (Config.DEBUG) {
            CrashHandler.getInstance().initCrashHandler();
        }

        //友盟统计
        UMGameAgent.setDebugMode(Config.DEBUG);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //全局信息初始化
        GoagalInfo.get().init(getApplicationContext());

        //设置文件唯一性 防止手机相互拷贝
        FileUtil.setUuid(GoagalInfo.get().uuid);

        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sv", sv);
        params.put("device_type", "2");
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionName + "");
        }
        HttpConfig.setDefaultParams(params);

        //动态设置渠道信息
        String appId_agentId = Config.Appname + "多开-渠道id" + agent_id;
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getApplicationContext(),
                "58a4f8d0cae7e708ed00240c", appId_agentId));

        //分身试用
        if (FPUitl.get(getApplicationContext(),
                Pfr_main, "").isEmpty()) {
            TryInfo.setTryTime(getApplicationContext(), Config.tryHour);
            FPUitl.putString(getApplicationContext(), Pfr_main, "[main]\nfirstrun=true");
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
