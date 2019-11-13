package com.xyq.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *  @Description:
 *  @Author:xuyaqi
 *  @2018年10月18日
 */
public class DBUtil {
	/**
	 * 日志输出
	 */
	private static final Logger logger =  LoggerFactory.getLogger(DBUtil.class);
	
	private static final String PROXOOL_FILE_PATH = "config/proxool.xml";
	
	static {
		try {
			JAXPConfigurator.configure(PROXOOL_FILE_PATH, false);
		} catch (ProxoolException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection(String alias) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			conn = DriverManager.getConnection("proxool." + alias);
		} catch (ClassNotFoundException e) {
			logger.error("在proxool.xml中别名为" + alias + "的数据库连接失败", e);
			throw e;
		} catch (SQLException e) {
			logger.error("在proxool.xml中别名为" + alias + "的数据库连接失败", e);
			throw e;
		}
		if(conn == null){
			logger.info("未获取连接,请检查数据库连接关闭操作,或者适当增大最大连接数");
		}
		return conn;
	}

	/**
	 * 把连接放回连接池
	 * 
	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * 
	 * @decription：关闭 
	 * @author xuyaqi
	 * @date 2018年10月18日
	 * @param conn
	 * @param stmt
	 * @param rs
	 * @return void
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	
	/**
	 * @decription：更新数据
	 * @param connection
	 * @param sql
	 * @param params
	 * @param isClosed 是否关闭连接
	 * @return
	 * @throws SQLException
	 * @return int
	 */
	public static int update(Connection conn, String sql, Object params[]) throws SQLException {
		int n = 0;
		QueryRunner qRunner = new QueryRunner(true);
		n = qRunner.update(conn, sql, params);
		return n;
	}
	
	/**
	 * @decription：批量更新 
	 * @param conn
	 * @param sql
	 * @param paramsList
	 * @return
	 * @throws SQLException
	 * @return int
	 */
	public static int batch(Connection conn, String sql, List<Object[]> paramsList) throws SQLException {
		if (paramsList == null || paramsList.size() == 0) {
			return 0;
		}
		int listSize = paramsList.size();
		int arraySize = paramsList.get(0).length;
		Object[][] params = new Object[listSize][arraySize];
		for (int i = 0; i < listSize; i ++) {
			Object[] objs = paramsList.get(i);
			for (int j =0; j < arraySize; j ++) {
				params[i][j] = objs[j];
			}
		}
		return batch(conn, sql, params);
	}
	
	
	/**
	 * @decription：批量更新
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @return int
	 */
	public static int batch(Connection conn, String sql, Object params[][]) throws SQLException {
		int n = 0;
		QueryRunner qRunner = new QueryRunner(true);
		n = qRunner.batch(conn, sql, params).length;
		return n;
	}
	
	public static Object queryForObject(Connection conn, String sql, Object params[], Class<Object> clazz, boolean isClose)
			throws SQLException {
		Object obj = null;
		try {
			QueryRunner qRunner = new QueryRunner();
			obj = qRunner.query(conn, sql, new BeanHandler<Object>(clazz), params);
		} finally {
			if (isClose) {
				close(conn);
			}
		}
		return obj;
	}

	public static List<?> queryForOList(Connection conn, String sql, Object params[], Class<Object> clazz, boolean isClose)
			throws SQLException {
		List<?> list = null;
		try {
			QueryRunner qRunner = new QueryRunner();
			list = qRunner.query(conn, sql, new BeanListHandler<Object>(clazz), params);
		} finally {
			if (isClose) {
				close(conn);
			}
		}
		return list;
	}

