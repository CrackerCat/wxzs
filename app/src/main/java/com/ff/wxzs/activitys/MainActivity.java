package com.ff.wxzs.activitys;


import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ff.wxzs.R;
import com.ff.wxzs.domain.AppModel;
import com.ff.wxzs.domain.AppRepository;
import com.ff.wxzs.domain.Config;
import com.ff.wxzs.domain.QAListInfo;
import com.ff.wxzs.domain.GoodListInfo;
import com.ff.wxzs.domain.LoginDataInfo;
import com.ff.wxzs.domain.PayWayInfo;
import com.ff.wxzs.domain.PaywayListInfo;
import com.ff.wxzs.domain.ShareInfo;
import com.ff.wxzs.domain.TryInfo;
import com.ff.wxzs.domain.UserInfo;
import com.ff.wxzs.domain.StatusInfo;
import com.ff.wxzs.domain.VipInfo;
import com.ff.wxzs.engin.CheckEngin;
import com.ff.wxzs.engin.LoginEngin;
import com.ff.wxzs.engin.GoodEngin;
import com.ff.wxzs.engin.PaywayListEngin;
import com.ff.wxzs.utils.SMSUtil;
import com.ff.wxzs.utils.ShareUtil;
import com.ff.wxzs.utils.TipUtil;
import com.ff.wxzs.utils.UpdateUtil;
import com.ff.wxzs.utils.VUiKit;
import com.ff.wxzs.views.adpaters.MainAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.fileupload.HttpBigFileUpload;
import com.kk.fileupload.SocketBigFileUpload;
import com.kk.loading.LoadingDialog;
import com.kk.pay.I1PayAbs;
import com.kk.pay.INowPayImpl;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.IPayImpl;
import com.kk.pay.IWXH5PayImpl;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FPUitl;
import com.kk.utils.LogUtil;
import com.kk.utils.TaskUtil;
import com.kk.utils.TimeUtil;
import com.kk.utils.ToastUtil;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.melnykov.fab.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


import static com.ta.utdid2.android.utils.SystemUtils.getSystemVersion;


/**
 * Created by zhangkai on 2017/2/16.
 */


public class MainActivity extends BaseActivity {
    @BindView(R.id.list)
    ListView wxlistView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avLoadingIndicatorView;

    private LoadingDialog loadingDialog;

    private MainAdapter mainAdapter;

    private int shareAddHour = 6;
    private final Handler handler = new Handler();
    private float tryHour = Config.tryHour;
    private TryInfo tryInfo;

    public static final String Prf_tryHour = "tryHour";
    public static final String Prf_addHourByShare = "addHourByShare";
    public static final String Prf_usernicknames = "usernicknames";
    public static final String Prf_userInfos = "userInfos";
    public static final String Prf_firstCreateShort = "firstCreateShort";
    public static final String Prf_VipInfo = "VipInfo";
    public static final String Prf_firstOpenfs = "firstOpenfs";
    public static final String Prf_firstPay = "firstPay";

    public String packageName = Config.packageName;

    private AppModel appModel = null;
    private AppRepository appRepository;

    private LoginEngin loginEngin;
    private GoodEngin goodEngin;
    private PaywayListEngin paywayListEngin;
    private CheckEngin checkEngin;

    private LoginDataInfo loginDataInfo;
    private List<QAListInfo.GoodInfo> goodInfos;

    private boolean isEveryForver = false;

    private IPayAbs iPayAbs;

    @Override
    public void initVars() {
        super.initVars();



        iPayAbs = new I1PayAbs(this);

        Intent intent = getIntent();
        AppModel tempAppModel = intent.getParcelableExtra(Config.App_Model);
        if (tempAppModel != null) {
            appModel = tempAppModel;
            packageName = tempAppModel.packageName;
            getSupportActionBar().setTitle(tempAppModel.name + "多开");
            isEveryForver = true;
        }

        if (!isEveryForver) {
            VipInfo vipInfo = getVipInfoWithFP();
            startApps(vipInfo);
        }

        appRepository = new AppRepository(this);
        mainAdapter = new MainAdapter(this);
        loadingDialog = new LoadingDialog(this);
        loginEngin = new LoginEngin(this);
        goodEngin = new GoodEngin(this);
        checkEngin = new CheckEngin(this);
        paywayListEngin = new PaywayListEngin(this);

        tryHour = getTryHour();

        SMSUtil.send(getApplicationContext());


    }


