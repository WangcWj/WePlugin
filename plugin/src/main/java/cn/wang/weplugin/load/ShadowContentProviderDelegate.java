package cn.wang.weplugin.load;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.annotation.Nullable;
import java.io.FileNotFoundException;
import java.util.Set;
import cn.wang.weplugin.container.HostContentProviderDelegate;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public final class ShadowContentProviderDelegate extends ShadowDelegate implements HostContentProviderDelegate {
    private final PluginContentProviderManager mProviderManager;

    public ShadowContentProviderDelegate(PluginContentProviderManager mProviderManager) {
        super();
        this.mProviderManager = mProviderManager;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Set<ContentProvider> allContentProvider = mProviderManager.getAllContentProvider();
        for (ContentProvider it : allContentProvider) {
            it.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onLowMemory() {
        Set<ContentProvider> allContentProvider = mProviderManager.getAllContentProvider();
        for (ContentProvider it : allContentProvider) {
            it.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        Set<ContentProvider> allContentProvider = mProviderManager.getAllContentProvider();
        for (ContentProvider it : allContentProvider) {
            it.onTrimMemory(level);
        }
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    @Nullable
    public Cursor query(Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.query(pluginUri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    @Nullable
    public String getType(Uri uri) {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.getType(pluginUri);
    }

    @Override
    @Nullable
    public Uri insert(Uri uri, ContentValues values) {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.insert(pluginUri, values);
    }

    @Override
    public int delete(Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.delete(pluginUri, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.update(pluginUri, values, selection, selectionArgs);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.bulkInsert(pluginUri, values);
    }

    @Override
    @Nullable
    public Bundle call(String method, @Nullable String arg, Bundle extras) {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(extras);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.call(method, arg, extras);
    }

    @Override
    @Nullable
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.openFile(pluginUri, mode);
    }

    @Override
    @TargetApi(19)
    @Nullable
    public ParcelFileDescriptor openFile(Uri uri, String mode, @Nullable CancellationSignal signal) throws FileNotFoundException {
        Uri pluginUri = this.mProviderManager.convert2PluginUri(uri);
        ContentProvider var7 = mProviderManager.getPluginContentProvider(pluginUri.getAuthority());
        return var7.openFile(pluginUri, mode, signal);
    }
}
