package com.moses.io;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.io.IOException;
import java.lang.reflect.Method;

import dalvik.system.DexFile;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void doTest() {
        DexFile dexFile;
        try {
            dexFile = DexFile.loadDex("/sdcard/moses.apk", "/sdcard/moses_temp.apk", 0);
            Object obj = dexFile.loadClass("com.moses.mosesnews.NewsFragment", null).newInstance();
            Method method = obj.getClass().getDeclaredMethod("getName", null);

        } catch (IOException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}