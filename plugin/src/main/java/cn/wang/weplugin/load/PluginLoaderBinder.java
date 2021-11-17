package cn.wang.weplugin.load;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import java.util.Map;

import cn.wang.weplugin.manager.PluginLoader;
import cn.wang.weplugin.manager.PluginLoaderImpl;
import cn.wang.weplugin.manager.UuidManager;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginLoaderBinder extends Binder implements PluginLoaderImpl {

    private DynamicPluginLoader mDynamicPluginLoader;

    public PluginLoaderBinder(DynamicPluginLoader mDynamicPluginLoader) {
        this.mDynamicPluginLoader = mDynamicPluginLoader;
    }

    @Override
    public void setUuidManager(UuidManager uuidManager) {
        mDynamicPluginLoader.setUuidManager(uuidManager);
    }

    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case IBinder.INTERFACE_TRANSACTION:
                reply.writeString(PluginLoader.DESCRIPTOR);
                return true;
            case PluginLoader.TRANSACTION_loadPlugin:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                mDynamicPluginLoader.loadPlugin(data.readString());
                reply.writeNoException();
                return true;
            case PluginLoader.TRANSACTION_getLoadedPlugin:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                Map<String, Boolean> plugin = mDynamicPluginLoader.getLoadedPlugin();
                reply.writeNoException();
                reply.writeMap(plugin);
                return true;
            case PluginLoader.TRANSACTION_callApplicationOnCreate:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                mDynamicPluginLoader.callApplicationOnCreate(data.readString());
                reply.writeNoException();
                return true;
            case PluginLoader.TRANSACTION_convertActivityIntent:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                Intent parcel = null;
                if (0 != data.readInt()) {
                    parcel = Intent.CREATOR.createFromParcel(data);
                }
                Intent intent = mDynamicPluginLoader.convertActivityIntent(parcel);
                reply.writeNoException();
                if (intent != null) {
                    reply.writeInt(1);
                    intent.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case PluginLoader.TRANSACTION_startPluginService:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                Intent si = null;
                if (0 != data.readInt()) {
                    si = Intent.CREATOR.createFromParcel(data);
                }
                ComponentName componentName = mDynamicPluginLoader.startPluginService(si);
                reply.writeNoException();
                if (componentName != null) {
                    reply.writeInt(1);
                    componentName.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case PluginLoader.TRANSACTION_stopPluginService:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                Intent ssi = null;
                if (0 != data.readInt()) {
                    ssi = Intent.CREATOR.createFromParcel(data);
                }
                boolean stopPluginService = mDynamicPluginLoader.stopPluginService(ssi);
                reply.writeNoException();
                reply.writeInt(stopPluginService ? 1 : 0);
                return true;
            case PluginLoader.TRANSACTION_bindPluginService:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                Intent bsi = null;
                if (0 != data.readInt()) {
                    bsi = Intent.CREATOR.createFromParcel(data);
                }
                BinderPluginServiceConnection bpsc = new BinderPluginServiceConnection(data.readStrongBinder());
                boolean bpResult = mDynamicPluginLoader.bindPluginService(bsi, bpsc, data.readInt());
                reply.writeNoException();
                reply.writeInt(bpResult ? 1 : 0);
                return true;
            case PluginLoader.TRANSACTION_unbindService:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                mDynamicPluginLoader.unbindService(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case PluginLoader.TRANSACTION_startActivityInPluginProcess:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                mDynamicPluginLoader.startActivityInPluginProcess(Intent.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }
}
