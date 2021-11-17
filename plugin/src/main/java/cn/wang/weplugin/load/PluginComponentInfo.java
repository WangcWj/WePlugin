package cn.wang.weplugin.load;

import android.os.Parcelable;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public abstract class PluginComponentInfo implements Parcelable {
    protected String className;

    public PluginComponentInfo(String className) {
        this.className = className;
    }
}
