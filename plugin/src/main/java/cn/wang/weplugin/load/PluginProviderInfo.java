package cn.wang.weplugin.load;

import android.content.pm.ProviderInfo;
import android.os.Parcel;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginProviderInfo extends PluginComponentInfo{

    public String className;
    private String authority;
    private ProviderInfo providerInfo;

    public PluginProviderInfo(Parcel parcel) {
         this(parcel.readString(),parcel.readString(),(ProviderInfo) parcel.readParcelable(ProviderInfo.class.getClassLoader()));
    }

    public PluginProviderInfo(String className, String authority, ProviderInfo providerInfo) {
        super(className);
        this.className = className;
        this.authority = authority;
        this.providerInfo = providerInfo;
    }

    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public String getAuthority() {
        return authority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(className);
        parcel.writeString(authority);
        parcel.writeParcelable(providerInfo, flags);
    }

    public static Creator CREATOR = new Creator<PluginProviderInfo>(){

        @Override
        public PluginProviderInfo createFromParcel(Parcel source) {
            return new PluginProviderInfo(source);
        }

        @Override
        public PluginProviderInfo[] newArray(int size) {
            return new PluginProviderInfo[size];
        }
    };
}
