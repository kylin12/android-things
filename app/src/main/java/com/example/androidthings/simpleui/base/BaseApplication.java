package com.example.androidthings.simpleui.base;

import android.app.Application;
import android.graphics.Typeface;

import java.lang.reflect.Field;



public class BaseApplication extends Application {

    private static BaseApplication context;
    private static Typeface typeface;

    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        typeface=Typeface.createFromAsset(getAssets(), "simhei.ttf");
        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static BaseApplication context() {
        return context;
    }

}
