/**
 * 
 */
package com.microwise.msp.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author he.ming
 * @since May 22, 2013
 * 
 */
public class JdbcByPropertiesUtil {

	private static final String DRIVERCLASSNAME = "driverClassName";
	private static final String URL = "url";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	private static String filePath = "config.properties";
	private static JdbcByPropertiesUtil instance = null;

	/**
	 * 注册驱动，静态代码块，用于启动web服务器时加载驱动
	 */
	static {
		Properties properties = readPropertiesFile();
		String className = properties.getProperty(DRIVERCLASSNAME);
		try {
			Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private JdbcByPropertiesUtil() {

	}

	/**
	 * 单例
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @return
	 */
	public static JdbcByPropertiesUtil getInstance() {
		if (instance == null) {
			synchronized (JdbcByPropertiesUtil.class) {
				if (instance == null) {
					instance = new JdbcByPropertiesUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * 读取properties文件，数据库连接信息
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @return
	 */
	public static Properties readPropertiesFile() {
		String realFilePath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath()
				+ filePath;
		Properties properties = new Properties();
		try {
			InputStream iStream = new BufferedInputStream(new FileInputStream(
					realFilePath));
			properties.load(iStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 获取数据库连接
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @return
	 */
	public Connection getConnection() {
		Properties properties = readPropertiesFile();
		String url = properties.getProperty(URL);
		String username = properties.getProperty(USERNAME);
		String password = properties.getProperty(PASSWORD);
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 依次关闭ResultSet、Statement、Connection<br>
	 * 若对象不存在，则创建一个空对象
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public void close(ResultSet rs, Statement st, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (st != null) {
					try {
						st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 新增、修改、删除、查询记录（）
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @param sql
	 */
	public void execute(String sql) {
		JdbcByPropertiesUtil jUtil = getInstance();
		Connection connection = null;
		PreparedStatement pStatement = null;

		try {
			connection = jUtil.getConnection();
			connection.setAutoCommit(false);
			pStatement = connection.prepareStatement(sql);
			pStatement.execute();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			ResultSet rSet = null;
			jUtil.close(rSet, pStatement, connection);
		}
	}

	/**
	 * 新增、修改、删除记录
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @param sql
	 */
	public void executeUpdate(String sql) {
		JdbcByPropertiesUtil jUtil = getInstance();
		Connection connection = null;
		PreparedStatement pStatement = null;
		try {
			connection = jUtil.getConnection();
			connection.setAutoCommit(false);
			pStatement = connection.prepareStatement(sql);
			pStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			ResultSet rs = null;
			jUtil.close(rs, pStatement, connection);
		}
	}
}
