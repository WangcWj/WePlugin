package cn.wang.weplugin.load;

/**
 * Created to :
 *
 * @author cc.wang
 * @date 2021/2/26
 */
public class ContainerProviderInfo {
    private String className;
    private String authority;
    
    public final String getClassName() {
        return this.className;
    }

    public final void setClassName( String var1) {
        this.className = var1;
    }

    
    public final String getAuthority() {
        return this.authority;
    }

    public final void setAuthority( String var1) {
        this.authority = var1;
    }

    public ContainerProviderInfo( String className,  String authority) {
        super();
        this.className = className;
        this.authority = authority;
    }
}
