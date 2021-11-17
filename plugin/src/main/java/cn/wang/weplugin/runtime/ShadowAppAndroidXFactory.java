package cn.wang.weplugin.runtime;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;

public class ShadowAppAndroidXFactory extends ShadowAppComponentFactory {

    @Override
    public ShadowApplication instantiateApplication(ClassLoader cl,
                                                    String className)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ShadowApplication) cl.loadClass(className).newInstance();
    }

    @Override
    public ShadowActivity instantiateActivity(ClassLoader cl, String className,
                                              Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ShadowActivity) cl.loadClass(className).newInstance();
    }

    @Override
    public BroadcastReceiver instantiateReceiver(ClassLoader cl,
                                                 String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (BroadcastReceiver) cl.loadClass(className).newInstance();
    }

    @Override
    public ShadowService instantiateService(ClassLoader cl,
                                            String className, Intent intent)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ShadowService) cl.loadClass(className).newInstance();
    }

    @Override
    public ContentProvider instantiateProvider(ClassLoader cl,
                                               String className)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ContentProvider) cl.loadClass(className).newInstance();
    }
}
