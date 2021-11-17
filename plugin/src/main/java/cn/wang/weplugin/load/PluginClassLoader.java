package cn.wang.weplugin.load;

import android.os.Build;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
class PluginClassLoader extends BaseDexClassLoader {

    private ClassLoader specialClassLoader;
    private List<String> allHostWhiteList;
    private ClassLoader loaderClassLoader = PluginClassLoader.class.getClassLoader();


    public PluginClassLoader(String dexPath, String librarySearchPath, File optimizedDirectory, ClassLoader parent, ClassLoader specialClassLoader,String[] w) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
        this.specialClassLoader = specialClassLoader;
        allHostWhiteList = new ArrayList<>(Arrays.asList(w));
    }

    @Override
    public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        if (specialClassLoader != null) {
            return super.loadClass(className, resolve);
        } else if (className.startsWith("com.tencent.shadow.core.runtime")) {
            return loaderClassLoader.loadClass(className);
        } else if (inPackage(className,allHostWhiteList)|| (Build.VERSION.SDK_INT < 28 && className.startsWith("org.apache.http"))){
            return super.loadClass(className,resolve);
        }else {
            Class<?> loadedClass = findLoadedClass(className);
            if(null == loadedClass){
                ClassNotFoundException exception = null;
                try {
                    loadedClass = findClass(className);
                }catch (ClassNotFoundException e){
                    exception = e;
                }
                if(null == loadedClass){
                    try {
                        loadedClass = specialClassLoader.loadClass(className);
                    }catch (ClassNotFoundException e){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            e.addSuppressed(exception);
                        }
                        throw e;
                    }
                }
            }
            return loadedClass;
        }
    }

    private String substringBeforeLast(String target, String place) {
        int index = target.lastIndexOf(place);
        if (index < 0) {
            return "";
        }
        return target.substring(0, index);
    }

    private boolean inPackage(String target, List<String> packageNames) {
        String packageName = substringBeforeLast(target, ".");

        for (String value : packageNames) {
            if (packageName.contains(value)) {
                return true;
            }
        }
        return false;
    }

}
