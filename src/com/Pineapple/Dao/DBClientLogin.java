package com.Pineapple.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringEscapeUtils;

import com.Pineapple.Dao.DBConfig;
import com.Pineapple.Dao.model.Client;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBClientLogin implements DBConfig{
	/**
     * 使用MySQL数据源获得数据库连接对象
     *
     * @return：MySQL连接对象，如果获得失败返回null
     */
    public static Connection getConnection() {
        MysqlDataSource mds = new MysqlDataSource();// 创建MySQL数据源（此方法来自于mysql的插件）
        //与DBConfig接口对接
        mds.setDatabaseName(databaseName);// 设置数据库名称
        mds.setUser(username);// 设置数据库用户名
        mds.setPassword(password);// 设置数据库密码
        try {
            return mds.getConnection();// 获得连接（返回值是一个数据库连接对象）
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return null;// 如果获取失败就返回null
    }
    /**
     * 判断指定用户名的用户是否存在
     *
     * @return：如果存在返回true，不存在或者查询失败返回false
     */
    public static boolean exists(String username) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select id_client from tb_client where name_client = '" + username + "';";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        ResultSetHandler<List<Object>> rsh = new ColumnListHandler();// 创建结果集处理类
        try {
            List<Object> result = runner.query(conn, sql, rsh);// 获得查询结果
            if (result.size() > 0) {// 如果列表中存在数据
                return true;// 返回true
            } else {// 如果列表中没有数据
                return false;// 返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);// 关闭连接
        }
        return false;// 如果发生异常返回false
    }
    /**
     * 验证用户名和密码是否正确 使用Commons Lang组件转义字符串避免SQL注入
     *
     * @return：如果正确返回true，错误返回false
     */
    public static boolean check(String username, String password) {
        username = StringEscapeUtils.escapeSql(username);// 将用户输入的用户名转义
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select psd_client from tb_client where name_client = '" + username + "';";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        ResultSetHandler<Object> rsh = new ScalarHandler();// 创建结果集处理类
        try {
            String result = (String) runner.query(conn, sql, rsh);// 获得查询结果
          
            if (result.equals(password)) {// 如果密码相同则返回true
                
                return true;
            } else {// 如果密码不同则返回false
                
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);// 关闭连接
        }
        return false;// 如果发生异常返回false
    }
    /**
     * 保存用户输入的注册信息
     *
     * @return：如果保存成功返回true，保存失败返回false
     */
    public static boolean save(Client user) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "insert into tb_client (name_client, psd_client, email_client) values (?, ?, ?);";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        Object[] params = { user.getUsername(), user.getPassword(), user.getEmail() };// 获得传递的参数
        try {
            int result = runner.update(conn, sql, params);// 保存用户
            if (result > 0) {// 如果保存成功返回true
                return true;
            } else {// 如果保存失败返回false
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);// 关闭连接
        }
        return false;// 如果发生异常返回false
    }
    
    public static String getEmail(String username){
    	QueryRunner runner = new QueryRunner();// 创建QueryRunner对象       
        Connection conn = getConnection();// 获得连接
        String sql = "select email_client from tb_client where name_client = '"+username+"';";
        try {
			 String email = runner.query(conn, sql, new ScalarHandler<String>());
			 return email;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}// 获得查询结果
    }
	public static int getClientID(String name_client) {
		QueryRunner runner = new QueryRunner();// 创建QueryRunner对象       
        Connection conn = getConnection();// 获得连接
        String sql = "select id_client from tb_client where name_client = '"+name_client+"';";
        try {
			 int id_client = (int) runner.query(conn, sql, new ScalarHandler<Integer>());
			 return id_client;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return 0;
		}// 获得查询结果
	}

}
