package cn.wang.weplugin.load;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import cn.wang.weplugin.common.InstalledApk;
import cn.wang.weplugin.manager.LoadParameters;
import cn.wang.weplugin.runtime.PluginPartInfo;
import cn.wang.weplugin.runtime.PluginPartInfoManager;
import cn.wang.weplugin.runtime.ShadowAppComponentFactory;
import cn.wang.weplugin.runtime.ShadowApplication;
import cn.wang.weplugin.runtime.ShadowContext;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class LoadPluginBloc {
    public static final LoadPluginBloc INSTANCE;

    static {
        INSTANCE = new LoadPluginBloc();
    }

    public final Future loadPlugin(ExecutorService executorService,
                                   final Set<PackageInfo> pluginPackageInfoSet,
                                   final PackageInfo[] allPluginPackageInfo,
                                   final ComponentManager componentManager,
                                   final ReentrantLock lock,
                                   final Map<String, PluginParts> pluginPartsMap,
                                   final Context hostAppContext,
                                   final InstalledApk installedApk,
                                   final LoadParameters loadParameters) throws LoadPluginException {
        final Future<PluginClassLoader> buildClassLoader = executorService.submit(new Callable<PluginClassLoader>() {
            @Override
            public PluginClassLoader call() throws Exception {
                PluginClassLoader pluginClassLoader;
                lock.lock();
                try {
                    pluginClassLoader = LoadApkBloc.INSTANCE.loadPlugin(installedApk, loadParameters, pluginPartsMap);
                } finally {
                    lock.unlock();
                }
                Log.e("cc.wang","LoadPluginBloc.call.buildClassLoader 创建成功");
                return pluginClassLoader;
            }
        });
        final Future<PackageInfo> getPackageInfo = executorService.submit(new Callable<PackageInfo>() {
            @Override
            public PackageInfo call() throws Exception {
                String archiveFilePath = installedApk.apkFilePath;
                PackageManager packageManager = hostAppContext.getPackageManager();
                //获取插件的PackageInfo。
                PackageInfo archiveInfo = packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES |
                        PackageManager.GET_META_DATA |
                        PackageManager.GET_SERVICES |
                        PackageManager.GET_PROVIDERS |
                        PackageManager.GET_SIGNATURES);
                if (null == archiveInfo) {
                    throw new NullPointerException("getPackageArchiveInfo return null.archiveFilePath==" + archiveFilePath);
                }
                ShadowContext tempContext = new ShadowContext(hostAppContext, 0);
                tempContext.setBusinessName(loadParameters.businessName);
                File dataDir;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dataDir = tempContext.getDataDir();
                } else {
                    dataDir = new File(tempContext.getFilesDir(), "dataDir");
                }
                dataDir.mkdirs();
                archiveInfo.applicationInfo.nativeLibraryDir = installedApk.libraryPath;
                archiveInfo.applicationInfo.dataDir = dataDir.getAbsolutePath();
                archiveInfo.applicationInfo.processName = hostAppContext.getApplicationInfo().processName;
                archiveInfo.applicationInfo.uid = hostAppContext.getApplicationInfo().uid;
                lock.lock();
                try {
                    pluginPackageInfoSet.add(archiveInfo);
                } finally {
                    lock.unlock();
                }
                Log.e("cc.wang","LoadPluginBloc.call.PackageInfo 创建成功");
                return archiveInfo;
            }
        });


        final Future<PluginInfo> buildPluginInfo = executorService.submit(new Callable<PluginInfo>() {
            @Override
            public PluginInfo call() throws Exception {
                PackageInfo packageInfo = getPackageInfo.get();
                return ParsePluginApkBloc.parse(packageInfo, loadParameters, hostAppContext);
            }
        });
        final Future<PluginPackageManagerImpl> buildPackageManager = executorService.submit(new Callable<PluginPackageManagerImpl>() {
            @Override
            public PluginPackageManagerImpl call() throws Exception {
                PackageInfo packageInfo = getPackageInfo.get();
                PackageManager packageManager = hostAppContext.getPackageManager();
                Log.e("cc.wang","LoadPluginBloc.call.PackageManagerImpl 创建成功");
                return new PluginPackageManagerImpl(packageManager, packageInfo, allPluginPackageInfo);
            }
        });

        final Future<Resources> buildResources = executorService.submit(new Callable<Resources>() {
            @Override
            public Resources call() throws Exception {
                PackageInfo packageInfo = getPackageInfo.get();
                return CreateResourceBloc.create(packageInfo, installedApk.apkFilePath, hostAppContext);
            }
        });
        final Future<ShadowAppComponentFactory> buildAppComponentFactory = executorService.submit(new Callable<ShadowAppComponentFactory>() {
            @Override
            public ShadowAppComponentFactory call() throws Exception {
                    /*PluginClassLoader pluginClassLoader = buildClassLoader.get();
                    PluginInfo pluginInfo = buildPluginInfo.get();
                    if(!TextUtils.isEmpty(pluginInfo.getAppComponentFactory())){
                        Class<?> aClass = pluginClassLoader.loadClass(pluginInfo.getAppComponentFactory());
                        return  (ShadowAppComponentFactory) aClass.newInstance();
                    }*/
                    Log.e("cc.wang","LoadPluginBloc.call.ShadowAppComponentFactory 创建成功");
                return new ShadowAppComponentFactory();
            }
        });
        final Future<ShadowApplication> buildApplication = executorService.submit(new Callable<ShadowApplication>() {
            @Override
            public ShadowApplication call() throws Exception {
                Log.e("cc.wang","LoadPluginBloc.call.buildApplication  pluginInfo"+buildPluginInfo.get());
                return CreateApplicationBloc.createShadowApplication(
                        buildClassLoader.get(),
                        buildPluginInfo.get(),
                        buildResources.get(),
                        hostAppContext,
                        componentManager,
                        getPackageInfo.get().applicationInfo,
                        buildAppComponentFactory.get());
            }
        });

        Future<?> buildRunningPlugin = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    if (!new File(installedApk.apkFilePath).exists()) {
                        throw new LoadPluginException("插件文件不存在.pluginFile==" + installedApk.apkFilePath);
                    }
                    PluginPackageManagerImpl pluginPackageManager = buildPackageManager.get();
                    PluginClassLoader pluginClassLoader = buildClassLoader.get();
                    Resources resources = buildResources.get();
                    PluginInfo pluginInfo = buildPluginInfo.get();
                    ShadowApplication shadowApplication = buildApplication.get();
                    ShadowAppComponentFactory shadowAppComponentFactory = buildAppComponentFactory.get();
                    lock.lock();
                    componentManager.addPluginApkInfo(pluginInfo);
                    pluginPartsMap.put(pluginInfo.getPartKey(), new PluginParts(shadowAppComponentFactory, shadowApplication, pluginClassLoader, resources, pluginInfo.getBusinessName(), pluginPackageManager));
                    PluginPartInfoManager.addPluginInfo(pluginClassLoader, new PluginPartInfo(
                            shadowApplication,
                            resources,
                            pluginClassLoader,
                            pluginPackageManager
                    ));
                } catch (Exception e) {
                    Log.e("cc.wang", "LoadPluginBloc.call.Exception   " + e);
                } finally {
                    lock.unlock();
                }
                return new Object();
            }
        });
        return buildRunningPlugin;
    }

}
