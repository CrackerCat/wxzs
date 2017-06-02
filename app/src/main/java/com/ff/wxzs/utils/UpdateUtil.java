package com.ff.wxzs.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ff.wxzs.R;
import com.ff.wxzs.domain.LoginDataInfo;
import com.ff.wxzs.domain.UpdateInfo;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.utils.PathUtil;
import com.kk.utils.security.Md5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/4/10.
 */

public class UpdateUtil {
    //< 软件更新
    public static void update(Context context, LoginDataInfo loginDataInfo) {
        if (loginDataInfo != null && loginDataInfo.getUpdateInfo() != null) {
            UpdateInfo updateInfo = loginDataInfo.getUpdateInfo();

            Observable.just(updateInfo).filter(new Func1<UpdateInfo, Boolean>() {
                @Override
                public Boolean call(UpdateInfo updateInfo) {
                    return updateInfo.getVersion() != null && !updateInfo.getVersion().equals(GoagalInfo.get().packageInfo
                            .versionName);
                }
            }).map(new Func1<UpdateInfo, File>() {
                @Override
                public File call(UpdateInfo updateInfo) {
                    String url = updateInfo.getAppUrl();
                    if (url == null || url.isEmpty()) {
                        return null;
                    }
                    return downLoadFile(context, updateInfo.getAppUrl());
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
                @Override
                public void call(File file) {
                    if (file == null || !file.exists()) {
                        return;
                    }

                    if (!isNewVersion(context, file)) {
                        return;
                    }

                    MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                            .title("检测到新版本")
                            .content(Html.fromHtml(updateInfo.getDesp()))
                            .contentColorRes(R.color.black)
                            .positiveText("升级")
                            .positiveColorRes(R.color.primary).onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    openFile(context, file.getAbsolutePath());
                                    dialog.dismiss();
                                }
                            }).build();
                    materialDialog.setCancelable(false);
                    materialDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                dialogInterface.dismiss();
                            }
                            return false;
                        }
                    });
                    materialDialog.show();
                }
            });
        }
    }

    //< 版本比较
    private static boolean isNewVersion(Context context, File file) {
        PackageManager packageManager =context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), 0);

        if (packageInfo == null || GoagalInfo.get().packageInfo == null) {
            return false;
        }

        if (packageInfo.versionCode <= GoagalInfo.get().packageInfo.versionCode) {
            return false;
        }
        return true;
    }

    //< 下载文件
    public static File downLoadFile(Context context, String urlStr) {
        // TODO Auto-generated method stub
        final String dirName = PathUtil.getConfigPath(context);
        final File file = new File(dirName + "/" + Md5.md5(urlStr));
        try {
            URL url = new URL(urlStr);
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                if (file.exists() && file.length() == conn.getContentLength()) {
                    if (isNewVersion(context, file)) {
                        conn.disconnect();
                        return file;
                    } else {
                        file.delete();
                    }
                }
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(file);
                byte[] bs = new byte[1024];
                int len;
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                conn.disconnect();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //<  打开apk进行安装
    private static void openFile(Context context, String apkPath) {
        File file = new File(apkPath);
        if (file.exists()) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
}
