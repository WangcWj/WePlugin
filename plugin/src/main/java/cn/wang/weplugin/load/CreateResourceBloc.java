package cn.wang.weplugin.load;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import java.util.concurrent.CountDownLatch;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class CreateResourceBloc {

    public static Resources create(PackageInfo packageArchiveInfo, String archiveFilePath, final Context hostAppContext) {
        //先用宿主context初始化一个WebView，以便WebView的逻辑去修改sharedLibraryFiles，将webview.apk添加进去
        final CountDownLatch latch = new CountDownLatch(1);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new WebView(hostAppContext);
                latch.countDown();
            }
        });
        try {
            latch.await();
            PackageManager packageManager = hostAppContext.getPackageManager();
            packageArchiveInfo.applicationInfo.publicSourceDir = archiveFilePath;
            packageArchiveInfo.applicationInfo.sourceDir = archiveFilePath;
            packageArchiveInfo.applicationInfo.sharedLibraryFiles = hostAppContext.getApplicationInfo().sharedLibraryFiles;
            Resources resourcesForApplication = packageManager.getResourcesForApplication(packageArchiveInfo.applicationInfo);
            Log.e("cc.wang","CreateResourceBloc.create.Resource 创建成功");
            return resourcesForApplication;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
