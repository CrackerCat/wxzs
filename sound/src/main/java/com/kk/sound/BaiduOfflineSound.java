package com.kk.sound;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import com.baidu.speech.VoiceRecognitionService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhangkai on 2017/5/10.
 */

public class BaiduOfflineSound {
    private SpeechRecognizer speechRecognizer;
    private RecognitionListener recognitionListener;
    public static final int OFFLINE_RESULT_OK = 1;

    private Activity mActivity;
    private static BaiduOfflineSound instance;

    public static BaiduOfflineSound get(Activity activity, RecognitionListener recognitionListener) {
        synchronized (BaiduOfflineSound.class) {
            if (instance == null)
                instance = new BaiduOfflineSound(activity, recognitionListener);
        }
        return instance;
    }

    public static BaiduOfflineSound get(Activity activity) {
        return get(activity, null);
    }


    public BaiduOfflineSound(Activity activity) {
        this(activity, null);
    }

    public BaiduOfflineSound(Activity activity, RecognitionListener recognitionListener) {
        this.mActivity = activity;
        this.recognitionListener = recognitionListener;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(mActivity, new ComponentName(mActivity, VoiceRecognitionService.class));
        speechRecognizer.setRecognitionListener(recognitionListener);
    }

    public void start() {
        Intent intent = createIntent();
        intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
        mActivity.startActivityForResult(intent, OFFLINE_RESULT_OK);
    }

    public void start2() {
        if (recognitionListener == null) throw new NullPointerException("recognitionListener is null!");
        Intent intent = createIntent();
        speechRecognizer.startListening(intent);
    }

    private Intent createIntent() {
        Intent intent = new Intent();
        bindParams(intent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
        {
            String args = sp.getString("args", "");
            if (null != args) {
                intent.putExtra("args", args);
            }
        }
        return intent;
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == OFFLINE_RESULT_OK) {
            Bundle results = data.getExtras();
            ArrayList<String> results_recognition = results.getStringArrayList("results_recognition");
        }
    }


    public void bindParams(Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
        if (sp.getBoolean("tips_sound", true)) {
            intent.putExtra(Constant.EXTRA_SOUND_START, R.raw.bdspeech_recognition_start);
            intent.putExtra(Constant.EXTRA_SOUND_END, R.raw.bdspeech_speech_end);
            intent.putExtra(Constant.EXTRA_SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
            intent.putExtra(Constant.EXTRA_SOUND_ERROR, R.raw.bdspeech_recognition_error);
            intent.putExtra(Constant.EXTRA_SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);
        }
        if (sp.contains(Constant.EXTRA_INFILE)) {
            String tmp = sp.getString(Constant.EXTRA_INFILE, "").replaceAll(",.*", "").trim();
            intent.putExtra(Constant.EXTRA_INFILE, tmp);
        }
        if (sp.getBoolean(Constant.EXTRA_OUTFILE, false)) {
            intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
        }
        if (sp.getBoolean(Constant.EXTRA_GRAMMAR, false)) {
            intent.putExtra(Constant.EXTRA_GRAMMAR, "assets:///baidu_speech_grammar.bsg");
        }
        if (sp.contains(Constant.EXTRA_SAMPLE)) {
            String tmp = sp.getString(Constant.EXTRA_SAMPLE, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt(tmp));
            }
        }
        if (sp.contains(Constant.EXTRA_LANGUAGE)) {
            String tmp = sp.getString(Constant.EXTRA_LANGUAGE, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_LANGUAGE, tmp);
            }
        }
        if (sp.contains(Constant.EXTRA_NLU)) {
            String tmp = sp.getString(Constant.EXTRA_NLU, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_NLU, tmp);
            }
        }

        if (sp.contains(Constant.EXTRA_VAD)) {
            String tmp = sp.getString(Constant.EXTRA_VAD, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_VAD, tmp);
            }
        }
        String prop = null;
        if (sp.contains(Constant.EXTRA_PROP)) {
            String tmp = sp.getString(Constant.EXTRA_PROP, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_PROP, Integer.parseInt(tmp));
                prop = tmp;
            }
        }

        // offline asr
        {
            intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "/sdcard/easr/s_1");
            if (null != prop) {
                int propInt = Integer.parseInt(prop);
                if (propInt == 10060) {
                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_Navi");
                } else if (propInt == 20000) {
                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_InputMethod");
                }
            }
        }
    }
}
