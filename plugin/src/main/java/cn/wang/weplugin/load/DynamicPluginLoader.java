package cn.wang.weplugin.load;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import cn.wang.weplugin.common.InstalledApk;
import cn.wang.weplugin.container.ContentProviderDelegateProvider;
import cn.wang.weplugin.container.ContentProviderDelegateProviderHolder;
import cn.wang.weplugin.container.DelegateProviderHolder;
import cn.wang.weplugin.manager.UuidManager;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public final class DynamicPluginLoader {
    private final ShadowPluginLoader mPluginLoader;
    private Context mContext;
    private UuidManager mUuidManager;
    private String mUuid;
    private final Handler mUiHandler;
    private final HashMap<IBinder, ServiceConnection> mConnectionMap;
    public DynamicPluginLoader(Context hostContext, String uuid) {
        super();
        this.mUiHandler = new Handler(Looper.getMainLooper());
        this.mConnectionMap = new HashMap();
        try {
            CoreLoaderFactory coreLoaderFactory = new CoreLoaderFactoryImpl();
            this.mPluginLoader = coreLoaderFactory.build(hostContext);
            DelegateProviderHolder.setDelegateProvider(this.mPluginLoader.getDelegateProviderKey(), this.mPluginLoader);
            ContentProviderDelegateProviderHolder.setContentProviderDelegateProvider((ContentProviderDelegateProvider) this.mPluginLoader);
            this.mPluginLoader.onCreate();
        } catch (Exception e) {
            throw new RuntimeException("当前的classLoader找不到PluginLoader的实现", e);
        }
        this.mContext = hostContext;
        this.mUuid = uuid;
    }

    public final void setUuidManager(@Nullable UuidManager p0) {
        if (p0 != null) {
            this.mUuidManager = p0;
        }
         //todo #30 兼容mUuidManager为null时的逻辑
    }

    public final void loadPlugin(String partKey) {
        InstalledApk installedApk = null;
        try {
            installedApk = mUuidManager.getPlugin(mUuid, partKey);
            Log.e("cc.wang","DynamicPluginLoader.loadPlugin."+installedApk.toString());
            Future<?> future = mPluginLoader.loadPlugin(installedApk);
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public final Map<String, Boolean> getLoadedPlugin() {
        Map<String, PluginParts> plugins = this.mPluginLoader.getAllPluginPart();
        HashMap<String, Boolean> loadPlugins = new HashMap<String, Boolean>();
        Set<String> keySet = plugins.keySet();
        for (String key : keySet) {
            PluginParts pluginParts = plugins.get(key);
            loadPlugins.put(key, pluginParts.getApplication().isCallOnCreate);
        }
        return loadPlugins;
    }

    public final synchronized void callApplicationOnCreate(String partKey) {
        this.mPluginLoader.callApplicationOnCreate(partKey);
    }

    @Nullable
    public final Intent convertActivityIntent(Intent pluginActivityIntent) {
        return this.mPluginLoader.getComponentManager().convertPluginActivityIntent(pluginActivityIntent);
    }

    private ComponentName cn;
    private boolean stopped = false;

    @Nullable
    public final synchronized ComponentName startPluginService(final Intent pluginServiceIntent) {
        if (isUiThread()) {
            cn = mPluginLoader.getPluginServiceManager().startPluginService(pluginServiceIntent);
        } else {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    cn = mPluginLoader.getPluginServiceManager().startPluginService(pluginServiceIntent);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return cn;
    }

    public final synchronized boolean stopPluginService(final Intent pluginServiceIntent) {
        if (isUiThread()) {
            stopped = mPluginLoader.getPluginServiceManager().stopPluginService(pluginServiceIntent);
        } else {
            final CountDownLatch waitUiLock = new CountDownLatch(1);
            mUiHandler.post(new Runnable() {
                @Override
                public final void run() {
                    stopped = mPluginLoader.getPluginServiceManager().stopPluginService(pluginServiceIntent);
                    waitUiLock.countDown();
                }
            });
            try {
                waitUiLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return stopped;
    }

    private boolean getConnection(Intent pluginServiceIntent, final BinderPluginServiceConnection binderPsc, final int flags) {
        ServiceConnection serviceConnection = mConnectionMap.get(binderPsc.getRemote());
        if (null == serviceConnection) {
            serviceConnection = new ServiceConnectionWrapper(binderPsc);
            mConnectionMap.put(binderPsc.getRemote(), serviceConnection);
        }
        return mPluginLoader.getPluginServiceManager().bindPluginService(pluginServiceIntent, serviceConnection, flags);
    }

    public final synchronized boolean bindPluginService(final Intent pluginServiceIntent, final BinderPluginServiceConnection binderPsc, final int flags) {
        stopped = false;
        if (isUiThread()) {
            stopped = getConnection(pluginServiceIntent, binderPsc, flags);
        } else {
            final CountDownLatch waitUiLock = new CountDownLatch(1);
            mUiHandler.post(new Runnable() {
                @Override
                public final void run() {
                    stopped = getConnection(pluginServiceIntent, binderPsc, flags);
                    waitUiLock.countDown();
                }
            });
            try {
                waitUiLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return stopped;
    }

    public final synchronized void unbindService(final IBinder connBinder) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                ServiceConnection serviceConnection = mConnectionMap.get(connBinder);
                if (null != serviceConnection) {
                    mConnectionMap.remove(connBinder);
                    mPluginLoader.getPluginServiceManager().unbindPluginService(serviceConnection);
                }
            }
        });
    }

    public final synchronized void startActivityInPluginProcess(final Intent intent) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mContext.startActivity(intent);
            }
        });
    }

    private final boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public final <T> T getInterface(ClassLoader classLoader, Class<T> clazz, String className) throws Exception {
        try {
            Class<?> interfaceImplementClass = classLoader.loadClass(className);
            Object var10000 = interfaceImplementClass.newInstance();
            return clazz.cast(var10000);
        } catch (ClassNotFoundException e) {
            throw new Exception(e);
        } catch (InstantiationException e) {
            throw new Exception(e);
        } catch (ClassCastException e) {
            throw new Exception(e);
        } catch (IllegalAccessException e) {
            throw new Exception(e);
        }
    }

    private static final class ServiceConnectionWrapper implements ServiceConnection {
        private final BinderPluginServiceConnection mConnection;

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                this.mConnection.onServiceDisconnected(name);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.mConnection.onServiceConnected(name, service);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public ServiceConnectionWrapper(BinderPluginServiceConnection mConnection) {
            super();
            this.mConnection = mConnection;
        }
    }

}
