package com.microwise.msp.hardware.common;

import com.google.common.io.Closeables;
import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * properties文件加载类，可公用 传配置文件路径常量调getConfig方法 返回Configs后调get方法获取配置文件value
 * 
 * @author liuzhu
 * @date 2015-1-12
 * 
 */
public class ConfigFactory {

	// 配置文件池
	private Map<String, Configs> configPool = new HashMap<String, Configs>();

	// 静态工厂,负责生成创建配置文件对象
	private static ConfigFactory configFactory = new ConfigFactory();

	private ConfigFactory() {
	}

	public synchronized static ConfigFactory getInstance() {
		return configFactory;
	}

	/**
	 * 获取配置文件
	 * 
	 * @param key
	 *            配置文件路径常量
	 * @return Confugs 配置文件
	 */
	public Configs getConfig(String key) {
		Configs config = null;
		if (configPool.containsKey(key)) {
			config = configPool.get(key);
		} else {
			// 获得新配置文件
			config = new Configs(key);
			// 放入对象池
			configPool.put(key, config);
		}
		return config;
	}

	/**
	 * 获得已创建的配置文件数量
	 * 
	 * @return int 已创建配置文件数量
	 */
	public int getLoadConfigsTotal() {
		return configPool.size();
	}

	/**
	 * 配置文件加载类
	 */
	public class Configs {

		/** 配置文件对象 */
		private Properties properties;

		/**
		 * 构造函数
		 * 
		 * @param config
		 *            配置文件路径常量
		 */
		public Configs(String config) {
			try {
				// 加载 "config.properties" 到 properties.
				InputStream input = Resources.newInputStreamSupplier(
						Resources.getResource(config)).getInput();
				properties = new Properties();
				properties.load(input);
				Closeables.closeQuietly(input);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		/**
		 * 获取配置中 key 对应的 value
		 * 
		 * @param key
		 * @return 配置文件key对应值
		 */
		public String get(String key) {
			return properties.getProperty(key);
		}

	}

}