package cn.wang.weplugin.load;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Pair;

import cn.wang.weplugin.container.GeneratedHostActivityDelegator;
import cn.wang.weplugin.runtime.ShadowContext;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/11
 */
class ComponentManagerV1 implements ShadowContext.PluginComponentLauncher  {
    public static final String CM_LOADER_BUNDLE_KEY = "CM_LOADER_BUNDLE";
    public static final String CM_EXTRAS_BUNDLE_KEY = "CM_EXTRAS_BUNDLE";
    public static final String CM_ACTIVITY_INFO_KEY = "CM_ACTIVITY_INFO";
    public static final String CM_CLASS_NAME_KEY = "CM_CLASS_NAME";
    public static final String CM_CALLING_ACTIVITY_KEY = "CM_CALLING_ACTIVITY_KEY";
    public static final String CM_PACKAGE_NAME_KEY = "CM_PACKAGE_NAME";
    public static final String CM_BUSINESS_NAME_KEY = "CM_BUSINESS_NAME";
    public static final String CM_PART_KEY = "CM_PART";
    public static final String PACKAGE = "com.wang.weplugin.activitys";
    public static final String CLASSNAME = "PluginDefaultProxyActivity";


    private Context mHostAppContext;

    public ComponentManagerV1(Context mHostAppContext) {
        this.mHostAppContext = mHostAppContext;
    }

    @Override
    public boolean startActivity(ShadowContext shadowContext, Intent intent, Bundle options) {
        return false;
    }

    @Override
    public boolean startActivityForResult(GeneratedHostActivityDelegator delegator, Intent intent, int requestCode, Bundle option, ComponentName callingActivity) {
        return false;
    }

    @Override
    public Pair<Boolean, ComponentName> startService(ShadowContext context, Intent service) {
        return null;
    }

    @Override
    public Pair<Boolean, Boolean> stopService(ShadowContext context, Intent name) {
        return null;
    }

    @Override
    public Pair<Boolean, Boolean> bindService(ShadowContext context, Intent service, ServiceConnection conn, int flags) {
        return null;
    }

    @Override
    public Pair<Boolean, ?> unbindService(ShadowContext context, ServiceConnection conn) {
        return null;
    }

    @Override
    public Intent convertPluginActivityIntent(Intent pluginIntent) {
        Bundle bundle = new Bundle();




        return null;
    }

    /**
     * 将插件中的Activity转化成宿主中的。
     *
     * @param intent
     * @return
     */
    private Intent toContainerIntent(Intent intent) {
        Bundle bundle = new Bundle();
        //这个是
        ComponentName component = intent.getComponent();
        String className = component.getClassName();


return intent;

    }
}
