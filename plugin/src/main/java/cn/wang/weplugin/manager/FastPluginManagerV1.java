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

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class FastPluginManagerV1 extends PluginManagerThatUseDynamicLoader {


    private ExecutorService mFixedPool = Executors.newFixedThreadPool(4);

    public FastPluginManagerV1(Context context) {
        super(context);
    }

    public void startPluginActivity(String uuid, String partKey, Intent pluginIntent) throws RemoteException, TimeoutException, FailedException {
        Intent intent = convertActivityIntent(uuid, partKey, pluginIntent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.e("cc.wang", "FastPluginManager.startPluginActivity.开始启动Activity ");
        //mPluginLoader是与PluginProcessService通讯。
        mPluginLoader.startActivityInPluginProcess(intent);

    }

    /**
     * step 1：启动承载插件的Service。
     * step 2：创建于Service的本地代理Binder对象BinderPluginLoader。
     * step 3: 创建承载插件的Application。
     *
     * @param uuid
     * @param partKey
     * @param pluginIntent
     * @return
     * @throws RemoteException
     * @throws TimeoutException
     * @throws FailedException
     */
    public Intent convertActivityIntent(String uuid, String partKey, Intent pluginIntent) throws RemoteException, TimeoutException, FailedException {
        loadPluginLoaderAndRuntime(uuid, partKey);
        mPluginLoader.callApplicationOnCreate(partKey);
        return mPluginLoader.convertActivityIntent(pluginIntent);
    }

    /**
     * 1.绑定插件进程服务。
     * 2.获取Service端加载插件的BinderPluginLoader。
     *
     * @param uuid
     * @param partKey
     * @throws RemoteException
     * @throws TimeoutException
     * @throws FailedException
     */
    private void loadPluginLoaderAndRuntime(String uuid, String partKey) throws RemoteException, TimeoutException, FailedException {
        if (mPpsController == null) {
            //Bind Service。
            bindPluginProcessService(getPluginProcessServiceName(partKey));
            //等待连接建立成功。
            waitServiceConnected(10, TimeUnit.SECONDS);
        }
        //loadRunTime(uuid);
        //通知PluginProcessService创建PluginLoaderBinder对象。
        loadPluginLoader(uuid);
    }

    protected abstract String getPluginProcessServiceName(String partKey);

}
