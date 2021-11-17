package cn.wang.weplugin.load;

import android.os.Build;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/3/8
 */
public class CombineClassLoader extends ClassLoader {
    private final ClassLoader[] classLoaders;
    public CombineClassLoader( ClassLoader[] classLoaders, ClassLoader parent) {
        super(parent);
        this.classLoaders = classLoaders;
    }

    @Override
    protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c = this.findLoadedClass(name);
        ClassNotFoundException classNotFoundException = new ClassNotFoundException(name);
        if (c == null) {
            try {
                c = super.loadClass(name, resolve);
            } catch (ClassNotFoundException var11) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    classNotFoundException.addSuppressed((Throwable)var11);
                }
            }
            if (c == null) {
                for (ClassLoader classLoader:classLoaders) {
                    try {
                        c = classLoader.loadClass(name);
                    }catch (ClassNotFoundException e){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            classNotFoundException.addSuppressed(e);
                        }
                    }
                }
                if (c == null) {
                    throw classNotFoundException;
                }
            }
        }
        return c;
    }
}
