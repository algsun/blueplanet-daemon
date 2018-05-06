package com.microwise.msp.util;

import org.springframework.context.ApplicationContext;

/**
 * <pre>
 * 单例获取Spring容器
 * </pre>
 * 
 * @author heming
 * 
 */
public class AppContext {

	private static AppContext instance = new AppContext();

	private ApplicationContext appContext; //

	private AppContext() {
	}

    /**
     *
     * @return
     * @deprecated  换注解注入
     */
	public static AppContext getInstance() {
		return instance;
	}

    /**
     *
     * @return
     * @deprecated  换注解注入
     */
	public ApplicationContext getAppContext() {
		return appContext;
	}

    public void setAppContext(ApplicationContext appContext){
        this.appContext = appContext;
    }

}
