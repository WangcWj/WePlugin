/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.wang.weplugin.manager;

import android.app.Application;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import cn.wang.weplugin.common.InstalledApk;

import static cn.wang.weplugin.manager.FailedException.ERROR_CODE_FILE_NOT_FOUND_EXCEPTION;
import static cn.wang.weplugin.manager.FailedException.ERROR_CODE_RELOAD_LOADER_EXCEPTION;
import static cn.wang.weplugin.manager.FailedException.ERROR_CODE_RELOAD_RUNTIME_EXCEPTION;
import static cn.wang.weplugin.manager.FailedException.ERROR_CODE_RESET_UUID_EXCEPTION;
import static cn.wang.weplugin.manager.FailedException.ERROR_CODE_RUNTIME_EXCEPTION;
import static cn.wang.weplugin.manager.FailedException.ERROR_CODE_UUID_MANAGER_DEAD_EXCEPTION;
import static cn.wang.weplugin.manager.FailedException.ERROR_CODE_UUID_MANAGER_NULL_EXCEPTION;


public class PluginProcessService extends BasePluginProcessService {

    private final PpsBinder mPpsControllerBinder = new PpsBinder(this);

    static final ActivityHolder sActivityHolder = new ActivityHolder();

    public static Application.ActivityLifecycleCallbacks getActivityHolder() {
        return sActivityHolder;
    }

    public static PpsController wrapBinder(IBinder ppsBinder) {
        return new PpsController(ppsBinder);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mPpsControllerBinder;
    }

    private UuidManager mUuidManager;

    private PluginLoaderImpl mPluginLoader;

    private boolean mRuntimeLoaded = false;

    /**
     * 当前的Uuid。一旦设置不可修改。
     */
    private String mUuid = "";

    private void setUuid(String uuid) throws FailedException {
        if (mUuid.isEmpty()) {
            mUuid = uuid;
        } else if (!mUuid.equals(uuid)) {
            throw new FailedException(ERROR_CODE_RESET_UUID_EXCEPTION, "已设置过uuid==" + mUuid + "试图设置uuid==" + uuid);
        }
    }

    private void checkUuidManagerNotNull() throws FailedException {
        if (mUuidManager == null) {
            throw new FailedException(ERROR_CODE_UUID_MANAGER_NULL_EXCEPTION, "mUuidManager == null");
        }
    }

    void loadRuntime(String uuid) throws FailedException {
        checkUuidManagerNotNull();
        setUuid(uuid);
        if (mRuntimeLoaded) {
            throw new FailedException(ERROR_CODE_RELOAD_RUNTIME_EXCEPTION
                    , "重复调用loadRuntime");
        }
        try {
            InstalledApk installedApk;
            try {
                installedApk = mUuidManager.getRuntime(uuid);
            } catch (RemoteException e) {
                throw new FailedException(ERROR_CODE_UUID_MANAGER_DEAD_EXCEPTION, e.getMessage());
            } catch (NotFoundException e) {
                throw new FailedException(ERROR_CODE_FILE_NOT_FOUND_EXCEPTION, "uuid==" + uuid + "的Runtime没有找到。cause:" + e.getMessage());
            }

            InstalledApk installedRuntimeApk = new InstalledApk(installedApk.apkFilePath, installedApk.oDexPath, installedApk.libraryPath);
            boolean loaded = DynamicRuntime.loadRuntime(installedRuntimeApk);
            if (loaded) {
                DynamicRuntime.saveLastRuntimeInfo(this, installedRuntimeApk);
            }
            mRuntimeLoaded = true;
        } catch (RuntimeException e) {
            throw new FailedException(e);
        }
    }

    void loadPluginLoader(String uuid) throws FailedException {
        checkUuidManagerNotNull();
        setUuid(uuid);
        if (mPluginLoader != null) {
            throw new FailedException(ERROR_CODE_RELOAD_LOADER_EXCEPTION
                    , "重复调用loadPluginLoader");
        }
        try {
            PluginLoaderImpl pluginLoader = new LoaderImplLoader().load(uuid, getApplicationContext());
            pluginLoader.setUuidManager(mUuidManager);
            mPluginLoader = pluginLoader;
        } catch (RuntimeException e) {
            throw new FailedException(e);
        } catch (FailedException e) {
            throw e;
        } catch (Exception e) {
            String msg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            throw new FailedException(ERROR_CODE_RUNTIME_EXCEPTION, "加载动态实现失败 cause：" + msg);
        }
    }

    void setUuidManager(UuidManager uuidManager) {
        mUuidManager = uuidManager;
        if (mPluginLoader != null) {
            mPluginLoader.setUuidManager(uuidManager);
        }
    }

    void exit() {
        PluginProcessService.sActivityHolder.finishAll();
        System.exit(0);
        try {
            wait();
        } catch (InterruptedException ignored) {
        }
    }

    PpsStatus getPpsStatus() {
        return new PpsStatus(mUuid, mRuntimeLoaded, mPluginLoader != null, mUuidManager != null);
    }

    IBinder getPluginLoader() {
        return mPluginLoader;
    }
}
