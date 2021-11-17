package cn.wang.weplugin.load;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import cn.wang.weplugin.manager.PluginServiceConnection;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public final class BinderPluginServiceConnection {

    private final IBinder mRemote;

    public IBinder getRemote() {
        return mRemote;
    }

    public BinderPluginServiceConnection(IBinder mRemote) {
        super();
        this.mRemote = mRemote;
    }

    public final void onServiceConnected(@Nullable ComponentName name, IBinder service) throws RemoteException {
        Parcel var10000 = Parcel.obtain();
        Parcel _data = var10000;
        var10000 = Parcel.obtain();
        Parcel _reply = var10000;

        try {
            _data.writeInterfaceToken(PluginServiceConnection.DESCRIPTOR);
            if (name != null) {
                _data.writeInt(1);
                name.writeToParcel(_data, 0);
            } else {
                _data.writeInt(0);
            }

            _data.writeStrongBinder(service);
            this.mRemote.transact(PluginServiceConnection.TRANSACTION_onServiceConnected, _data, _reply, 0);
            _reply.readException();
        } finally {
            _reply.recycle();
            _data.recycle();
        }

    }

    public final void onServiceDisconnected(@Nullable ComponentName name) throws RemoteException {
        Parcel var10000 = Parcel.obtain();
        Parcel _data = var10000;
        var10000 = Parcel.obtain();
        Parcel _reply = var10000;

        try {
            _data.writeInterfaceToken(PluginServiceConnection.DESCRIPTOR);
            if (name != null) {
                _data.writeInt(1);
                name.writeToParcel(_data, 0);
            } else {
                _data.writeInt(0);
            }
            this.mRemote.transact(PluginServiceConnection.TRANSACTION_onServiceDisconnected, _data, _reply, 0);
            _reply.readException();
        } finally {
            _reply.recycle();
            _data.recycle();
        }

    }

}