    @Override
    public void initViews() {
        super.initViews();
        fab.hide();
        fab.attachToListView(wxlistView);
        wxlistView.setAdapter(mainAdapter);
        mainAdapter.listView = wxlistView;
        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                VipInfo vipInfo = getVipInfo();
                if (vipInfo.getType() == Config.Vip_STOP) {
                    stop();
                    return;
                }

                UserInfo userInfo = (UserInfo) view.getTag();
                if (userInfo.uid == 0 && !userInfo.isCanTry()) {
                    TipUtil.tip(MainActivity.this, Html.fromHtml("<font color=red>试用分身已过期</font><font " +
                            "color=#000>！</font>"));
                    return;
                }

                TipUtil.firstTip(MainActivity.this, Prf_firstOpenfs, "[openfs]\n" +
                        "firstOpenfs=true", Html.fromHtml("<font color=red>1.点击子项打开分身</font><br/>&nbsp;&nbsp;" +
                        "每个子项分身最多同时只能登录一个帐号<br/>&nbsp;&nbsp;" +
                        "试用分身到期后将不能使用<br/><font color=red>2.长按子项弹出菜单</font><br/>&nbsp;&nbsp;" +
                        "修改分身名称，可自定义分身名称<br/>&nbsp;&nbsp;" +
                        "添加到桌面，可快速打开分身<br/>&nbsp;&nbsp;删除该分身，可清除数据，点击右下角+号重新创建分身"), !isEveryForver, () -> {
                    LoadingActivity.launch(getBaseContext(), appModel, userInfo.uid);
                }).subscribe(flag -> {
                    if (!flag) {
                        LoadingActivity.launch(getBaseContext(), appModel, userInfo.uid);
                    }
                });
            }

