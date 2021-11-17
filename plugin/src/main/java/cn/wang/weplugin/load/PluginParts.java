package cn.wang.weplugin.load;

import android.content.res.Resources;

import cn.wang.weplugin.runtime.PluginPackageManager;
import cn.wang.weplugin.runtime.ShadowAppComponentFactory;
import cn.wang.weplugin.runtime.ShadowApplication;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginParts {
    ShadowAppComponentFactory appComponentFactory;
    ShadowApplication application;
    PluginClassLoader classLoader;
    Resources resources;
    String businessName;
    PluginPackageManager pluginPackageManager;

    public PluginParts(ShadowAppComponentFactory appComponentFactory, ShadowApplication application, PluginClassLoader classLoader, Resources resources, String businessName, PluginPackageManager pluginPackageManager) {
        this.appComponentFactory = appComponentFactory;
        this.application = application;
        this.classLoader = classLoader;
        this.resources = resources;
        this.businessName = businessName;
        this.pluginPackageManager = pluginPackageManager;
    }

    public ShadowAppComponentFactory getAppComponentFactory() {
        return appComponentFactory;
    }

    public void setAppComponentFactory(ShadowAppComponentFactory appComponentFactory) {
        this.appComponentFactory = appComponentFactory;
    }

    public ShadowApplication getApplication() {
        return application;
    }

    public void setApplication(ShadowApplication application) {
        this.application = application;
    }

    public PluginClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(PluginClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public PluginPackageManager getPluginPackageManager() {
        return pluginPackageManager;
    }

    public void setPluginPackageManager(PluginPackageManager pluginPackageManager) {
        this.pluginPackageManager = pluginPackageManager;
    }
}
