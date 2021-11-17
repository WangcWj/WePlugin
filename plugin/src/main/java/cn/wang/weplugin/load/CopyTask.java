package cn.wang.weplugin.load;

import android.content.Context;
import android.os.Environment;


import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created to : 将插件APK压缩包，复制解压到SD卡中。
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class CopyTask {

    /**
     * 动态加载的插件管理apk
     */
    public final static String sPluginManagerName = "sample-manager-debug.apk";

    /**
     * 动态加载的插件包，里面包含以下几个部分，插件apk，插件框架apk（loader apk和runtime apk）, apk信息配置关系json文件
     */
    public final static String sPluginZip = "wangdemo.zip";

    public File pluginManagerFile;

    public File pluginZipFile;

    public File loadApkFile;

    public ExecutorService singlePool = Executors.newSingleThreadExecutor();

    private Context mContext;

    private static CopyTask sInstance = new CopyTask();

    public static CopyTask getInstance() {
        return sInstance;
    }

    private CopyTask() {
    }

    public void init(Context context) {

        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        pluginManagerFile = new File(externalStoragePublicDirectory, "wang/" + sPluginManagerName);
        pluginZipFile = new File(externalStoragePublicDirectory, "wang/" + sPluginZip);
        loadApkFile = new File(externalStoragePublicDirectory, "wang/" + "sample-loader-debug.apk");
        mContext = context.getApplicationContext();
        delectedFile(pluginManagerFile);
        delectedFile(pluginZipFile);
        singlePool.execute(new Runnable() {
            @Override
            public void run() {
                preparePlugin();
            }
        });

    }

    private void delectedFile(File file) {
        if (null != file && file.exists()) {
            file.delete();
        }
        File parentFile = file.getParentFile();
        if (null != parentFile && parentFile.exists()) {
            parentFile.delete();
        }
        parentFile.mkdirs();
    }

    private void preparePlugin() {
        try {
            InputStream is = mContext.getAssets().open(sPluginManagerName);
            FileUtils.copyInputStreamToFile(is, pluginManagerFile);

            InputStream zip = mContext.getAssets().open(sPluginZip);
            FileUtils.copyInputStreamToFile(zip, pluginZipFile);

            InputStream apk = mContext.getAssets().open("sample-loader-debug.apk");
            FileUtils.copyInputStreamToFile(apk, loadApkFile);
            Log.e("cc.wang", "PluginHelper.preparePlugin." + pluginZipFile);
        } catch (IOException e) {
            Log.e("cc.wang","PluginHelper.preparePlugin."+e);
            throw new RuntimeException("从assets中复制apk出错", e);
        }
    }

}
