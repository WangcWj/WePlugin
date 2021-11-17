package cn.wang.weplugin.manager;


import cn.wang.weplugin.common.InstalledApk;

public interface UuidManagerImpl {
    InstalledApk getPlugin(String uuid, String partKey) throws NotFoundException, FailedException;

    InstalledApk getPluginLoader(String uuid) throws NotFoundException, FailedException;

    InstalledApk getRuntime(String uuid) throws NotFoundException, FailedException;
}