            @Override
            public void onLongClick(View view) {
                final UserInfo userInfo = (UserInfo) view.getTag();
                if (userInfo.uid == 0) {
                    TipUtil.tip(MainActivity.this, Html.fromHtml("<font color=red>试用分身不可修改</font><font color=#000>！</font>"));
                    return;
                }
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                long[] pattern = {0, 1};
                vib.vibrate(pattern, -1);
                MenuDialog menuDialog = new MenuDialog(MainActivity.this);
                menuDialog.setOnItemClickListener(new MenuDialog.OnItemClickListener() {
                    @Override
                    public void onDel(View view) {
                        delUser(userInfo);
                    }

                    @Override
                    public void onShort(View view) {
                        TipUtil.firstTip(MainActivity.this, Prf_firstCreateShort, "[short]\n" +
                                "firstcreate=true", "已经尝试发送到桌面，如果在桌面未能找到该分身图标，请检查系统设置中是否开启了\"发送到桌面\"功能");
                        createShortcut(MainActivity.this, userInfo, packageName);
                    }

                    @Override
                    public void onModify(View view) {
                        modifyUserInfo(userInfo);
                    }
                });
                menuDialog.show();
            }
        });


        RxView.clicks(fab).throttleFirst(2, TimeUnit.SECONDS).subscribe(view -> {
            VipInfo vipInfo = getVipInfo();
            if (vipInfo != null && (vipInfo.getType() == Config
                    .Vip_EveryForver)) {
                addUser(1);
                return;
            }

            if (vipInfo != null && vipInfo.getType() == Config.Vip_Signer && vipInfo.getCount() >= mainAdapter.getCount
                    ()) {
                addUser(0);
                return;
            }

            loadingDialog.show("请稍后...");
            goodEngin.getGoodList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<GoodListInfo>>() {
                @Override
                public void call(ResultInfo<GoodListInfo> resultInfo) {
                    loadingDialog.dismiss();
                    if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                        if (resultInfo.data != null && resultInfo.data.getGoodInfoList() != null) {
                            goodInfos = resultInfo.data.getGoodInfoList();
                            int i = 1;
                            for (QAListInfo.GoodInfo goodInfo : resultInfo.data.getGoodInfoList()) {
                                goodInfo.setVipid((i++) + "");
                                setGoodInfo(goodInfo);
                            }
                        }
                    }
                    pay(vipInfo);
                }
            });

        });
    }


    @Override
    public void loadData() {
        super.loadData();

        //设置商品信息
        goodEngin.getGoodList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<GoodListInfo>>() {
            @Override
            public void call(ResultInfo<GoodListInfo> resultInfo) {
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                    if (resultInfo.data != null && resultInfo.data.getGoodInfoList() != null) {
                        goodInfos = resultInfo.data.getGoodInfoList();
                        int i = 1;
                        for (QAListInfo.GoodInfo goodInfo : resultInfo.data.getGoodInfoList()) {
                            goodInfo.setVipid((i++) + "");
                            setGoodInfo(goodInfo);
                        }
                    }
                }
                getInfo();
            }
        });


    }

    private void getInfo() {
        loginEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResultInfo<LoginDataInfo>>() {
                    @Override
                    public void call(ResultInfo<LoginDataInfo> resultInfo) {
                        if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                            loginDataInfo = resultInfo.data;
                            GoagalInfo.get().extra = loginDataInfo;
                            preInit();
                        }
                        init();
                    }
                });
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        if (title.equals(getResources().getString(R.string.main_service))) {
            startActivity(new Intent(MainActivity.this, HelpActivity.class));
        } else if (title.equals(getResources().getString(R.string.main_bill_list))) {
            startActivity(new Intent(MainActivity.this, PayRecordActivity.class));
        } else if (title.equals(getResources().getString(R.string.share))) {
            SharePopupWindow sharePopupWindow = new SharePopupWindow(MainActivity.this);
            sharePopupWindow.setDesc(shareAddHour + "");
            sharePopupWindow.showAtLocation(wxlistView, Gravity.BOTTOM, 0, 0);
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
        } else if (title.equals(getResources().getString(R.string.back))) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEveryForver) {
            getMenuInflater().inflate(R.menu.back_actions, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        }
        return true;
    }

    //< 禁止使用
    private void stop() {
        TipUtil.tip(this, Html.fromHtml("禁止使用<font color=#000>！</font>"), () -> {
            finish();
        });
    }

    //< 预初始化信息
    private void preInit() {
        this.runOnUiThread(() -> {
            UpdateUtil.update(MainActivity.this, loginDataInfo);
        });

        if (loginDataInfo != null) {
            StatusInfo statusInfo = loginDataInfo.getStatusInfo();
            List<VipInfo> vipInfos = loginDataInfo.getVipInfoList();
            if (statusInfo != null) {
                if (statusInfo.getStatus() == 1) {
                    FPUitl.putString(MainActivity.this, Prf_VipInfo, Config.Vip_STOP + "-" + 1);
                    this.runOnUiThread(() -> {
                        stop();
                    });
                    return;
                }
            }

            if (vipInfos != null) {
                boolean isVip_Forver = false;
                for (VipInfo vipInfo : vipInfos) {
                    int vip = vipInfo.getType();
                    if (vipInfo.getType() == Config.Vip_EveryForver) {
                        FPUitl.putString(MainActivity.this, Prf_VipInfo, vip + "-" + 1);
                        break;
                    }
                    if (vipInfo.getType() == Config.Vip_Forver) {
                        FPUitl.putString(MainActivity.this, Prf_VipInfo, vip + "-" + 1);
                        isVip_Forver = true;
                        continue;
                    }
                    if (vipInfo.getType() == Config.Vip_Signer && !isVip_Forver) {
                        FPUitl.putString(MainActivity.this, Prf_VipInfo, vip + "-" + vipInfo.getCount());
                        continue;
                    }
                }
                VipInfo vipInfo = getVipInfo();
                startApps(vipInfo);
            }

            ShareInfo shareInfo = loginDataInfo.getShareInfo();
            if (shareInfo != null) {
                shareAddHour = shareInfo.getShare_add_hour();
            }
        }
    }


    //< 安装应用到虚拟空间 并初始化分身列表
    private void init() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                appModel = new AppModel(this, packageInfo);
                appModel.fastOpen = true;
                appRepository.addVirtualApp(appModel);
                synchronizesUserInfo();
                updateListView();
                doInstall();
            } else {
                doNoInstall();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            doNoInstall();
        }
    }

    //< vip补丁
    private boolean vip_fix(String uuid) {
        if (GoagalInfo.get().uuid.equals(uuid)) {
            FPUitl.putString(MainActivity.this, Prf_VipInfo, Config.Vip_EveryForver + "-" + 1);
            return true;
        } else {
            FPUitl.putString(MainActivity.this, Prf_VipInfo, Config.Vip_STOP + "-" + 1);
            stop();
            return false;
        }
    }

    //< 支付
    private void pay(final String type, final List<QAListInfo.GoodInfo> goodInfos) {
        paywayListEngin.getPaywayList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<PaywayListInfo>>() {
            @Override
            public void call(ResultInfo<PaywayListInfo> resultInfo) {
                if (resultInfo != null) {
                    real_pay(type, goodInfos, resultInfo.data);
                }
            }
        });
    }

    private void real_pay(final String type, final List<QAListInfo.GoodInfo> goodInfos, final PaywayListInfo paywayListInfos) {
        final PayDialog payDialog = new PayDialog(MainActivity.this);
        if (paywayListInfos != null && paywayListInfos.getPayWayInfos() != null) {
            boolean hasWxPay = false;
            boolean hasAliPay = false;
            String alipayWay = "";
            String wxpayWay = "";
            for (PayWayInfo payWayInfo : paywayListInfos.getPayWayInfos()) {
                if (payWayInfo != null && payWayInfo.getName() != null) {
                    String payway = payWayInfo.getName();
                    String title = payWayInfo.getTitle();
                    if ((payway.contains("wxpay") || title.contains("微信")) && !hasWxPay) {
                        hasWxPay = true;
                        wxpayWay = payway;
                    } else if ((payway.contains("alipay") || title.contains("支付宝")) && !hasAliPay) {
                        hasAliPay = true;
                        alipayWay = payway;
                    }
                }
            }

            for (QAListInfo.GoodInfo goodInfo : goodInfos) {
                goodInfo.setAlipayway(alipayWay);
                goodInfo.setWxpayway(wxpayWay);
            }

            if (!hasAliPay) {
                payDialog.hideAlipay();
            }

            if (!hasWxPay) {
                payDialog.hideWxpay();
            }
        }

        payDialog.show();
        payDialog.setDataSource(goodInfos);
        payDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                QAListInfo.GoodInfo goodInfo = goodInfos.get(i);
                payDialog.setGoodInfo(goodInfo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //< 支付回调
        final IPayCallback callback = new IPayCallback() {
            @Override
            public void onSuccess(OrderInfo orderInfo) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        synOrder(orderInfo);
                        payDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(OrderInfo orderInfo) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toast2(MainActivity.this, orderInfo.getMessage());
                    }
                });
            }
        };

        payDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QAListInfo.GoodInfo goodInfo = payDialog.getGoodInfo();
                if (goodInfo == null) return;

                int viptype = goodInfo.getId();
                float price = Float.parseFloat(goodInfo.getReal_price());
                String name = goodInfo.getAlias();

                OrderParamsInfo orderParamsInfo = new OrderParamsInfo(Config.PAY_URL, viptype + "", type, price, name);

                if (payDialog.getPayType() == PayDialog.ALPAY_TYPE) {
                    orderParamsInfo.setPayway_name(goodInfo.getAlipayway());
                } else if (payDialog.getPayType() == PayDialog.WXPAY_TYPE) {
                    orderParamsInfo.setPayway_name(goodInfo.getWxpayway());
                }

                iPayAbs.pay(orderParamsInfo, callback);

            }
        });
    }

    //< 打开支付窗口
    private void pay(final VipInfo vipInfo) {
        if (vipInfo != null && (vipInfo.getType() == Config
                .Vip_Forver)) {
            Menu2Dialog menu2Dialog = new Menu2Dialog(MainActivity.this);
            menu2Dialog.setOnItemClickListener(new Menu2Dialog.OnItemClickListener() {
                @Override
                public void onCreate(View view) {
                    addUser(1);
                }

                @Override
                public void onUpdate(View view) {
                    QAListInfo.GoodInfo goodInfo = new QAListInfo.GoodInfo();
                    goodInfo.setId(Config.Vip_EveryForver);
                    goodInfo.setReal_price((Config.Price_Update_Forver) + "");
                    goodInfo.setPrice((Config.Price_Update_Forver) + "");
                    goodInfo.setAlias(Config.Price_Update_Forver_Desc_Alias);
                    goodInfo.setTitle(Config.Price_Update_Forver_Desc);
                    List<QAListInfo.GoodInfo> updateGoodsInfo = new ArrayList<QAListInfo.GoodInfo>();
                    updateGoodsInfo.add(goodInfo);
                    pay(Config.Good_Up, updateGoodsInfo);
                }
            });
            menu2Dialog.show();
            return;
        }

        TipUtil.firstTip(MainActivity.this, Prf_firstPay, "[pay]\nfirstPay=true", Html.fromHtml("<font color=red>1.购买单个分身" + Config.Price_Signer + "元</font><br/>&nbsp;&nbsp;" +
                "付款成功后只能创建一个分身<br/><font" +
                " " +
                "color=red>2.购买无限分身" + Config.Price_Forver + "元</font><br/>&nbsp;&nbsp;付款成功后可以无限制创建分身<br/><font " +
                "color=red>3" +
                ".购买所有应用无限分身" + Config.Price_Every_Forver +
                "元</font><br/>&nbsp;&nbsp;不仅只有" + Config.Appname + "，还可以多开本机上安装的包括游戏、QQ等所有应用<br/><font " +
                "color=red>❋" + Config.Price_Every_Forver + "元开启应用多开助手的功能,您值得拥有❋</font><br/><font" +
                " " +
                "color=red>❋每个创建的分身都是永久使用的❋</font>"), !isEveryForver, () -> {
            openPay();
        }).subscribe(flag -> {
            if (!flag) {
                openPay();
            }
        });
    }

    //< 支付信息同步
    private void synOrder(OrderInfo orderInfo) {
        int viptype = orderInfo.getViptype();
        if (viptype == Config.Vip_Forver || viptype == Config.Vip_EveryForver) {
            FPUitl.putString(MainActivity.this, Prf_VipInfo, viptype + "-" + 1);
        } else {
            VipInfo statusInfo = getVipInfo();
            int count = 1;
            if (statusInfo != null && statusInfo.getType() != Config.Vip_No) {
                count = statusInfo.getCount() + 1;
            }
            FPUitl.putString(MainActivity.this, Prf_VipInfo, Config.Vip_Signer + "-" + count);
        }
        if (viptype == Config.Vip_EveryForver) {
            startActivity(new Intent(MainActivity.this, AppsListActivity.class));
            finish();
        } else {
            addUser(0);
        }
        checkOrder(orderInfo);
    }


    //< 设置商品信息
    private void setGoodInfo(QAListInfo.GoodInfo goodInfo) {
        if (goodInfo.getVipid().equals(Config.Vip_Signer + "")) {
            Config.Vip_Signer = goodInfo.getId();
            Config.Price_Signer = Float.parseFloat(goodInfo.getReal_price());
            Config.Price_Signer_Desc = goodInfo.getTitle();
            Config.Price_Signer_Desc_Alias = goodInfo.getAlias();
            return;
        }

        if (goodInfo.getVipid().equals(Config.Vip_Forver + "")) {
            Config.Vip_Forver = goodInfo.getId();
            Config.Price_Forver = Float.parseFloat(goodInfo.getReal_price());
            Config.Price_Forver_Desc_Alias = goodInfo.getAlias();
            return;
        }

        if (goodInfo.getVipid().equals(Config.Vip_EveryForver + "")) {
            Config.Vip_EveryForver = goodInfo.getId();
            Config.Price_Every_Forver = Float.parseFloat(goodInfo.getReal_price());

            Config.Price_Every_Forver_Desc = goodInfo.getTitle();
            Config.Price_Update_Forver_Desc = goodInfo.getTitle();

            Config.Price_Every_Forver_Desc_Alias = goodInfo.getAlias();
            Config.Price_Update_Forver_Desc_Alias = goodInfo.getAlias();
            return;
        }

    }

    //< 打开支付窗口
    private void openPay() {
        if (goodInfos == null || goodInfos.size() == 0) {
            goodInfos = new ArrayList<>();
            QAListInfo.GoodInfo goodInfo = new QAListInfo.GoodInfo();
            goodInfo.setId(Config.Vip_Signer);
            goodInfo.setPrice(Config.Price_Signer + "");
            goodInfo.setAlias(Config.Price_Signer_Desc_Alias);
            goodInfo.setTitle(Config.Price_Signer_Desc);

            QAListInfo.GoodInfo goodInfo2 = new QAListInfo.GoodInfo();
            goodInfo.setId(Config.Vip_Forver);
            goodInfo2.setPrice(Config.Price_Forver + "");
            goodInfo2.setAlias(Config.Price_Forver_Desc_Alias);
            goodInfo2.setTitle(Config.Price_Forver_Desc);

            QAListInfo.GoodInfo goodInfo3 = new QAListInfo.GoodInfo();
            goodInfo.setId(Config.Vip_EveryForver);
            goodInfo3.setPrice(Config.Price_Every_Forver + "");
            goodInfo3.setAlias(Config.Price_Every_Forver_Desc_Alias);
            goodInfo3.setTitle(Config.Price_Every_Forver_Desc);

            goodInfos.add(goodInfo);
            goodInfos.add(goodInfo2);
            goodInfos.add(goodInfo3);
        }

        pay(Config.Good_Gen, goodInfos);
    }


    //进入多开应用助手
    private void startApps(VipInfo vipInfo) {
        if (vipInfo != null && vipInfo.getType() == Config.Vip_EveryForver && !isEveryForver) {
            startActivity(new Intent(this, AppsListActivity.class));
            finish();
            return;
        }
    }


    //< 列表更新
    private void updateListView() {
        VUiKit.post(() -> {
            mainAdapter.appModel = appModel;
            mainAdapter.dataInfos = getUsers2();
            mainAdapter.notifyDataSetChanged();
        });
    }

    //< 分身安装时的操作
    private void doInstall() {
        this.runOnUiThread(() -> {
            avLoadingIndicatorView.hide();
            fab.show();

            VipInfo vipInfo = getVipInfo();
            if (vipInfo == null || (vipInfo.getType() != Config.Vip_Forver && vipInfo.getType() != Config
                    .Vip_EveryForver) || vipInfo.isRegister()) {
                startTry();
            }
        });
    }

    //< 同步获取会员信息
    private VipInfo getVipInfo() {
        VipInfo vipInfo = getVipInfoWithFP();
        if (vipInfo != null && loginDataInfo != null) {
            if (loginDataInfo.getStatusInfo() != null) {
                vipInfo.setRegister(loginDataInfo.getStatusInfo().isRegister());
            }
        }
        return vipInfo;
    }

    //< 分身未安装时的操作
    private void doNoInstall() {
        this.runOnUiThread(() -> {
            String appname = Config.Appname;
            if (appModel != null) {
                appname = appModel.name;
            }
            final String fappname = appname;
            ToastUtil.toast2(MainActivity.this, "请安装" + fappname);
            avLoadingIndicatorView.hide();
        });
    }

    //< 开始试用 ~ 定时器开始执行
    private void startTry() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                long t = canTry();
                if (t == 0) {
                    mainAdapter.updateView("免费分身已过期", false);
                    return;
                }
                String desc = getDescByTime(t);
                mainAdapter.updateView(desc, true);
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    //< 获取试用的时间描述
    private String getDescByTime(long t) {
        long hour = t / 1000 / 60 / 60;
        long minute = (t - (hour * 1000 * 60 * 60)) / (1000 * 60);
        long second = (t - (hour * 1000 * 60 * 60) - minute * (1000 * 60)) / (1000);
        return "试用时间还剩：" + hour + "时" + minute + "分" + second + "秒";
    }

    //< 是否还在试用期
    private long canTry() {
        if (tryInfo == null) {
            tryInfo = TryInfo.getTryTime(MainActivity.this);
        }
        long t = System.currentTimeMillis() - tryInfo.startTimeStamp;
        if (tryInfo.startTimeStamp == 0 || t <= 0) {
            return 0;
        }

        if (t > tryHour * 1000 * 60 * 60 - 1) {
            return 0;
        }

        return (long) (tryHour * 1000 * 60 * 60) - t;
    }

    //< 缓存分享加的时间并开始计时
    private void addHourByShare() {
        String h = FPUitl.get(this, Prf_addHourByShare, "");
        if (h.equals(TimeUtil.getTimeStr())) {
            return;
        }
        FPUitl.putString(this, Prf_addHourByShare, TimeUtil.getTimeStr());
        if (canTry() > 0) {
            tryHour += shareAddHour;
            FPUitl.putString(this, Prf_tryHour, tryHour + "");
        } else {
            tryHour = shareAddHour;
            TryInfo.setTryTime(MainActivity.this, tryHour);
            FPUitl.putString(this, Prf_tryHour, tryHour + "");
            if (tryInfo != null) {
                tryInfo = TryInfo.getTryTime(MainActivity.this);
            }
            startTry();
        }
    }

    //< 获取试用时间
    private float getTryHour() {
        return Float.parseFloat(FPUitl.get(this, Prf_tryHour, Config.tryHour + ""));
    }


    //< 同步用户
    private void synchronizesUserInfo() {
        String uidsStr = FPUitl.get(this, Prf_userInfos, "0");
        int count = Integer.parseInt(uidsStr);
        List<UserInfo> vapplist = getUsers();

        VipInfo vipInfo = getVipInfo();

        if (vipInfo != null && vipInfo.getCount() >
                count && count == 0) {
            count = vipInfo.getCount();
        }

        if (count > vapplist.size() && !isEveryForver) {
            for (int i = 0; i <= count - vapplist.size(); i++) {
                VUserManager.get().createUser(appModel.name + "分身" + (vapplist.size() + i), VUserInfo
                        .FLAG_ADMIN);
            }
        }
    }

    //< 获取分身用户
    private List<UserInfo> getUsers() {
        VipInfo vipInfo = getVipInfo();
        List<VUserInfo> userList = VUserManager.get().getUsers(false);
        List<UserInfo> users = new ArrayList<>();
        if (userList == null) {
            return users;
        }
        for (int i = 0; i < userList.size(); i++) {
            VUserInfo info = userList.get(i);
            UserInfo userInfo = new UserInfo();
            if (i == 0) {
                if (vipInfo != null && (vipInfo.getType() == Config.Vip_Forver || vipInfo.getType() == Config
                        .Vip_EveryForver)) {
                    continue;
                }
                long t = canTry();
                if (t == 0) {
                    userInfo.setCanTry(false);
                } else {
                    userInfo.setCanTry(true);
                    userInfo.setDesc(getDescByTime(t));
                }
            } else {
                if (!info.name.contains(appModel.name)) {
                    continue;
                }
                userInfo.setName(info.name);
                userInfo.setDesc("");
            }
            userInfo.uid = info.id;
            users.add(userInfo);
        }
        FPUitl.putString(this, Prf_userInfos, users.size() + "");
        return users;
    }

    //< 获取分身用户2
    private List<UserInfo> getUsers2() {
        String str = FPUitl.get(this, Prf_usernicknames, "");
        String[] strs = str.split(",");
        List<UserInfo> list = new ArrayList<>();
        List<UserInfo> vapplist = getUsers();

        if (strs.length != 0) {
            for (String _str : strs) {
                int idx = _str.indexOf('-');
                if (idx != -1 && idx < _str.length()) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.uid = Integer.parseInt(_str.substring(0, idx));
                    userInfo.setNickname(_str.substring(idx + 1, _str.length()));
                    list.add(userInfo);
                }
            }
        }

        for (UserInfo userInfo : list) {
            for (UserInfo userInfo2 : vapplist) {
                if (userInfo.uid == userInfo2.uid) {
                    userInfo2.setNickname(userInfo.getNickname());
                    break;
                }
            }
        }
        return vapplist;
    }

    //< 缓存分身用户
    private void setUserInfo() {
        if (mainAdapter.dataInfos == null || mainAdapter.dataInfos.size() < 0) {
            return;
        }
        String str = "";
        for (UserInfo userInfo2 : mainAdapter.dataInfos) {
            if (!userInfo2.getNickname().isEmpty())
                str += userInfo2.uid + "-" + userInfo2.getNickname() + ",";
        }
        FPUitl.putString(this, Prf_usernicknames, str);
    }

    //< 添加用户
    private void addUser(int step) {
        VUserManager.get().createUser(appModel.name + "分身" + (mainAdapter.getCount() + step), VUserInfo.FLAG_ADMIN);
        updateListView();
        ToastUtil.toast2(MainActivity.this, "创建分身成功");
    }

    //< 删除用户
    private void delUser(UserInfo userInfo) {
        boolean isDelete = VUserManager.get().removeUser(userInfo.uid);
        if (isDelete) {
            ToastUtil.toast2(MainActivity.this, "删除分身成功");
            updateListView();
            setUserInfo();
            return;
        }
        ToastUtil.toast2(MainActivity.this, "删除分身失败");
    }

    //< 修改用户名称
    private void modifyUserInfo(UserInfo userInfo) {
        new MaterialDialog.Builder(MainActivity.this)
                .title("分身名称修改：")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("请输入分身名称", userInfo.getShowName(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String nickname = input.toString().trim().replace(",", "");
                        if (nickname.isEmpty()) {
                            ToastUtil.toast2(MainActivity.this, "分身名称不能为空或包含,");
                            return;
                        }
                        userInfo.setNickname(nickname);
                        setUserInfo();
                        mainAdapter.notifyDataSetChanged();
                    }
                }).show();
    }

    //< 分享
    private void share(int type) {
        ShareInfo shareInfo = null;
        if (loginDataInfo != null) {
            shareInfo = loginDataInfo.getShareInfo();
        }
        ShareUtil.share(MainActivity.this, shareInfo.getDesc() + "" + shareInfo.getUrl(), type);
    }

    //< 订单检测
    private void checkOrder(OrderInfo orderInfo) {
        checkEngin.checkOrder(orderInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<String>>() {
            @Override
            public void call(ResultInfo<String> resultInfo) {
                if (resultInfo.code == HttpConfig.STATUS_OK) {
                    return;
                }
                checkOrder(orderInfo);
            }
        });
    }

    //< 获取vip信息 FP
    public VipInfo getVipInfoWithFP() {
        VipInfo vInfo = null;
        String vipInfo = FPUitl.get(MainActivity.this, Prf_VipInfo, "");
        if (vipInfo.isEmpty()) {
            FPUitl.putString(MainActivity.this, Prf_VipInfo, Config.Vip_No + "-" + 1);
            vipInfo = Config.Vip_No + "-" + 1;
        }
        if (!vipInfo.isEmpty()) {
            String[] vipInfos = vipInfo.split("-");
            if (vipInfos.length == 2) {
                vInfo = new VipInfo();
                vInfo.setType(Integer.parseInt(vipInfos[0]));
                vInfo.setCount(Integer.parseInt(vipInfos[1]));
            }
        }
        return vInfo;
    }

    //< 创建快捷方式
    public void createShortcut(final Activity context, final UserInfo userInfo, String packageName) {
        if (hasShortcut(context, userInfo)) {
            LogUtil.msg("快捷方式已存在");
            return;
        }
        LogUtil.msg("开始创建快捷方式");
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse("fywxzs://?uid=" + userInfo.uid + "&pk=" + packageName));
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            final Intent putShortCutIntent = new Intent();
            putShortCutIntent.putExtra("duplicate", false);
            putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, userInfo.getShowName());
            putShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, ((BitmapDrawable) appModel.icon).getBitmap());
            putShortCutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(putShortCutIntent);
            LogUtil.msg("创建快捷方式任务执行");
            ToastUtil.toast2(context, "已添加");
        } catch (Exception e) {
            LogUtil.msg("创建快捷方式失败->" + e);
        }
    }

    //< 根据title判断快捷方式是否存在
    private boolean hasShortcut(Context context, UserInfo userInfo) {
        String url;
        if (getSystemVersion() < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), new String[]{"title", "iconResource"},
                "title=?", new String[]{userInfo.getShowName()}, null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub
        if (data == null) {
            return;
        }

        if (IPayImpl.uiPayCallback != null && IPayImpl.uOrderInfo != null) {
            String msg = data.getExtras().getString("respMsg");
            String errorcode = data.getExtras().getString("errorCode");
            String respCode = data.getExtras().getString("respCode");
            if (respCode.equals("00")) {
                IPayImpl.uOrderInfo.setMessage("支付成功");
                IPayImpl.uiPayCallback.onSuccess(INowPayImpl.uOrderInfo);
            } else {
                IPayImpl.uOrderInfo.setMessage("支付失败");
                IPayImpl.uiPayCallback.onFailure(INowPayImpl.uOrderInfo);
            }
            IPayImpl.uOrderInfo = null;
            IPayImpl.uiPayCallback = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IPayImpl.uiPayCallback != null && IPayImpl.uOrderInfo != null && IWXH5PayImpl.isGen()) {
            IWXH5PayImpl.checkOrder(IPayImpl.uOrderInfo, IPayImpl.uiPayCallback);
        }
    }
}





