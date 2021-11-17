package cn.wang.weplugin.load;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public interface DI {

    void inject(ShadowDelegate delegate,String partKey);
}
