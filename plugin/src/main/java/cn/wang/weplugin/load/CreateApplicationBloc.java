package cn.wang.weplugin.load;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.util.Log;

import cn.wang.weplugin.runtime.ShadowAppComponentFactory;
import cn.wang.weplugin.runtime.ShadowApplication;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class CreateApplicationBloc {

    public static ShadowApplication createShadowApplication(PluginClassLoader pluginClassLoader, PluginInfo pluginInfo, Resources resources, Context hostAppContext, ComponentManager componentManager, ApplicationInfo applicationInfo, ShadowAppComponentFactory appComponentFactory) throws CreateApplicationException {
        try {
            Log.e("cc.wang","CreateApplicationBloc.createShadowApplication.开始创建 ");
            String appClassName = pluginInfo.getApplicationClassName();
            if (appClassName == null) {
                appClassName = ShadowApplication.class.getName();
            }

            ShadowApplication shadowApplication = appComponentFactory.instantiateApplication(pluginClassLoader, appClassName);
            String partKey = pluginInfo.getPartKey();
            shadowApplication.setPluginResources(resources);
            shadowApplication.setPluginClassLoader(pluginClassLoader);
            shadowApplication.setPluginComponentLauncher(componentManager);
            shadowApplication.setBroadcasts(componentManager.getBroadcastsByPartKey(partKey));
            shadowApplication.setApplicationInfo(applicationInfo);
            shadowApplication.setBusinessName(pluginInfo.getBusinessName());
            shadowApplication.setPluginPartKey(partKey);
            //和ShadowActivityDelegate.initPluginActivity一样，attachBaseContext放到最后
            shadowApplication.setHostApplicationContextAsBase(hostAppContext);
            shadowApplication.setTheme(applicationInfo.theme);
            Log.e("cc.wang","CreateApplicationBloc.createShadowApplication.ShadowApplication  创建成功");
            return shadowApplication;
        } catch (Exception e) {
            Log.e("cc.wang","CreateApplicationBloc.createShadowApplication.Exception "+e);
            throw new CreateApplicationException(e);
        }
    }
}
