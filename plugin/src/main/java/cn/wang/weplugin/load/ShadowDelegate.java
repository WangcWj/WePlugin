package cn.wang.weplugin.load;

import android.content.res.Resources;

import cn.wang.weplugin.runtime.ShadowAppComponentFactory;
import cn.wang.weplugin.runtime.ShadowApplication;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public abstract class ShadowDelegate {
    private ShadowAppComponentFactory _appComponentFactory;
    private ShadowApplication _pluginApplication;
    private PluginClassLoader _pluginClassLoader;
    private Resources _pluginResources;
    private ComponentManager _componentManager;

    public final void inject( ShadowApplication shadowApplication) {

        this._pluginApplication = shadowApplication;
    }

    public final void inject( ShadowAppComponentFactory appComponentFactory) {

        this._appComponentFactory = appComponentFactory;
    }

    public final void inject( PluginClassLoader pluginClassLoader) {

        this._pluginClassLoader = pluginClassLoader;
    }

    public final void inject( Resources resources) {

        this._pluginResources = resources;
    }

    public final void inject( ComponentManager componentManager) {

        this._componentManager = componentManager;
    }

    
    protected final ShadowAppComponentFactory getMAppComponentFactory() {
        ShadowAppComponentFactory var10000 = this._appComponentFactory;
        return var10000;
    }

    
    protected final ShadowApplication getMPluginApplication() {
        ShadowApplication var10000 = this._pluginApplication;
        return var10000;
    }

    
    protected final PluginClassLoader getMPluginClassLoader() {
        PluginClassLoader var10000 = this._pluginClassLoader;
        return var10000;
    }

    
    protected final Resources getMPluginResources() {
        Resources var10000 = this._pluginResources;
        return var10000;
    }

    
    protected final ComponentManager getMComponentManager() {
        ComponentManager var10000 = this._componentManager;
        return var10000;
    }
}
