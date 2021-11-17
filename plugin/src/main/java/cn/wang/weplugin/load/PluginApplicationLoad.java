package cn.wang.weplugin.load;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

import cn.wang.weplugin.runtime.ShadowApplication;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/11
 */
public class PluginApplicationLoad {

    private Context mHostAppContext;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private ComponentManagerV1 componentManager;


    public PluginApplicationLoad(Context hostAppContext) {
        this.mHostAppContext = hostAppContext;
        componentManager = new ComponentManagerV1(hostAppContext);
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

    private void realAction(String partKey) {
        ShadowApplication application =createShadowApplication();
        application.attachBaseContext(mHostAppContext);
        application.onCreate();
    }

    public ShadowApplication createShadowApplication() {
        SampleComponentManager componentManager = new SampleComponentManager(mHostAppContext);
        ShadowApplication application = new ShadowApplication();
        application.setPluginComponentLauncher(componentManager);
        application.setHostApplicationContextAsBase(mHostAppContext);
        return application;
    }


    private boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public Intent convertActivityIntent(Intent pluginActivityIntent) {
        return componentManager.convertPluginActivityIntent(pluginActivityIntent);
    }
}
