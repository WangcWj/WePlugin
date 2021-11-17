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
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public final class DynamicPluginManager implements PluginManager {

    private PluginManagerUpdater mUpdater;
    private PluginManagerImpl mManagerImpl;
    private long mLastModified;

    public DynamicPluginManager() {

    }

    public DynamicPluginManager(PluginManagerUpdater updater) {
        if (updater.getLatest() == null) {
            throw new IllegalArgumentException("构造DynamicPluginManager时传入的PluginManagerUpdater" +
                    "必须已经已有本地文件，即getLatest()!=null");
        }
        mUpdater = updater;
    }

    @Override
    public void enter(Context context, long fromId, Bundle bundle, EnterCallback callback) {
        updateManagerImpl(context);
        Log.e("cc.wang", "DynamicPluginManager.enter." + mManagerImpl);
        mManagerImpl.enter(context, fromId, bundle, callback);
        if (null != mUpdater) {
            mUpdater.update();
        }
    }

    public void release() {
        if (mManagerImpl != null) {
            mManagerImpl.onDestroy();
            mManagerImpl = null;
        }
    }

    private void updateManagerImpl(Context context) {
        if (null != mUpdater) {
            File latestManagerImplApk = mUpdater.getLatest();
            long lastModified = latestManagerImplApk.lastModified();
            if (mLastModified != lastModified) {
                ManagerImplLoader implLoader = new ManagerImplLoader(context, latestManagerImplApk);
                PluginManagerImpl newImpl = implLoader.load();
                Bundle state;
                if (mManagerImpl != null) {
                    state = new Bundle();
                    mManagerImpl.onSaveInstanceState(state);
                    mManagerImpl.onDestroy();
                } else {
                    state = null;
                }
                newImpl.onCreate(state);
                mManagerImpl = newImpl;
                mLastModified = lastModified;
            }
        } else {
            Bundle state;
            if (mManagerImpl != null) {
                state = new Bundle();
                mManagerImpl.onSaveInstanceState(state);
                mManagerImpl.onDestroy();
            } else {
                state = null;
            }
            PluginManagerImpl newImpl = new SamplePluginManager(context);
            newImpl.onCreate(state);
            mManagerImpl = newImpl;
        }
    }

    public PluginManager getManagerImpl() {
        return mManagerImpl;
    }
}
