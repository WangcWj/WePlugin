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

import java.lang.reflect.Field;
import cn.wang.weplugin.common.InstalledApk;
import dalvik.system.DexClassLoader;

abstract class ImplLoader {
    private static final String WHITE_LIST_CLASS_NAME = "com.tencent.shadow.dynamic.impl.WhiteList";
    private static final String WHITE_LIST_FIELD_NAME = "sWhiteList";

    abstract String[] getCustomWhiteList();

    String[] loadWhiteList(InstalledApk installedApk) {
        DexClassLoader dexClassLoader = new DexClassLoader(
                installedApk.apkFilePath,
                installedApk.oDexPath,
                installedApk.libraryPath,
                getClass().getClassLoader()
        );

        String[] whiteList = null;
        try {
            Class<?> whiteListClass = dexClassLoader.loadClass(WHITE_LIST_CLASS_NAME);
            Field whiteListField = whiteListClass.getDeclaredField(WHITE_LIST_FIELD_NAME);
            Object o = whiteListField.get(null);
            whiteList = (String[]) o;
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String[] interfaces;
        if (whiteList != null) {
            interfaces = concatenate(getCustomWhiteList(), whiteList);
        } else {
            interfaces = getCustomWhiteList();
        }
        return interfaces;
    }

    private static String[] concatenate(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c = new String[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}
