package com.ff.wxzs.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.ff.wxzs.R;
import com.ff.wxzs.domain.LoginDataInfo;
import com.ff.wxzs.views.widgets.HelpItemView;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class HelpActivity extends BaseActivity {
    @BindView(R.id.qq)
    HelpItemView qqItem;

    @BindView(R.id.service)
    HelpItemView serviceItem;

    @BindView(R.id.points)
    HelpItemView pointsItem;

    @BindView(R.id.qa)
    HelpItemView qaItem;

    @BindView(R.id.version)
    TextView txtVersion;

    @Override
    public void initViews() {
        super.initViews();

        LoginDataInfo loginDataInfo = (LoginDataInfo)GoagalInfo.get().extra;

        if (loginDataInfo != null && loginDataInfo.getContactInfo() != null &&
                loginDataInfo.getContactInfo().getQq() != null) {
            qqItem.setQQ(loginDataInfo.getContactInfo().getQq());
        }

        qqItem.setQQOnClickListener(view -> {
            TextView textView = (TextView) view;
            if (checkQQInstalled(HelpActivity.this)) {
                String qq = textView.getText() + "";
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } else {
                ToastUtil.toast2(HelpActivity.this, "手机QQ未安装或该版本不支持");
            }
        });

        MaterialRippleLayout.on(qaItem)
                .rippleColor(ContextCompat.getColor(this, R.color.primary))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .rippleDuration(200)
                .create();

        RxView.clicks(qaItem).throttleFirst(2, TimeUnit.SECONDS).subscribe(v -> {
            startActivity(new Intent(HelpActivity.this, QAActivity.class));
        });

        txtVersion.setText("版本:" + GoagalInfo.get().packageInfo.versionName);
    }

    public boolean checkQQInstalled(Context context) {
        Uri uri = Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_help;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        if (title.equals(getResources().getString(R.string.back))) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_actions, menu);
        return true;
    }

}
