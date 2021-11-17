package cn.wang.weplugin.load;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager.LayoutParams;

import androidx.annotation.Nullable;

import cn.wang.weplugin.BuildConfig;
import cn.wang.weplugin.container.HostActivityDelegate;
import cn.wang.weplugin.container.HostActivityDelegator;
import cn.wang.weplugin.load.genera.GeneratedShadowActivityDelegate;
import cn.wang.weplugin.runtime.GeneratedPluginActivity;
import cn.wang.weplugin.runtime.MixResources;
import cn.wang.weplugin.runtime.PluginActivity;
import cn.wang.weplugin.runtime.ShadowActivity;
import cn.wang.weplugin.runtime.ShadowLayoutInflater;

import static cn.wang.weplugin.load.ComponentManager.CM_ACTIVITY_INFO_KEY;
import static cn.wang.weplugin.load.ComponentManager.CM_CLASS_NAME_KEY;
import static cn.wang.weplugin.load.ComponentManager.CM_EXTRAS_BUNDLE_KEY;
import static cn.wang.weplugin.load.ComponentManager.CM_LOADER_BUNDLE_KEY;
import static cn.wang.weplugin.load.ComponentManager.CM_PART_KEY;

public final class ShadowActivityDelegate extends GeneratedShadowActivityDelegate implements HostActivityDelegate {
    private HostActivityDelegator mHostActivityDelegator;
    private String mBusinessName;
    private String mPartKey;
    private Bundle mBundleForPluginLoader;
    private Bundle mRawIntentExtraBundle;
    private boolean mPluginActivityCreated;
    private boolean mDependenciesInjected;
    private boolean mRecreateCalled;
    private boolean mCallOnWindowAttributesChanged;
    private LayoutParams mBeforeOnCreateOnWindowAttributesChangedCalledParams;
    private MixResources mMixResources;
    private Configuration mCurrentConfiguration;
    private int mPluginHandleConfigurationChange;
    private ComponentName mCallingActivity;
    private final DI mDI;

    public ShadowActivityDelegate(DI mDI) {
        this.mDI = mDI;
    }

    public static final String PLUGIN_OUT_STATE_KEY = "PLUGIN_OUT_STATE_KEY";

    private final GeneratedPluginActivity getMPluginActivity() {
        return super.pluginActivity;
    }

    @Override
    public void setDelegator(HostActivityDelegator hostActivityDelegator) {
        this.mHostActivityDelegator = hostActivityDelegator;
    }


