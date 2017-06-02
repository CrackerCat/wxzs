package com.ff.wxzs.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ff.wxzs.R;
import com.kk.utils.FPUitl;

import rx.Observable;

/**
 * Created by zhangkai on 2017/4/10.
 */

public class TipUtil {

    public static Observable<Boolean> firstTip(Context context, String key, String value, String content) {
        return firstTip(context, key, value, content, true, null);
    }

    public static Observable<Boolean> firstTip(Context context, String key, String value, Spanned content) {
        return firstTip(context, key, value, content, true, null);
    }

    public static Observable<Boolean> firstTip(Context context, String key, String value, String content, Runnable runnable) {
        return firstTip(context, key, value, content, true, runnable);
    }

    public static Observable<Boolean> firstTip(Context context, String key, String value, Spanned content, Runnable runnable) {
        return firstTip(context, key, value, content, true, runnable);
    }

    //< 首次进入提示
    public static Observable<Boolean> firstTip(Context context, String key, String value, String content, boolean salt,
                                               Runnable
                                                       runnable) {
        String fristTipStr = FPUitl.get(context, key, "");
        Boolean flag = fristTipStr.isEmpty() && salt;
        if (flag) {
            FPUitl.putString(context, key, value);
            tip(context, content, runnable);
        }
        return Observable.just(flag);
    }

    public static Observable<Boolean> firstTip(Context context, String key, String value, Spanned content, boolean salt,
                                               Runnable
                                                       runnable) {
        String fristTipStr = FPUitl.get(context, key, "");
        Boolean flag = fristTipStr.isEmpty() && salt;
        if (flag) {
            FPUitl.putString(context, key, value);
            tip(context, content, runnable);
        }
        return Observable.just(flag);
    }

    public static void tip(Context context, String content) {
        tip(context, content, null);
    }

    public static void tip(Context context, Spanned content) {
        tip(context, content, null);
    }

    public static void tip(Context context, String content, Runnable runnable) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title("提示")
                .content(content)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.primary)
                .negativeText("确定")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                }).build();
        materialDialog.setCancelable(false);
        materialDialog.show();
    }

    public static void tip(Context context, Spanned content, Runnable runnable) {
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title("提示")
                .content(content)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.primary)
                .negativeText("确定")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                }).build();
        materialDialog.setCancelable(false);
        materialDialog.show();
    }
}
