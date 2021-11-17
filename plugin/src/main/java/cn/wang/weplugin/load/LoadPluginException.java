package cn.wang.weplugin.load;

import android.annotation.TargetApi;

import androidx.annotation.Nullable;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class LoadPluginException extends Exception {
    public LoadPluginException() {
    }

    public LoadPluginException(@Nullable String message) {
        super(message);
    }

    public LoadPluginException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public LoadPluginException(@Nullable Throwable cause) {
        super(cause);
    }

    @TargetApi(24)
    public LoadPluginException(@Nullable String message, @Nullable Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
