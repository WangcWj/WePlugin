package com.wang.weplugin.services;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.util.Log;

import cn.wang.weplugin.load.LoadPluginCallback;
import cn.wang.weplugin.manager.PluginProcessService;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/9
 */
public class PluginService extends PluginProcessService {

    public PluginService() {

        LoadPluginCallback.setCallback(new LoadPluginCallback.Callback() {
            @Override
            public void beforeLoadPlugin(String partKey) {
                Log.e("cc.wang","PluginService.beforeLoadPlugin.");
            }

            @Override
            public void afterLoadPlugin(String partKey, ApplicationInfo applicationInfo, ClassLoader pluginClassLoader, Resources pluginResources) {
                 Log.e("cc.wang","PluginService.afterLoadPlugin.");
            }
        });

    }
}
