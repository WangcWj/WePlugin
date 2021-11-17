package cn.wang.weplugin.load;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class PluginInfo {
    private   Set<PluginActivityInfo> mActivities;
    private  Set<PluginServiceInfo> mServices;
    private  Set<PluginProviderInfo> mProviders;
    @Nullable
    private String appComponentFactory;
    @Nullable
    private final String businessName;
   
    private final String partKey;
   
    private final String packageName;
    @Nullable
    private final String applicationClassName;

    public PluginInfo(@Nullable String businessName, String partKey, String packageName, @Nullable String applicationClassName) {
        super();
        this.businessName = businessName;
        this.partKey = partKey;
        this.packageName = packageName;
        this.applicationClassName = applicationClassName;
        this.mActivities = new HashSet<PluginActivityInfo>();
        this.mServices = new HashSet<PluginServiceInfo>();
        this.mProviders = new HashSet<PluginProviderInfo>();
    }

    public Set<PluginActivityInfo> getActivities() {
        return mActivities;
    }

    public Set<PluginProviderInfo> getProviders() {
        return mProviders;
    }

    public Set<PluginServiceInfo> getServices() {
        return mServices;
    }

    @Nullable
    public final String getAppComponentFactory() {
        return this.appComponentFactory;
    }

    public final void setAppComponentFactory(@Nullable String var1) {
        this.appComponentFactory = var1;
    }

    public final void putActivityInfo( PluginActivityInfo pluginActivityInfo) {
        this.mActivities.add(pluginActivityInfo);
    }

    public final void putServiceInfo( PluginServiceInfo pluginServiceInfo) {
        this.mServices.add(pluginServiceInfo);
    }

    public final void putPluginProviderInfo(PluginProviderInfo pluginProviderInfo) {
        this.mProviders.add(pluginProviderInfo);
    }

    @Nullable
    public final String getBusinessName() {
        return this.businessName;
    }

   
    public final String getPartKey() {
        return this.partKey;
    }

   
    public final String getPackageName() {
        return this.packageName;
    }

    @Nullable
    public final String getApplicationClassName() {
        return this.applicationClassName;
    }
    
}
