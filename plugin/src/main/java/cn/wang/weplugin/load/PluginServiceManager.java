package cn.wang.weplugin.load;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cn.wang.weplugin.runtime.ShadowAppComponentFactory;
import cn.wang.weplugin.runtime.ShadowApplication;
import cn.wang.weplugin.runtime.ShadowService;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginServiceManager {
    ShadowPluginLoader mPluginLoader;
    Context mHostContext;

    public PluginServiceManager(ShadowPluginLoader mPluginLoader, Context mHostContext) {
        this.mPluginLoader = mPluginLoader;
        this.mHostContext = mHostContext;
    }

    // 保存service的binder
    private HashMap<ComponentName, IBinder> mServiceBinderMap = new HashMap<ComponentName, IBinder>();
    // service对应ServiceConnection集合
    private HashMap<ComponentName, HashSet<ServiceConnection>> mServiceConnectionMap = new HashMap<>();
    // ServiceConnection与对应的Intent的集合
    private HashMap<ServiceConnection, Intent> mConnectionIntentMap = new HashMap<>();
    // 所有已启动的service集合
    private HashMap<ComponentName, ShadowService> mAliveServicesMap = new HashMap<>();
    // 通过startService启动起来的service集合
    private HashSet<ComponentName> mServiceStartByStartServiceSet = new HashSet<>();
    // 存在mAliveServicesMap中，且stopService已经调用的service集合
    private HashSet<ComponentName> mServiceStopCalledMap = new HashSet<ComponentName>();

    private Collection<ShadowService> allDelegates = mAliveServicesMap.values();

    private int startId = 0;

    public int getNewStartId() {
        startId++;
        return startId;
    }

    public ComponentName startPluginService(Intent intent) {
        ComponentName component = intent.getComponent();
        if (!mAliveServicesMap.containsKey(component)) {
            ShadowService service = createServiceAndCallOnCreate(intent);
            mAliveServicesMap.put(component, service);
            mServiceStartByStartServiceSet.add(component);
        }
        mAliveServicesMap.get(component).onStartCommand(intent, 0, getNewStartId());
        return component;
    }

    private ShadowService createServiceAndCallOnCreate(Intent intent) {
        ShadowService service = newServiceInstance(intent);
        service.onCreate();
        return service;
    }

    private ShadowService newServiceInstance(Intent intent) {
        ComponentName componentName = intent.getComponent();
        String businessName = mPluginLoader.mComponentManager.getComponentBusinessName(componentName);
        String partKey = mPluginLoader.mComponentManager.getComponentPartKey(componentName);
        String className = componentName.getClassName();

        TmpShadowDelegate tmpShadowDelegate = new TmpShadowDelegate();
        mPluginLoader.inject(tmpShadowDelegate, partKey);
        ShadowService service = null;
        try {
            service = tmpShadowDelegate.getAppComponentFactory().instantiateService(tmpShadowDelegate.getPluginClassLoader(), className, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.setPluginResources(tmpShadowDelegate.getPluginResources());
        service.setPluginClassLoader(tmpShadowDelegate.getPluginClassLoader());
        service.setShadowApplication(tmpShadowDelegate.getPluginApplication());
        service.setPluginComponentLauncher(tmpShadowDelegate.getComponentManager());
        service.setApplicationInfo(tmpShadowDelegate.getPluginApplication().getApplicationInfo());
        service.setBusinessName(businessName);
        service.setPluginPartKey(partKey);

        //和ShadowActivityDelegate.initPluginActivity一样，attachBaseContext放到最后
        service.setHostContextAsBase(mHostContext);
        return service;
    }

    public boolean stopPluginService(Intent intent) {
        ComponentName component = intent.getComponent();
        if (mAliveServicesMap.containsKey(component)) {
            mServiceStopCalledMap.add(component);
            return destroyServiceIfNeed(component);
        }
        return false;
    }

    public boolean bindPluginService(Intent intent, ServiceConnection conn, int flag) {
        try {
            ComponentName component = intent.getComponent();
            // 1. 看要bind的service是否创建并在运行了
            if (!mAliveServicesMap.containsKey(component)) {
                ShadowService service = createServiceAndCallOnCreate(intent);
                mAliveServicesMap.put(component, service);
            }
            // 2. 检查是否该Service之前是否被绑定过了
            ShadowService service = mAliveServicesMap.get(component);
            if (!mServiceBinderMap.containsKey(component)) {
                mServiceBinderMap.put(component, service.onBind(intent));
            }
            // 3. 如果binder不为空，则要回调onServiceConnected
            IBinder iBinder = mServiceBinderMap.get(component);
            if (mServiceConnectionMap.containsKey(component)) {
                if (mServiceConnectionMap.get(component).contains(conn)) {
                    mServiceConnectionMap.get(component).add(conn);
                    mConnectionIntentMap.put(conn, intent);

                    conn.onServiceConnected(component, iBinder);
                } else {
                    // 已经包含该connection了，说明onServiceConnected已经回调过了，所以这里什么也不用干
                }

            } else {
                HashSet<ServiceConnection> connectionSet = new HashSet<>();
                connectionSet.add(conn);
                mServiceConnectionMap.put(component, connectionSet);
                mConnectionIntentMap.put(conn, intent);
                conn.onServiceConnected(component, iBinder);
            }
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public void unbindPluginService(ServiceConnection connection) {
        Set<ComponentName> componentNames = mServiceConnectionMap.keySet();
        for (ComponentName key : componentNames) {
            HashSet<ServiceConnection> serviceConnections = mServiceConnectionMap.get(key);
            if (serviceConnections.contains(connection)) {
                serviceConnections.remove(connection);
                Intent intent = mConnectionIntentMap.remove(connection);
                if (serviceConnections.size() == 0) {
                    mServiceConnectionMap.remove(key);
                    mAliveServicesMap.get(key).onUnbind(intent);
                }
                destroyServiceIfNeed(key);
                break;
            }
        }
    }

    private boolean destroyServiceIfNeed(ComponentName service) {

        if (!mServiceStartByStartServiceSet.contains(service)) {
            if (mServiceConnectionMap.get(service) != null) {
                destroy(service);
                return true;
            }
        } else {
            if (mServiceStopCalledMap.contains(service) && !mServiceConnectionMap.containsKey(service)) {
                destroy(service);
                return true;
            }
        }
        return false;
    }

    private void destroy(ComponentName service) {
        ShadowService serviceDelegate = mAliveServicesMap.remove(service);
        mServiceStopCalledMap.remove(service);
        mServiceBinderMap.remove(service);
        mServiceStartByStartServiceSet.remove(service);
        serviceDelegate.onDestroy();
    }

    public void onDestroy() {
        mServiceBinderMap.clear();
        mServiceConnectionMap.clear();
        mConnectionIntentMap.clear();
        mAliveServicesMap.clear();
        mServiceStartByStartServiceSet.clear();
        mServiceStopCalledMap.clear();
    }

    public void onTaskRemoved(Intent rootIntent) {
        for (ShadowService service : allDelegates) {
            service.onTaskRemoved(rootIntent);
        }
    }

    public void onTrimMemory(int level) {
        for (ShadowService service : allDelegates) {
            service.onTrimMemory(level);
        }
    }

    public void onLowMemory() {
        for (ShadowService service : allDelegates) {
            service.onLowMemory();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        for (ShadowService service : allDelegates) {
            service.onConfigurationChanged(newConfig);
        }
    }


    private class TmpShadowDelegate extends ShadowDelegate {
        public final ShadowApplication getPluginApplication() {
            return this.getMPluginApplication();
        }

        public final ShadowAppComponentFactory getAppComponentFactory() {
            return this.getMAppComponentFactory();
        }

        public final PluginClassLoader getPluginClassLoader() {
            return this.getMPluginClassLoader();
        }

        public final Resources getPluginResources() {
            return this.getMPluginResources();
        }

        public final ComponentManager getComponentManager() {
            return this.getMComponentManager();
        }

        public TmpShadowDelegate() {
        }
    }

}