	public static HashMap<?, ?> query4HashMapObject(Connection connection, String sql, Object params[]) throws SQLException {
		Connection conn = null;
		try {
			QueryRunner qRunner = new QueryRunner();

			ResultSetHandler<?> rsh = new ResultSetHandler<Object>() {
				@Override
				public Object handle(ResultSet rs) throws SQLException {
					HashMap<String, String> hashmap = new HashMap<String, String>();
					ResultSetMetaData meta = rs.getMetaData();
					int columnCount = meta.getColumnCount();
					String colName = null;
					String colValue = null;
					if (rs != null) {
						while (rs.next()) {
							for (int i = 1; i <= columnCount; i++) {
								colName = meta.getColumnLabel(i);
								colValue = rs.getString(i);
								hashmap.put(colName, colValue);
							}
							break;
						}
					}
					return hashmap;
				}
			};
			return (HashMap<?, ?>) qRunner.query(conn, sql, rsh, params);
		} finally {
			close(conn);
		}
	}
	/**
	 * @decription：LIst<Map<String, Object>> 
	 * @param conn
	 * @param sql
	 * @param params
	 * @param isClose
	 * @return
	 * @throws SQLException
	 * @return List<?>
	 */
	public static List<?> queryForList(Connection conn, String sql, Object params[], boolean isClose) throws SQLException {
		List<?> resultList = null;
		try {
			QueryRunner qRunner = new QueryRunner(true);

			ResultSetHandler<?> rsh = new ResultSetHandler<Object>() {
				@Override
				public Object handle(ResultSet rs) throws SQLException {
					@SuppressWarnings("rawtypes")
					List<Map> tempList = new ArrayList<Map>();
					ResultSetMetaData meta = rs.getMetaData();
					int columnCount = meta.getColumnCount();
					String colName = null;
					String colValue = null;
					if (rs != null) {
						while (rs.next()) {
							Map<String, String> map = new LinkedHashMap<String, String>();
							for (int i = 1; i <= columnCount; i++) {
								colName = meta.getColumnLabel(i);
								colValue = rs.getString(i);
								map.put(colName, colValue);
							}
							tempList.add(map);
						}
					}
					return tempList;
				}
			};
			resultList = (List<?>) qRunner.query(conn, sql, rsh, params);
		} finally {
			if (isClose) {
				close(conn);
			}
		}
		return resultList;
	}

	public static List<?> query4HashMapList(Connection connection, String sql, Object params[]) throws SQLException {
		QueryRunner qRunner = new QueryRunner(true);

		ResultSetHandler<?> rsh = new ResultSetHandler<Object>() {
			List<HashMap<String, String>> list = new LinkedList<HashMap<String, String>>();

			@Override
			public Object handle(ResultSet rs) throws SQLException {
				ResultSetMetaData meta = rs.getMetaData();
				int columnCount = meta.getColumnCount();
				String colName = null;
				String colValue = null;
				if (rs != null) {
					HashMap<String, String> hashmap = null;
					while (rs.next()) {
						hashmap = new HashMap<String, String>();
						for (int i = 1; i <= columnCount; i++) {
							colName = meta.getColumnLabel(i);
							colValue = rs.getString(i);
							hashmap.put(colName, colValue);
						}
						list.add(hashmap);
					}
				}
				return list;
			}
		};
		return (List<?>) qRunner.query(connection, sql, rsh, params);
	}
	
	public static int execute(String alias, String sql, List<String> warns) throws SQLException, ClassNotFoundException {
		return execute(getConnection(alias), sql, warns, true);
	}
	
	public static int execute(Connection conn, String sql, List<String> warns, boolean isClose) throws SQLException {
		PreparedStatement stat = null;
		int num = 0;
		try {
			stat = conn.prepareStatement(sql);
			num = stat.executeUpdate(sql);
			SQLWarning w = stat.getWarnings();
			while (w != null) {
				warns.add(w.getMessage());
				w = w.getNextWarning();
			}

		} finally {
			close(conn);
		}
		return num;
	}
	
	public static long get1row1col(Connection conn, String sql, Object params[]) throws SQLException {
		long count = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				count = rs.getLong(1); // 记录总数
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			if (conn != null) {
				close(conn);
			}
		}
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<List<Comparable>> getColumnNameAndType(Connection conn,String sql) {
		List<List<Comparable>> list = new ArrayList<List<Comparable>>();
		List<Comparable> columnName = new ArrayList<Comparable>();
		List<Comparable> columnType = new ArrayList<Comparable>();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				columnName.add(rsmd.getColumnName(i).toUpperCase());
				columnType.add(rsmd.getColumnType(i));
			}
		} catch (Exception e) {
			logger.error("查询表结构【" + sql + "】表结构存在异常,，异常原因：" + e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		list.add(columnName);
		list.add(columnType);
		return list;
	}
}
