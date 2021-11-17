package cn.wang.weplugin.runtime;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import android.util.Log;

public class ShadowAppComponentFactory {

    public ShadowApplication instantiateApplication(ClassLoader cl,
                                                    String className)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ShadowApplication) cl.loadClass(className).newInstance();
    }

    public ShadowActivity instantiateActivity(ClassLoader cl, String className,
                                              Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Object aClass = cl.loadClass(className).newInstance();
        Log.e("cc.wang","ShadowAppComponentFactory.instantiateActivity is   "+aClass+"   ClassLoader   "+cl);
        return (ShadowActivity)aClass;
    }

    public BroadcastReceiver instantiateReceiver(ClassLoader cl,
                                                 String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (BroadcastReceiver) cl.loadClass(className).newInstance();
    }

    public ShadowService instantiateService(ClassLoader cl,
                                            String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ShadowService) cl.loadClass(className).newInstance();
    }

    public ContentProvider instantiateProvider(ClassLoader cl,
                                               String className)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ContentProvider) cl.loadClass(className).newInstance();
    }
}
