package com.ff.wxzs.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kk.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/2/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public void initVars() {
    }

    public void initViews() {
        try {
            setContentView(getLayoutID());
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg(this.getClass().getSimpleName() + " initViews->初始化失败");
        }
    }

    public void loadData() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
        initViews();
        loadData();
    }


    public abstract int getLayoutID();

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
