package com.github.wu.core.rpc.remoting.filter;

/**
 * filter作用域
 *
 * @author wangyongxu
 */
public enum FilterScope {

    /**
     * 作用于client
     */
    CLIENT,
    /**
     * 作用于server
     */
    SERVER;


    public static boolean client(FilterScope[] scopes) {
        boolean support = false;
        for (FilterScope scope : scopes) {
            if (scope == FilterScope.CLIENT) {
                support = true;
                break;
            }
        }
        return support;
    }

    public static boolean server(FilterScope[] scopes) {
        boolean support = false;
        for (FilterScope scope : scopes) {
            if (scope == FilterScope.SERVER) {
                support = true;
                break;
            }
        }
        return support;
    }


}
