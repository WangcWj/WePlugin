package cn.wang.weplugin.load;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import cn.wang.weplugin.manager.PluginLoader;
import cn.wang.weplugin.manager.PluginLoaderImpl;
import cn.wang.weplugin.manager.UuidManager;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginLoaderBinderV1 extends Binder implements PluginLoaderImpl {

    private PluginApplicationLoad applicationLoad;

    public PluginLoaderBinderV1(PluginApplicationLoad applicationLoad) {
        this.applicationLoad = applicationLoad;
    }

    @Override
    public void setUuidManager(UuidManager uuidManager) {

    }

    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case IBinder.INTERFACE_TRANSACTION:
                reply.writeString(PluginLoader.DESCRIPTOR);
                return true;
            case PluginLoader.TRANSACTION_callApplicationOnCreate:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                applicationLoad.callApplicationOnCreate(data.readString());
                reply.writeNoException();
                return true;
            case PluginLoader.TRANSACTION_convertActivityIntent:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                Intent parcel = null;
                if (0 != data.readInt()) {
                    parcel = Intent.CREATOR.createFromParcel(data);
                }
                Intent intent = applicationLoad.convertActivityIntent(parcel);
                reply.writeNoException();
                if (intent != null) {
                    reply.writeInt(1);
                    intent.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                } else {
                    reply.writeInt(0);
                }
                return true;
           /* case PluginLoader.TRANSACTION_startPluginService:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                Intent si = null;
                if (0 != data.readInt()) {
                    si = Intent.CREATOR.createFromParcel(data);
                }
                ComponentName componentName = applicationLoad.startPluginService(si);
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
                boolean stopPluginService = applicationLoad.stopPluginService(ssi);
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
                boolean bpResult = applicationLoad.bindPluginService(bsi, bpsc, data.readInt());
                reply.writeNoException();
                reply.writeInt(bpResult ? 1 : 0);
                return true;
            case PluginLoader.TRANSACTION_unbindService:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                applicationLoad.unbindService(data.readStrongBinder());
                reply.writeNoException();
                return true;*/
            case PluginLoader.TRANSACTION_startActivityInPluginProcess:
                data.enforceInterface(PluginLoader.DESCRIPTOR);
                //applicationLoad.startActivityInPluginProcess(Intent.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }
}
