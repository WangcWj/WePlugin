package cn.wang.weplugin.load;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.wang.weplugin.BuildConfig;
import cn.wang.weplugin.container.DelegateProviderHolder;
import cn.wang.weplugin.container.GeneratedHostActivityDelegator;
import cn.wang.weplugin.runtime.ShadowContext;

import static cn.wang.weplugin.container.DelegateProvider.LOADER_VERSION_KEY;
import static cn.wang.weplugin.container.DelegateProvider.PROCESS_ID_KEY;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public abstract class ComponentManager implements ShadowContext.PluginComponentLauncher {
    public static final String CM_LOADER_BUNDLE_KEY = "CM_LOADER_BUNDLE";
    public static final String CM_EXTRAS_BUNDLE_KEY = "CM_EXTRAS_BUNDLE";
    public static final String CM_ACTIVITY_INFO_KEY = "CM_ACTIVITY_INFO";
    public static final String CM_CLASS_NAME_KEY = "CM_CLASS_NAME";
    public static final String CM_CALLING_ACTIVITY_KEY = "CM_CALLING_ACTIVITY_KEY";
    public static final String CM_PACKAGE_NAME_KEY = "CM_PACKAGE_NAME";
    public static final String CM_BUSINESS_NAME_KEY = "CM_BUSINESS_NAME";
    public static final String CM_PART_KEY = "CM_PART";

    private Map<String, String> packageNameMap = new HashMap<>();
    private Map<ComponentName, ComponentName> componentMap = new HashMap<>();
    private Map<String, Map<String, List<String>>> application2broadcastInfo = new HashMap<>();
    private Map<ComponentName, PluginInfo> pluginInfoMap = new HashMap<>();
    private Map<ComponentName, PluginComponentInfo> pluginComponentInfoMap = new HashMap<>();

    private PluginServiceManager mPluginServiceManager;
    private PluginContentProviderManager mPluginContentProviderManager;


    public void setPluginServiceManager(PluginServiceManager mPluginServiceManager) {
        this.mPluginServiceManager = mPluginServiceManager;
    }

    public void setPluginContentProviderManager(PluginContentProviderManager mPluginContentProviderManager) {
        this.mPluginContentProviderManager = mPluginContentProviderManager;
    }

    public abstract ComponentName onBindContainerActivity(ComponentName pluginActivity);

    public abstract ContainerProviderInfo onBindContainerContentProvider(ComponentName pluginContentProvider);

    public abstract List<BroadcastInfo> getBroadcastInfoList(String partKey);

    @Override
    public boolean startActivity(ShadowContext shadowContext, Intent pluginIntent, Bundle option) {
        Log.e("cc.wang", "ComponentManager.startActivity." + pluginComponentInfoMap.size());
        if (isPluginComponent(pluginIntent)) {
            shadowContext.superStartActivity(toActivityContainerIntent(pluginIntent), option);
            return true;
        } else {
            Log.e("cc.wang", "ComponentManager.startActivity.没找到");
            return false;
        }
    }

    @Override
    public boolean startActivityForResult(GeneratedHostActivityDelegator delegator, Intent pluginIntent, int requestCode, Bundle option, ComponentName callingActivity) {
        if (isPluginComponent(pluginIntent)) {
            Intent intent = toActivityContainerIntent(pluginIntent);
            intent.putExtra(CM_CALLING_ACTIVITY_KEY, callingActivity);
            delegator.startActivityForResult(intent, requestCode, option);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Pair<Boolean, ComponentName> startService(ShadowContext context, Intent service) {
        if (isPluginComponent(service)) {
            ComponentName component = mPluginServiceManager.startPluginService(service);
            if (null != component) {
                return new Pair<>(true, component);
            }
        }
        return new Pair<>(false, service.getComponent());
    }

    @Override
    public Pair<Boolean, Boolean> stopService(ShadowContext context, Intent service) {
        if (isPluginComponent(service)) {
            boolean stop = mPluginServiceManager.stopPluginService(service);
            return new Pair<>(true, stop);
        }
        return new Pair<>(false, true);
    }

    @Override
    public Pair<Boolean, Boolean> bindService(ShadowContext context, Intent service, ServiceConnection conn, int flags) {
        if (isPluginComponent(service)) {
            mPluginServiceManager.bindPluginService(service, conn, flags);
            return new Pair<>(true, true);
        }
        return new Pair<>(false, false);
    }

    @Override
    public Pair<Boolean, Object> unbindService(ShadowContext context, ServiceConnection conn) {
        mPluginServiceManager.unbindPluginService(conn);
        return new Pair<>(false, null);
    }

    @Override
    public Intent convertPluginActivityIntent(Intent pluginIntent) {
        if (isPluginComponent(pluginIntent)) {
            return toActivityContainerIntent(pluginIntent);
        } else {
            Log.e("cc.wang", "ComponentManager.convertPluginActivityIntent.未找到");
            return pluginIntent;
        }
    }

    public void addPluginApkInfo(PluginInfo pluginInfo) {

        Set<PluginActivityInfo> activities = pluginInfo.getActivities();
        if (null != activities) {
            for (PluginActivityInfo info : activities) {
                ComponentName componentName = new ComponentName(pluginInfo.getPackageName(), info.className);
                addPluginCommon(info, componentName, pluginInfo);
                componentMap.put(componentName, onBindContainerActivity(componentName));
            }
        }
        Set<PluginServiceInfo> services = pluginInfo.getServices();
        if (services != null) {
            for (PluginServiceInfo info : services) {
                ComponentName componentName = new ComponentName(pluginInfo.getPackageName(), info.className);
                addPluginCommon(info, componentName, pluginInfo);
            }
        }

        Set<PluginProviderInfo> providers = pluginInfo.getProviders();
        if (null == providers) {
            for (PluginProviderInfo info : providers) {
                ComponentName componentName = new ComponentName(pluginInfo.getPackageName(), info.className);
                mPluginContentProviderManager.addContentProviderInfo(pluginInfo.getPartKey(), info, onBindContainerContentProvider(componentName));
            }
        }
    }

    public String getComponentBusinessName(ComponentName componentName) {
        return pluginInfoMap.get(componentName).getBusinessName();
    }

    public String getComponentPartKey(ComponentName componentName) {
        return pluginInfoMap.get(componentName).getPartKey();
    }

    private void addPluginCommon(PluginComponentInfo pluginComponentInfo, ComponentName componentName, PluginInfo pluginInfo) {
        packageNameMap.put(pluginComponentInfo.className, pluginInfo.getPackageName());
        PluginInfo previousValue = pluginInfoMap.put(componentName, pluginInfo);
        if (previousValue != null) {
            throw new IllegalStateException("重复添加Component：$componentName");
        }
        pluginComponentInfoMap.put(componentName, pluginComponentInfo);
    }

    private boolean isPluginComponent(Intent intent) {
        ComponentName component = intent.getComponent();
        if (null == component) {
            return false;
        }
        String className = component.getClassName();
        return packageNameMap.containsKey(className);
    }

    private Intent toActivityContainerIntent(Intent intent) {
        Bundle bundle = new Bundle();
        PluginComponentInfo pluginComponentInfo = pluginComponentInfoMap.get(intent.getComponent());
        bundle.putParcelable(CM_ACTIVITY_INFO_KEY, pluginComponentInfo);
        return toContainerIntent(intent, bundle);
    }

    private Intent toContainerIntent(Intent intent, Bundle bundleForPluginLoader) {
        ComponentName component = intent.getComponent();
        String className = component.getClassName();

        String packageName = packageNameMap.get(className);
        intent.setComponent(new ComponentName(packageName, className));
        ComponentName containerComponent = componentMap.get(component);
        PluginInfo pluginInfo = pluginInfoMap.get(component);
        String businessName = pluginInfo.getBusinessName();
        String partKey = pluginInfo.getPartKey();

        Bundle pluginExtras = intent.getExtras();
        intent.replaceExtras((Bundle) null);

        Intent containerIntent = new Intent(intent);
        containerIntent.setComponent(containerComponent);
        Log.e("cc.wang", "ComponentManager.toActivityContainerIntent.setComponent  " + containerComponent.getClassName());
        bundleForPluginLoader.putString(CM_CLASS_NAME_KEY, className);
        bundleForPluginLoader.putString(CM_PACKAGE_NAME_KEY, packageName);


        containerIntent.putExtra(CM_EXTRAS_BUNDLE_KEY, pluginExtras);
        containerIntent.putExtra(CM_BUSINESS_NAME_KEY, businessName);
        containerIntent.putExtra(CM_PART_KEY, partKey);
        containerIntent.putExtra(CM_LOADER_BUNDLE_KEY, bundleForPluginLoader);
        containerIntent.putExtra(LOADER_VERSION_KEY, BuildConfig.VERSION_NAME);
        containerIntent.putExtra(PROCESS_ID_KEY, DelegateProviderHolder.sCustomPid);
        return containerIntent;
    }

    public Map<String, List<String>> getBroadcastsByPartKey(String partKey) {
        if (application2broadcastInfo.get(partKey) == null) {
            Map<String, List<String>> stringListMap = new HashMap<>();
            List<BroadcastInfo> broadcastInfoList = getBroadcastInfoList(partKey);
            if (null != broadcastInfoList) {
                for (BroadcastInfo info : broadcastInfoList) {
                    ArrayList<String> strings = new ArrayList<String>(Arrays.asList(info.actions));
                    stringListMap.put(info.className, strings);
                }
            }
            application2broadcastInfo.put(partKey, stringListMap);
        }
        return application2broadcastInfo.get(partKey);
    }

    public static final class BroadcastInfo {
        private final String className;
        private final String[] actions;

        public final String getClassName() {
            return this.className;
        }

        public final String[] getActions() {
            return this.actions;
        }

        public BroadcastInfo(String className, String[] actions) {
            super();
            this.className = className;
            this.actions = actions;
        }
    }


}