    @Override
    public Object getPluginActivity() {
        return getMPluginActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle pluginInitBundle = savedInstanceState;
        if (pluginInitBundle == null) {
            pluginInitBundle = mHostActivityDelegator.getIntent().getExtras();
        }
        this.mCallingActivity = (ComponentName) pluginInitBundle.getParcelable("CM_CALLING_ACTIVITY_KEY");
        this.mBusinessName = pluginInitBundle.getString("CM_BUSINESS_NAME", "");
        String partKey = pluginInitBundle.getString(CM_PART_KEY);
        mPartKey = partKey;
        mDI.inject(this, partKey);
        mDependenciesInjected = true;
        mMixResources = new MixResources(mHostActivityDelegator.superGetResources(), getMPluginResources());
        Bundle bundleForPluginLoader = pluginInitBundle.getBundle(CM_LOADER_BUNDLE_KEY);
        mBundleForPluginLoader = bundleForPluginLoader;
        if (null != bundleForPluginLoader) {
            bundleForPluginLoader.setClassLoader(this.getClassLoader());
        }
        String pluginActivityClassName = bundleForPluginLoader.getString(CM_CLASS_NAME_KEY);
        PluginActivityInfo pluginActivityInfo = bundleForPluginLoader.getParcelable(CM_ACTIVITY_INFO_KEY);
        mCurrentConfiguration = new Configuration(getResources().getConfiguration());
        mPluginHandleConfigurationChange = pluginActivityInfo.getActivityInfo().configChanges | ActivityInfo.CONFIG_SCREEN_SIZE | ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE | 0x20000000;
        if (savedInstanceState == null) {
            mRawIntentExtraBundle = pluginInitBundle.getBundle(CM_EXTRAS_BUNDLE_KEY);
            mHostActivityDelegator.getIntent().replaceExtras(mRawIntentExtraBundle);
        }
        mHostActivityDelegator.getIntent().setExtrasClassLoader(getMPluginClassLoader());
        try {
            ShadowActivity pluginActivity = getMAppComponentFactory().instantiateActivity(
                    getMPluginClassLoader(),
                    pluginActivityClassName,
                    mHostActivityDelegator.getIntent()
            );
            initPluginActivity(pluginActivity, pluginActivityInfo);
            super.pluginActivity = pluginActivity;
            //使PluginActivity替代ContainerActivity接收Window的Callback
            mHostActivityDelegator.getWindow().setCallback(pluginActivity);
            //设置插件AndroidManifest.xml 中注册的WindowSoftInputMode
            mHostActivityDelegator.getWindow().setSoftInputMode(pluginActivityInfo.getActivityInfo().softInputMode);
            if (mCallOnWindowAttributesChanged) {
                pluginActivity.onWindowAttributesChanged(mBeforeOnCreateOnWindowAttributesChangedCalledParams);
                mBeforeOnCreateOnWindowAttributesChangedCalledParams = null;
            }
            Bundle pluginSavedInstanceState = null;
            if (null != savedInstanceState) {
                pluginSavedInstanceState = savedInstanceState.getBundle(PLUGIN_OUT_STATE_KEY);
                pluginSavedInstanceState.setClassLoader(getMPluginClassLoader());
            }
            pluginActivity.onCreate(pluginSavedInstanceState);
            mPluginActivityCreated = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final void initPluginActivity(PluginActivity pluginActivity, PluginActivityInfo pluginActivityInfo) {
        pluginActivity.setHostActivityDelegator(mHostActivityDelegator);
        pluginActivity.setPluginResources(this.getMPluginResources());
        pluginActivity.setPluginClassLoader((ClassLoader) this.getMPluginClassLoader());
        pluginActivity.setPluginComponentLauncher(this.getMComponentManager());
        pluginActivity.setPluginApplication(this.getMPluginApplication());
        pluginActivity.setShadowApplication(this.getMPluginApplication());
        pluginActivity.setApplicationInfo(this.getMPluginApplication().getApplicationInfo());
        pluginActivity.setBusinessName(mBusinessName);
        pluginActivity.setCallingActivity(this.mCallingActivity);
        pluginActivity.setPluginPartKey(mPartKey);
        //前面的所有set方法都是PluginActivity定义的方法，
        //业务的Activity子类不会覆盖这些方法。调用它们不会执行业务Activity的任何逻辑。
        //最后这个setHostContextAsBase会调用插件Activity的attachBaseContext方法，
        //有可能会执行业务Activity覆盖的逻辑。
        //所以，这个调用要放在最后。
        pluginActivity.setHostContextAsBase((Context) mHostActivityDelegator.getHostActivity());
        pluginActivity.setTheme(pluginActivityInfo.getThemeResource());
    }


    @Override
    public String getLoaderVersion() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle pluginExtras = intent.getBundleExtra("CM_EXTRAS_BUNDLE");
        intent.replaceExtras(pluginExtras);
        this.getMPluginActivity().onNewIntent(intent);
    }

    @Override
    public boolean onNavigateUpFromChild(@Nullable Activity arg0) {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle pluginOutState = new Bundle((ClassLoader) this.getMPluginClassLoader());
        this.getMPluginActivity().onSaveInstanceState(pluginOutState);
        outState.putBundle(PLUGIN_OUT_STATE_KEY, pluginOutState);
        outState.putString(CM_PART_KEY, mPartKey);
        outState.putBundle(CM_LOADER_BUNDLE_KEY, mBundleForPluginLoader);
        if (this.mRecreateCalled) {
            outState.putBundle(CM_EXTRAS_BUNDLE_KEY, mHostActivityDelegator.getIntent().getExtras());
        } else {
            outState.putBundle(CM_EXTRAS_BUNDLE_KEY, this.mRawIntentExtraBundle);
        }
    }

    @Override
    public void onChildTitleChanged(@Nullable Activity arg0, @Nullable CharSequence arg1) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int diff = newConfig.diff(mCurrentConfiguration);
        if (diff == (diff & mPluginHandleConfigurationChange)) {
            getMPluginActivity().onConfigurationChanged(newConfig);
            mCurrentConfiguration = new Configuration(newConfig);
        } else {
            mHostActivityDelegator.superOnConfigurationChanged(newConfig);
            mHostActivityDelegator.recreate();
        }
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        Bundle pluginSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle(PLUGIN_OUT_STATE_KEY) : null;
        this.getMPluginActivity().onRestoreInstanceState(pluginSavedInstanceState);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        Bundle pluginSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle(PLUGIN_OUT_STATE_KEY) : null;
        this.getMPluginActivity().onPostCreate(pluginSavedInstanceState);
    }

    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        if (this.mPluginActivityCreated) {
            this.getMPluginActivity().onWindowAttributesChanged(params);
        } else {
            this.mBeforeOnCreateOnWindowAttributesChangedCalledParams = params;
        }
        this.mCallOnWindowAttributesChanged = true;
    }

    @Override
    public void onApplyThemeResource(Theme theme, int resid, boolean first) {
        mHostActivityDelegator.superOnApplyThemeResource(theme, resid, first);
        if (this.mPluginActivityCreated) {
            this.getMPluginActivity().onApplyThemeResource(theme, resid, first);
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return (ClassLoader) this.getMPluginClassLoader();
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        LayoutInflater systemService = (LayoutInflater) mHostActivityDelegator.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return ShadowLayoutInflater.build(systemService, getMPluginActivity(), mPartKey);
    }

    @Override
    public Resources getResources() {
        if (this.mDependenciesInjected) {
            return mMixResources;
        } else {
            //预期只有android.view.Window.getDefaultFeatures会调用到这个分支，此时我们还无法确定插件资源
            //而getDefaultFeatures只需要访问系统资源
            return Resources.getSystem();
        }
    }

    @Override
    public void recreate() {
        this.mRecreateCalled = true;
        mHostActivityDelegator.superRecreate();
    }
}
