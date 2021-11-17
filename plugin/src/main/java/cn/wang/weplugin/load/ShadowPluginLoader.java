package cn.wang.weplugin.load;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.wang.weplugin.common.InstalledApk;
import cn.wang.weplugin.container.ContentProviderDelegateProvider;
import cn.wang.weplugin.container.DelegateProvider;
import cn.wang.weplugin.container.DelegateProviderHolder;
import cn.wang.weplugin.container.HostActivityDelegate;
import cn.wang.weplugin.container.HostActivityDelegator;
import cn.wang.weplugin.container.HostContentProviderDelegate;
import cn.wang.weplugin.manager.LoadParameters;
import cn.wang.weplugin.runtime.ShadowApplication;
import cn.wang.weplugin.runtime.UriConverter;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public abstract class ShadowPluginLoader implements DelegateProvider, DI, ContentProviderDelegateProvider {

    public ExecutorService mExecutorService = Executors.newCachedThreadPool();

    private String delegateProviderKey = DelegateProviderHolder.DEFAULT_KEY;
    private ReentrantLock mLock = new ReentrantLock();
    private HashMap<String, PluginParts> mPluginPartsMap = new HashMap<>();
    public ComponentManager mComponentManager;
    private Set<PackageInfo> mPluginPackageInfoSet = new HashSet<>();
    private PluginServiceManager mPluginServiceManager;
    private PluginContentProviderManager mPluginContentProviderManager;
    private ReentrantLock mPluginServiceManagerLock;
    private Context mHostAppContext;
    private Handler mUiHandler;

    public abstract ComponentManager getComponentManager();


    public String getDelegateProviderKey() {
        return delegateProviderKey;
    }

    public ShadowPluginLoader(Context hostAppContext) {
        this.mHostAppContext = hostAppContext;
        mPluginContentProviderManager = new PluginContentProviderManager();
        mPluginServiceManagerLock = new ReentrantLock();
        mUiHandler = new Handler(Looper.getMainLooper());
        UriConverter.setUriParseDelegate(mPluginContentProviderManager);
    }

    public PluginServiceManager getPluginServiceManager() {
        Lock currentLock = mPluginServiceManagerLock;
        PluginServiceManager manager;
        currentLock.lock();
        try {
            manager = this.mPluginServiceManager;
        } finally {
            currentLock.unlock();
        }
        return manager;
    }

    public PluginParts getPluginParts(String partKey) {
        PluginParts pluginParts;
        mLock.lock();
        try {
            pluginParts = mPluginPartsMap.get(partKey);
        } finally {
            mLock.unlock();
        }
        Log.e("cc.wang","ShadowPluginLoader.getPluginParts."+pluginParts+"  mPluginPartsMap   "+mPluginPartsMap);
        return pluginParts;
    }

    public Map<String, PluginParts> getAllPluginPart() {
        Map<String, PluginParts> maps;
        mLock.lock();
        try {
            maps = mPluginPartsMap;
        } finally {
            mLock.unlock();
        }
        return maps;
    }

    public void onCreate() {
        mComponentManager = getComponentManager();
        mComponentManager.setPluginContentProviderManager(mPluginContentProviderManager);
    }

    private void realAction(String partKey) {
        PluginParts pluginParts = getPluginParts(partKey);
        ShadowApplication application = pluginParts.getApplication();
        application.attachBaseContext(mHostAppContext);
        mPluginContentProviderManager.createContentProviderAndCallOnCreate(application, partKey, pluginParts);
        application.onCreate();
    }

    public void callApplicationOnCreate(final String partKey) {
        if (isUiThread()) {
            realAction(partKey);
        } else {
            final CountDownLatch waitUiLock = new CountDownLatch(1);
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    realAction(partKey);
                    waitUiLock.countDown();
                }
            });
            try {
                waitUiLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Future<?>  loadPlugin(InstalledApk installedApk) throws LoadPluginException {

        LoadParameters loadParameters = getLoadParameters(installedApk);
        mPluginServiceManagerLock.lock();
        try {
           if(mPluginServiceManager == null){
               mPluginServiceManager = new PluginServiceManager(this,mHostAppContext);
           }
            mComponentManager.setPluginServiceManager(mPluginServiceManager);
        }finally {
            mPluginServiceManagerLock.unlock();
        }

        Log.e("cc.wang","ShadowPluginLoader.loadPlugin."+loadParameters.toString());
        return new LoadPluginBloc().loadPlugin(
                mExecutorService,
                mPluginPackageInfoSet,
                allPluginPackageInfo(),
                mComponentManager,
                mLock,
                mPluginPartsMap,
                mHostAppContext,
                installedApk,
                loadParameters
                );
    }


    private PackageInfo[] allPluginPackageInfo(){
        PackageInfo[] result;
        mLock.lock();
        try {
            result = new PackageInfo[mPluginPackageInfoSet.size()];
            result =mPluginPackageInfoSet.toArray(result);
        }finally {
            mLock.unlock();
        }
        Log.e("cc.wang","ShadowPluginLoader.allPluginPackageInfo."+result);
        return result;
    }

    @Override
    public HostActivityDelegate getHostActivityDelegate(Class<? extends HostActivityDelegator> aClass){
         return new ShadowActivityDelegate(this);
    }

    @Override
    public HostContentProviderDelegate getHostContentProviderDelegate(){
        return new ShadowContentProviderDelegate(mPluginContentProviderManager);
    }

    @Override
    public void inject(ShadowDelegate delegate, String partKey){
        mLock.lock();
        try {
            PluginParts pluginParts = mPluginPartsMap.get(partKey);
            if(pluginParts == null){
                throw new IllegalArgumentException("partKey=="+partKey+"在map中找不到。此时map："+mPluginPartsMap);
            }else {
                delegate.inject(pluginParts.appComponentFactory);
                delegate.inject(pluginParts.application);
                delegate.inject(pluginParts.classLoader);
                delegate.inject(pluginParts.resources);
                delegate.inject(mComponentManager);
            }
        }finally {
            mLock.unlock();
        }
    }

    public LoadParameters getLoadParameters(InstalledApk installedApk){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(installedApk.parcelExtras,0,installedApk.parcelExtras.length);
        parcel.setDataPosition(0);
        LoadParameters loadParameters = new LoadParameters(parcel);
        parcel.recycle();
        return loadParameters;
    }


    private boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
