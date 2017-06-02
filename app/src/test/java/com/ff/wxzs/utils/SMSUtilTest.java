package com.ff.wxzs.utils;


import android.test.mock.MockContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * Created by zhangkai on 2017/5/4.
 */


public class SMSUtilTest {

    @Mock
    MockContext mockContext;

    @Test
    public void send() throws Exception {
        PowerMockito.mockStatic(PreferenceUtil.class);
        PowerMockito.when(PreferenceUtil.class, PowerMockito.method(PreferenceUtil.class,
                "getImpl"));
    }

}