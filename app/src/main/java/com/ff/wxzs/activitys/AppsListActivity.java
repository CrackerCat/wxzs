package com.ff.wxzs.activitys;

import android.content.Intent;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.ff.wxzs.R;
import com.ff.wxzs.domain.AppModel;
import com.ff.wxzs.domain.AppRepository;
import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.LoginDataInfo;
import com.ff.wxzs.domain.ShareInfo;
import com.ff.wxzs.utils.ShareUtil;
import com.ff.wxzs.utils.TipUtil;
import com.ff.wxzs.views.adpaters.AppsListAdapter;
import com.kk.securityhttp.domain.GoagalInfo;

import org.jdeferred.DoneCallback;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/2/24.
 */

public class AppsListActivity extends BaseActivity {

    @BindView(R.id.gridview)
    GridView gridView;

    private AppsListAdapter appsListAdapter;
    private AppRepository appRepository;

    private LoginDataInfo loginDataInfo;

    private static final String Prf_firstOpenAppsFs = "firstOpenAppsFs";

    @Override
    public void initVars() {
        super.initVars();
        appsListAdapter = new AppsListAdapter(this);
        appRepository = new AppRepository(this);
        loginDataInfo = (LoginDataInfo) GoagalInfo.get().extra;
    }

    @Override
    public void initViews() {
        super.initViews();
        gridView.setAdapter(appsListAdapter);

        appRepository.getVirtualApps().done(new DoneCallback<List<AppModel>>() {
            @Override
            public void onDone(List<AppModel> result) {
                if (appsListAdapter.dataInfos != null) {
                    appsListAdapter.dataInfos.addAll(result);
                } else {
                    appsListAdapter.dataInfos = result;
                }
            }
        });

        appRepository.getInstalledApps(this).done(new DoneCallback<List<AppModel>>() {
            @Override
            public void onDone(List<AppModel> result) {
                if (appsListAdapter.dataInfos != null) {
                    appsListAdapter.dataInfos.addAll(result);
                    appsListAdapter.notifyDataSetChanged();
                } else {
                    appsListAdapter.dataInfos = result;
                }
            }
        });
        appsListAdapter.notifyDataSetChanged();

        appsListAdapter.setOnItemClickListener(new AppsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                AppModel appModel = (AppModel) view.getTag();
                try {
                    appRepository.addVirtualApp(appModel);
                    Intent intent = new Intent(AppsListActivity.this, MainActivity.class);
                    intent.putExtra(Config.App_Model, appModel);
                    startActivity(intent);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onLongClick(View view) {

            }
        });

        TipUtil.firstTip(this, Prf_firstOpenAppsFs, "[openfs]\n" +
                "firstOpenfs=true", Html.fromHtml("<font color=red>1.欢迎开启应用多开助手</font><br/>&nbsp;&nbsp;点击子项进入该应用的分身列表<br/>&nbsp;&nbsp;每个应用都可以创建多个分身"));
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_apps_list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        if (title.equals(getResources().getString(R.string.main_service))) {
            startActivity(new Intent(AppsListActivity.this, HelpActivity.class));
        } else if (title.equals(getResources().getString(R.string.main_bill_list))) {
            startActivity(new Intent(AppsListActivity.this, PayRecordActivity.class));
        } else if (title.equals(getResources().getString(R.string.share))) {
            SharePopupWindow sharePopupWindow = new SharePopupWindow(this);
            sharePopupWindow.showAtLocation(gridView, Gravity.BOTTOM, 0, 0);
            sharePopupWindow.setOnItemClickListener(new SharePopupWindow.OnItemClickListener() {
                @Override
                public void onCancel(View view) {
                    sharePopupWindow.dismiss();
                }

                @Override
                public void onFriend(View view) {
                    share(1);
                }

                @Override
                public void onQuan(View view) {
                    share(2);
                }
            });
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }


    //< 分享
    private void share(int type) {
        ShareInfo shareInfo = null;
        if (loginDataInfo != null) {
            shareInfo = loginDataInfo.getShareInfo();
        }
        ShareUtil.share(AppsListActivity.this, shareInfo.getDesc() + "" + shareInfo.getUrl(), type);
    }
}
