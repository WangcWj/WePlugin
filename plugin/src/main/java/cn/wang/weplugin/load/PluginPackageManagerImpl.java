package cn.wang.weplugin.load;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.wang.weplugin.runtime.PluginPackageManager;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class PluginPackageManagerImpl implements PluginPackageManager {

    private PackageManager hostPackageManager;
    private PackageInfo packageInfo;
    private PackageInfo[] allPluginPackageInfo;

    public PluginPackageManagerImpl(PackageManager hostPackageManager, PackageInfo packageInfo, PackageInfo[] allPluginPackageInfo) {
        this.hostPackageManager = hostPackageManager;
        this.packageInfo = packageInfo;
        this.allPluginPackageInfo = allPluginPackageInfo;
    }

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        if (packageInfo.applicationInfo.packageName.equals(packageName)) {
            return packageInfo.applicationInfo;
        }
        return hostPackageManager.getApplicationInfo(packageName, flags);
    }

    @Override
    public ActivityInfo getActivityInfo(ComponentName component, int flags) throws PackageManager.NameNotFoundException {
        if (component.getPackageName().equals(packageInfo.applicationInfo.packageName)) {
            ActivityInfo pluginActivityInfo = null;
            a:
            for (PackageInfo p : allPluginPackageInfo) {
                ActivityInfo[] activities = p.activities;
                for (ActivityInfo a : activities) {
                    if (a.name.equals(component.getClassName())) {
                        pluginActivityInfo = a;
                        break a;
                    }
                }
            }
            if (null != pluginActivityInfo) {
                return pluginActivityInfo;
            }
        }
        return hostPackageManager.getActivityInfo(component, flags);
    }

    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws PackageManager.NameNotFoundException {
        if (packageInfo.applicationInfo.packageName.equals(packageName)) {
            return packageInfo;
        }
        return hostPackageManager.getPackageInfo(packageName, flags);
    }

    @Override
    public ProviderInfo resolveContentProvider(String name, int flags) {
        ProviderInfo pluginProviderInfo = null;
        a:
        for (PackageInfo p : allPluginPackageInfo) {
            ProviderInfo[] activities = p.providers;
            for (ProviderInfo a : activities) {
                if (a.authority.equals(name)) {
                    pluginProviderInfo = a;
                    break a;
                }
            }
        }
        if(null != pluginProviderInfo){
            return pluginProviderInfo;
        }
        return hostPackageManager.resolveContentProvider(name, flags);
    }

    @Override
    public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
        List<ProviderInfo> allList = new ArrayList<>();
        if(processName == null){
            List<ProviderInfo> allNormalProviders = hostPackageManager.queryContentProviders(null, 0, flags);
            List<ProviderInfo> allPluginProviders = new ArrayList<>();
            for (PackageInfo packageInfo :allPluginPackageInfo) {
                ProviderInfo[] providers = packageInfo.providers;
                if(null != providers) {
                    allPluginProviders.addAll(Arrays.asList(providers));
                }
            }
            allList.addAll(allNormalProviders);
            allList.addAll(allPluginProviders);
        }
        return allList;
    }

    @Override
    public ResolveInfo resolveActivity(Intent intent, int flags) {
        ResolveInfo resolveInfo = hostPackageManager.resolveActivity(intent, flags);
        if(resolveInfo.activityInfo == null){
            ResolveInfo it = new ResolveInfo();
            for (PackageInfo packageInfo :allPluginPackageInfo) {
                ActivityInfo[] activities = packageInfo.activities;
                for (ActivityInfo a :activities) {
                    if(a.name.equals(intent.getComponent().getClassName())){
                        it.activityInfo =a;
                    }
                }
            }
            return it;
        }
        return resolveInfo;
    }
}
