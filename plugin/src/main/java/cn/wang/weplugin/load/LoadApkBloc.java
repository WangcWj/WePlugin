package cn.wang.weplugin.load;

import java.io.File;
import java.util.Map;

import cn.wang.weplugin.common.InstalledApk;
import cn.wang.weplugin.core.Logger;
import cn.wang.weplugin.manager.LoadParameters;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class LoadApkBloc {
    public static final LoadApkBloc INSTANCE;

    static {
        INSTANCE = new LoadApkBloc();
    }

    public PluginClassLoader loadPlugin(InstalledApk installedApk, LoadParameters loadParameters, Map<String, PluginParts> pluginPartsMap) throws LoadApkException {

        File apk = new File(installedApk.apkFilePath);
        File odexDir = null;
        if(installedApk.oDexPath != null){
            odexDir = new File(installedApk.oDexPath);
        }
        String[] dependsOn = loadParameters.dependsOn;
        ClassLoader hostClassLoader = Logger.class.getClassLoader();
        ClassLoader hostParentClassLoader = hostClassLoader.getParent();
        if(dependsOn == null || dependsOn.length <= 0){
            return new PluginClassLoader(apk.getAbsolutePath(),installedApk.libraryPath,odexDir,hostClassLoader,hostParentClassLoader,loadParameters.hostWhiteList);
        }else if(dependsOn.length == 1){
            String partKey = dependsOn[0];
            PluginParts pluginParts = pluginPartsMap.get(partKey);
            if(null ==pluginParts){
                throw new LoadApkException("加载" + loadParameters.partKey + "时它的依赖" + partKey + "还没有加载");
            }else {
                return new PluginClassLoader(apk.getAbsolutePath(),installedApk.libraryPath,odexDir,pluginParts.classLoader,null,loadParameters.hostWhiteList);
            }
        }else {
            ClassLoader[] dependsOnClassLoaders = new ClassLoader[dependsOn.length];
            for (int i = 0; i < dependsOn.length; i++) {
                String it = dependsOn[i];
                PluginParts pluginParts = pluginPartsMap.get(it);
                if(null ==pluginParts){
                    throw new LoadApkException("加载" + loadParameters.partKey + "时它的依赖" + it + "还没有加载");
                }else {
                    dependsOnClassLoaders[i] = pluginParts.getClassLoader();
                }
            }
            CombineClassLoader combineClassLoader = new CombineClassLoader(dependsOnClassLoaders, hostParentClassLoader);
            return new PluginClassLoader(
                    apk.getAbsolutePath(),
                    installedApk.libraryPath,
                    odexDir,
                    combineClassLoader,
                    null,
                    loadParameters.hostWhiteList);
        }
    }
}
