package cn.wang.weplugin.load;

import android.content.ContentProvider;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.wang.weplugin.runtime.UriConverter;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/1
 */
public class PluginContentProviderManager implements UriConverter.UriParseDelegate {

    public static final String CONTENT_PREFIX = "content://";
    public static final String SHADOW_BUNDLE_KEY = "shadow_cp_bundle_key";

    private Map<String, ContentProvider> providerMap = new HashMap<>();
    private Map<String, String> providerAuthorityMap = new HashMap<>();
    private Map<String, Set<PluginProviderInfo>> pluginProviderInfoMap = new HashMap<>();


    @Override
    public Uri parse(String uriString) {
        if (uriString.startsWith(CONTENT_PREFIX)) {
            String uriContent = uriString.substring(CONTENT_PREFIX.length());
            int index = uriContent.indexOf("/");
            String originalAuthority;
            if (index != -1) {
                originalAuthority = uriContent.substring(0, index);
            } else {
                originalAuthority = uriContent;
            }
            String containerAuthority = getContainerProviderAuthority(originalAuthority);
            if(null != containerAuthority){
                return Uri.parse(TextUtils.concat(CONTENT_PREFIX,containerAuthority,"/",uriContent).toString());
            }
        }

        return Uri.parse(uriString);
    }

    @Override
    public Uri parseCall(String uriString, Bundle extra) {
        Uri parse = parse(uriString);
        extra.putString(SHADOW_BUNDLE_KEY, parse.toString());
        return parse;
    }

    public void addContentProviderInfo(String partKey, PluginProviderInfo pluginProviderInfo, ContainerProviderInfo containerProviderInfo) {
        if (providerMap.containsKey(pluginProviderInfo.getAuthority())) {
            throw new RuntimeException("重复添加 ContentProvider");
        }
        providerAuthorityMap.put(pluginProviderInfo.getAuthority(), containerProviderInfo.getAuthority());
        Set<PluginProviderInfo> pluginProviderInfos;
        if (pluginProviderInfoMap.containsKey(partKey)) {
            pluginProviderInfos = pluginProviderInfoMap.get(partKey);
        } else {
            pluginProviderInfos = new HashSet<>();
        }
        pluginProviderInfos.add(pluginProviderInfo);
        pluginProviderInfoMap.put(partKey, pluginProviderInfos);
    }

    public void createContentProviderAndCallOnCreate(Context mContext, String partKey, PluginParts pluginParts) {
        Set<PluginProviderInfo> pluginProviderInfos = pluginProviderInfoMap.get(partKey);
        if(null == pluginProviderInfos || pluginProviderInfos.size() <= 0){
            return;
        }
        for (PluginProviderInfo info : pluginProviderInfos) {
            try {
                ContentProvider provider = pluginParts.getAppComponentFactory().instantiateProvider(pluginParts.getClassLoader(), info.className);
                provider.attachInfo(mContext, info.getProviderInfo());
                providerMap.put(info.getAuthority(), provider);
            } catch (Exception e) {
                throw new RuntimeException("partKey", e);
            }
        }

    }

    public String getContainerProviderAuthority(String pluginAuthority) {
        return providerAuthorityMap.get(pluginAuthority);
    }

    public ContentProvider getPluginContentProvider(String pluginAuthority) {
        return providerMap.get(pluginAuthority);
    }

    public Set<ContentProvider> getAllContentProvider() {
        Set<ContentProvider> contentProviders = new HashSet<>();
        Set<String> keySet = providerMap.keySet();
        for (String key : keySet) {
            contentProviders.add(providerMap.get(key));
        }
        return contentProviders;
    }

    public Uri convert2PluginUri(Uri uri) {
        String containerAuthority = uri.getAuthority();
        if (!providerAuthorityMap.values().contains(containerAuthority)) {
            throw new IllegalArgumentException("不能识别的uri Authority:" + containerAuthority);
        }
        String uriString = uri.toString();
        return Uri.parse(uriString.replace(containerAuthority + "/", ""));
    }

    public Uri convert2PluginUri(Bundle extra) {
        String uriString = extra.getString(SHADOW_BUNDLE_KEY);
        extra.remove(SHADOW_BUNDLE_KEY);
        return convert2PluginUri(Uri.parse(uriString));
    }


}
