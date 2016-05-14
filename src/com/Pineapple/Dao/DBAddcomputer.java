package com.Pineapple.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.Pineapple.Dao.DBConfig;
import com.Pineapple.Dao.model.Computer;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBAddcomputer implements DBConfig{
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
     * 判断指定型号的商品是否存在
     *
     * @return：如果存在返回true，不存在或者查询失败返回false
     */
    public static boolean exists(String computerID) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select id_computer from tb_computer where id_computer = '" + computerID + "';";// 定义查询语句
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
     * 保存用户输入的商品信息
     *
     * @return：如果保存成功返回true，保存失败返回false
     */
    public static boolean save(Computer computer) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "insert into tb_computer (id_computer, name_computer, type_computer,price_computer,picture_computer) values (?, ?, ?, ?,?);";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        Object[] params = { computer.getId(), computer.getName(), computer.getType(),computer.getPrice(),computer.getPicture()};// 获得传递的参数
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
    
    /**
     * 保存用户输入的商品信息
     *
     * @return：如果保存成功返回true，保存失败返回false
     */
    public static boolean savecomputerconfig(String computerID, String componentID) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "insert into tb_computer_has_component (id_computer, id_component) values (?, ?);";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        Object[] params = { computerID,componentID};// 获得传递的参数
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

}
