package cn.wang.weplugin.load;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginServiceInfo extends PluginComponentInfo {

    public PluginServiceInfo(String className) {
        super(className);
    }

    public PluginServiceInfo(Parcel parcel) {
        super(parcel.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
    }

    public static Parcelable.Creator CREATOR = new Parcelable.Creator<PluginServiceInfo>() {

        @Override
        public PluginServiceInfo createFromParcel(Parcel source) {
            return new PluginServiceInfo(source);
        }

        @Override
        public PluginServiceInfo[] newArray(int size) {
            return new PluginServiceInfo[size];
        }
    };
}
