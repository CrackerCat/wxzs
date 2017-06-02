package com.ff.wxzs.activitys;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ff.wxzs.R;
import com.ff.wxzs.domain.PayRecordListInfo;
import com.ff.wxzs.engin.PayRecordEngin;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.ff.wxzs.views.adpaters.PayRecordAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class PayRecordActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listView;

    PayRecordAdapter payRecordAdapter;
    PayRecordEngin payRecordEngin;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    public void initVars() {
        super.initVars();
        payRecordAdapter = new PayRecordAdapter(this);
        payRecordEngin = new PayRecordEngin(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        listView.setAdapter(payRecordAdapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        payRecordEngin.getRecords().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<PayRecordListInfo>>() {
            @Override
            public void call(ResultInfo<PayRecordListInfo> resultInfo) {
                avLoadingIndicatorView.hide();
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                    if (resultInfo.data != null) {
                        payRecordAdapter.dataInfos = resultInfo.data.getRecordInfoList();
                        payRecordAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_pay_record;

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
