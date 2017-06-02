package com.ff.wxzs.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ff.wxzs.domain.AppModel;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.proto.AppSetting;

/**
 * Created by zhangkai on 2017/2/20.
 */

public class DameoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        String uid = uri.getQueryParameter("uid");
        String packageName = uri.getQueryParameter("pk");
        AppModel appModel = null;
        if (uid != null) {
            AppSetting appSetting = VirtualCore.get().findApp(packageName);
            if (appSetting != null) {
                appModel = new AppModel(this, appSetting);
            }
            LoadingActivity.launch(getBaseContext(), appModel, Integer.parseInt(uid));
        }
        finish();
    }
}
