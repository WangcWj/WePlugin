package cn.wang.weplugin.load;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.util.Log;

import cn.wang.weplugin.manager.LoadParameters;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class ParsePluginApkBloc {

    public static PluginInfo parse( PackageInfo packageArchiveInfo,  LoadParameters loadParameters,  Context hostAppContext) throws ParsePluginApkException {
        if(!packageArchiveInfo.applicationInfo.packageName.equals(hostAppContext.getPackageName())){
             /*
            要求插件和宿主包名一致有两方面原因：
            1.正常的构建过程中，aapt会将包名写入到arsc文件中。插件正常安装运行时，如果以
            android.content.Context.getPackageName为参数传给
            android.content.res.Resources.getIdentifier方法，可以正常获取到资源。但是在插件环境运行时，
            Context.getPackageName会得到宿主的packageName，则getIdentifier方法不能正常获取到资源。为此，
            一个可选的办法是继承Resources，覆盖getIdentifier方法。但是Resources的构造器已经被标记为
            @Deprecated了，未来可能会不可用，因此不首选这个方法。

            2.Android系统，更多情况下是OEM修改的Android系统，会在我们的context上调用getPackageName或者
            getOpPackageName等方法，然后将这个packageName跨进程传递做它用。系统的其他代码会以这个packageName
            去PackageManager中查询权限等信息。如果插件使用自己的包名，就需要在Context的getPackageName等实现中
            new Throwable()，然后判断调用来源以决定返回自己的包名还是插件的包名。但是如果保持采用宿主的包名，则没有
            这个烦恼。

            我们也可以始终认为Shadow App是宿主的扩展代码，使用是宿主的一部分，那么采用宿主的包名就是理所应当的了。
             */
            throw new ParsePluginApkException("插件和宿主包名不一致。宿主:"+hostAppContext.getPackageName()+" 插件:"+packageArchiveInfo.applicationInfo.packageName);
        }
        String partKey = loadParameters.partKey;
        PluginInfo pluginInfo = new PluginInfo(loadParameters.businessName,partKey,packageArchiveInfo.applicationInfo.packageName,packageArchiveInfo.applicationInfo.className);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pluginInfo.setAppComponentFactory(packageArchiveInfo.applicationInfo.appComponentFactory);
        }
        Log.e("cc.wang","ParsePluginApkBloc.parse.pluginInfo ActivityInfo"+packageArchiveInfo.activities);
        if(packageArchiveInfo.activities != null && packageArchiveInfo.activities.length > 0) {
            for (ActivityInfo a : packageArchiveInfo.activities) {
                pluginInfo.putActivityInfo(new PluginActivityInfo(a.name, a.getThemeResource(), a));
            }
        }
        Log.e("cc.wang","ParsePluginApkBloc.parse.pluginInfo services"+packageArchiveInfo.services);
        if(packageArchiveInfo.services != null && packageArchiveInfo.services.length > 0) {
            for (ServiceInfo a : packageArchiveInfo.services) {
                pluginInfo.putServiceInfo(new PluginServiceInfo(a.name));
            }
        }
        Log.e("cc.wang","ParsePluginApkBloc.parse.pluginInfo ProviderInfo"+packageArchiveInfo.providers);
        if(packageArchiveInfo.providers != null && packageArchiveInfo.providers.length > 0) {
            for (ProviderInfo a : packageArchiveInfo.providers) {
                pluginInfo.putPluginProviderInfo(new PluginProviderInfo(a.name, a.authority, a));
            }
        }
        Log.e("cc.wang","LoadPluginBloc.parse.pluginInfo 创建成功");
        return pluginInfo;
    }

}
