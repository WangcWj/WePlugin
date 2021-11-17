package cn.wang.weplugin.load;

import android.content.pm.ActivityInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginActivityInfo extends PluginComponentInfo{

    public String className;
    private int themeResource;
    private ActivityInfo activityInfo;

    public PluginActivityInfo(Parcel parcel) {
         this(parcel.readString(),parcel.readInt(),(ActivityInfo) parcel.readParcelable(ActivityInfo.class.getClassLoader()));
    }

    public PluginActivityInfo(String className, int themeResource, ActivityInfo activityInfo) {
        super(className);
        this.className = className;
        this.themeResource = themeResource;
        this.activityInfo = activityInfo;
    }

    public int getThemeResource() {
        return themeResource;
    }

    public ActivityInfo getActivityInfo() {
        return activityInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeInt(themeResource);
        dest.writeParcelable(activityInfo, flags);
    }

    public static Parcelable.Creator<PluginActivityInfo> CREATOR = new Parcelable.Creator<PluginActivityInfo>(){

        @Override
        public PluginActivityInfo createFromParcel(Parcel source) {
            return new PluginActivityInfo(source);
        }

        @Override
        public PluginActivityInfo[] newArray(int size) {
            return new PluginActivityInfo[size];
        }
    };

    @Override
    public String toString() {
        return "PluginActivityInfo{" +
                "className='" + className + '\'' +
                ", themeResource=" + themeResource +
                ", activityInfo=" + activityInfo +
                '}';
    }
}
