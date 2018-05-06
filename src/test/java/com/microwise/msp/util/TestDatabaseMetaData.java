/**
 * 
 */
package com.microwise.msp.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author he.ming
 * @since May 22, 2013
 * 
 */
public class TestDatabaseMetaData {

	private JdbcByPropertiesUtil jUtil = JdbcByPropertiesUtil.getInstance();

	public Properties getProperties() {
		Properties properties = JdbcByPropertiesUtil.readPropertiesFile();
		return properties;
	}

	/**
	 * 读取配置的数据库名称
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDataSourceName() throws Exception {
		Properties properties = this.getProperties();
		String dbName = properties.getProperty("dbName");
		return dbName;
	}

	/**
	 * 获取数据库中的表名称与视图名称
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getTablesAndViews() throws Exception {
		Connection connection = jUtil.getConnection();
		ResultSet rSet = null;
		List<String> list = new ArrayList<String>();

		try {
			Properties properties = this.getProperties();
			String schema = properties.get("username").toString();
			DatabaseMetaData metaData = connection.getMetaData();
			rSet = metaData.getTables(null, schema, null, new String[] {
					"TABLE", "VIEW" });
			while (rSet.next()) {
				String tableName = rSet.getString("TABLE_NAME");
				list.add(tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			jUtil.close(rSet, null, connection);
		}
		return list;
	}

	/**
	 * 利用表名和数据库用户名查询出该表的字段类型
	 * 
	 * @author he.ming
	 * @since May 22, 2013
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	public String generateBean() throws Exception {
		Connection connection = jUtil.getConnection();
		ResultSet rSet = null;
		String strJavaBean = "";
		String tableName = this.getDataSourceName();
		try {
			Properties properties = this.getProperties();
			String schema = properties.get("user").toString();
			DatabaseMetaData metaData = connection.getMetaData();
			rSet = metaData.getColumns(null, schema, tableName, null);
			Map<String, String> map = new HashMap<String, String>();
			while (rSet.next()) {
				String columnName = rSet.getString("COLUMN_NAME"); // 列名
				String dataType = rSet.getString("DATA_TYPE"); // 字段数据类型(对应java.sql.Types中的常量)
				String typeName = rSet.getString("TYPE_NAME"); // 字段类型名称（如varchar）
				map.put(columnName, dataType + ":" + typeName);
			}
			// 其他生成javabean的相关操作
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			jUtil.close(rSet, null, connection);
		}
		return strJavaBean;
	}
}
