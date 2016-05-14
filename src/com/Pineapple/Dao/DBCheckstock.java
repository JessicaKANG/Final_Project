package com.Pineapple.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.Pineapple.Dao.model.Component;
import com.Pineapple.Dao.model.Stock;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBCheckstock implements DBConfig{
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
     * 查询全部库存信息
     * SELECT * FROM tb_stock;
     * @return："list<JavaBean>"
     */
    public static List<Stock> select() {
    	//////////////////////////////////////////解决映射问题////////////////////////////////////////////
    	// BeanProcessor 有两个构造方法，可以传入一个HashMap集合
    	 // HashMap 规定了表字段映射到Javabean的哪个属性，即key为字段名称，value为对应的javabean属性
    	 // map.put(表字段名称, Javabean属性名称)
    	 Map<String, String> map = new HashMap<String, String>();
    	 map.put("id_stock", "id");
    	 map.put("id_computer", "idcpr");
    	 map.put("id_component", "idcpt");
    	 map.put("number_stock", "number");
    	 // 用构建好的HashMap建立一个BeanProcessor对象
    	 BeanProcessor bean = new BeanProcessor(map);
    	 RowProcessor processor = new BasicRowProcessor(bean);
    	 //Users rs = runner.query(sql, new BeanHandler<Users>(Users.class, processor));
    ///////////////////////////////////////////////////////////////////////////////////////////////////////	
    	
    	
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select * from tb_stock;";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        ResultSetHandler<List<Stock>> rsh = new BeanListHandler<Stock>(Stock.class,processor);// 创建结果集处理类
        try {
            List<Stock> result = (List<Stock>)runner.query(conn, sql, rsh);// 获得查询结果
          
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);// 关闭连接
        }
        return null;// 如果发生异常返回null
    }
    /**
     * 检查电脑库存
     * @param computerID
     * @param number 
     * @return
     */
    public static Boolean checkcpr(String computerID, Integer number){
    	Map<String, String> map = new HashMap<String, String>();
   	 	map.put("id_stock", "id");
   	 	map.put("id_computer", "idcpr");
   	 	map.put("id_component", "idcpt");
   	 	map.put("number_stock", "number");
   	 	// 用构建好的HashMap建立一个BeanProcessor对象
   	 	BeanProcessor bean = new BeanProcessor(map);
   	 	RowProcessor processor = new BasicRowProcessor(bean);
    	///////////////////////////////////////////////////////////////////////////
	   	 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	     String sql = "select number_stock from tb_stock where id_computer = '"+ computerID+"';";// 定义查询语句
	     Connection conn = getConnection();// 获得连接
	     try {	
	    	 	boolean checkexist = DBCheckstock.existscpr(computerID);
	    	 	if(checkexist){
	    	 		int result = runner.query(conn, sql, new ScalarHandler<Integer>());// 获得查询结果
		            if (result > number||result == number) {// 如果列表中存在数据
		                return true;// 返回true
		            } else {// 如果列表中没有数据
		                return false;// 返回false
		            }
	    	 	}else {
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
     * 修改电脑库存
     * @param computerID
     * @param number 
     */
    public static void modifycpr(String computerID, Integer number){
    	Map<String, String> map = new HashMap<String, String>();
   	 	map.put("id_stock", "id");
   	 	map.put("id_computer", "idcpr");
   	 	map.put("id_component", "idcpt");
   	 	map.put("number_stock", "number");
   	 	// 用构建好的HashMap建立一个BeanProcessor对象
   	 	BeanProcessor bean = new BeanProcessor(map);
   	 	RowProcessor processor = new BasicRowProcessor(bean);
   	 	////////////////////////////////////////////////////////////////////////
	   	 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	     String sql = "select number_stock from tb_stock where id_computer = '"+ computerID+"';";// 定义查询语句
	     Connection conn = getConnection();// 获得连接
	     //ResultSetHandler<Integer> rsh = new ColumnListHandler();// 创建结果集处理类
	     try {	    	 	
	    	 	int result = runner.query(conn, sql, new ScalarHandler<Integer>());// 获得查询结果
	            result = result - number;
	            QueryRunner runner2 = new QueryRunner();// 创建QueryRunner对象
	   	     	String sql2 = "update tb_stock set number_stock = '"+result+"'where id_computer = '"+ computerID+"';";// 定义查询语句
	   	     	Connection conn2 = getConnection();// 获得连接
	   	     	int q = runner2.update(conn2, sql2);
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
    }
    public static void modifybackcpr(String computerID, Integer number){
    	Map<String, String> map = new HashMap<String, String>();
   	 	map.put("id_stock", "id");
   	 	map.put("id_computer", "idcpr");
   	 	map.put("id_component", "idcpt");
   	 	map.put("number_stock", "number");
   	 	// 用构建好的HashMap建立一个BeanProcessor对象
   	 	BeanProcessor bean = new BeanProcessor(map);
   	 	RowProcessor processor = new BasicRowProcessor(bean);
   	 	////////////////////////////////////////////////////////////////////////
	   	 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	     String sql = "select number_stock from tb_stock where id_computer = '"+ computerID+"';";// 定义查询语句
	     Connection conn = getConnection();// 获得连接
	     //ResultSetHandler<Integer> rsh = new ColumnListHandler();// 创建结果集处理类
	     try {	    	 	
	    	 	int result = runner.query(conn, sql, new ScalarHandler<Integer>());// 获得查询结果
	            result = result + number;
	            QueryRunner runner2 = new QueryRunner();// 创建QueryRunner对象
	   	     	String sql2 = "update tb_stock set number_stock = '"+result+"'where id_computer = '"+ computerID+"';";// 定义查询语句
	   	     	Connection conn2 = getConnection();// 获得连接
	   	     	int q = runner2.update(conn2, sql2);
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
    }
    /**
     * 检查配件库存
     * @param componnentID,number
     * @return
     */
    public static Boolean checkcpt(String componentName,int number){
    	Map<String, String> map = new HashMap<String, String>();
   	 	map.put("id_stock", "id");
   	 	map.put("id_computer", "idcpr");
   	 	map.put("id_component", "idcpt");
   	 	map.put("number_stock", "number");
   	 	// 用构建好的HashMap建立一个BeanProcessor对象
   	 	BeanProcessor bean = new BeanProcessor(map);
   	 	RowProcessor processor = new BasicRowProcessor(bean);
    	///////////////////////////////////////////////////////////////////////////
	   	String componentID = null;
		 componentID = DBCheckcomponent.getComponentID(componentName);
   	 	QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	     String sql = "select number_stock from tb_stock where id_component = '"+ componentID+"';";// 定义查询语句
	     Connection conn = getConnection();// 获得连接	     
	     
	     try {	
	    	 		boolean checkexist = DBCheckstock.existscpt(componentID);
	    	 		if(checkexist){
	    	 			int result = runner.query(conn, sql, new ScalarHandler<Integer>());// 获得查询结果
	    	 			if (result > number||result==number) {// 如果列表中存在数据
	    	 				return true;// 返回true
	    	 			} else {// 如果列表中没有数据
	    	 				return false;// 返回false
	    	 			}
	    	 		}
	    	 		else{
	    	 			return false;// 返回false
	    	 		}	    	 	
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;// 如果发生异常返回false
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
	        
    }
    public static void modifycpt(String componentName, int number){
    	Map<String, String> map = new HashMap<String, String>();
   	 	map.put("id_stock", "id");
   	 	map.put("id_computer", "idcpr");
   	 	map.put("id_component", "idcpt");
   	 	map.put("number_stock", "number");
   	 	// 用构建好的HashMap建立一个BeanProcessor对象
   	 	BeanProcessor bean = new BeanProcessor(map);
   	 	RowProcessor processor = new BasicRowProcessor(bean);
   	 	////////////////////////////////////////////////////////////////////////
	   	String componentID = null;
	   	componentID = DBCheckcomponent.getComponentID(componentName);
	   	 QueryRunner runner1 = new QueryRunner();// 创建QueryRunner对象
	     String sql1 = "select number_stock from tb_stock where id_component = '"+ componentID+"';";// 定义查询语句
	     Connection conn = getConnection();// 获得连接
	     //ResultSetHandler<Integer> rsh1 = new ColumnListHandler();// 创建结果集处理类
	     try {	    	 	
	    	 	int result = runner1.query(conn, sql1, new ScalarHandler<Integer>());// 获得查询结果
	            result = result - number;
	            QueryRunner runner2 = new QueryRunner();// 创建QueryRunner对象
	   	     	String sql2 = "update tb_stock set number_stock = '"+result+"'where id_component = '"+ componentID+"';";// 定义查询语句
	   	     	//Connection conn2 = getConnection();// 获得连接
	   	     	int q = runner2.update(conn, sql2);
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
    }
    public static void modifybackcpt(String componentName, int number){
    	Map<String, String> map = new HashMap<String, String>();
   	 	map.put("id_stock", "id");
   	 	map.put("id_computer", "idcpr");
   	 	map.put("id_component", "idcpt");
   	 	map.put("number_stock", "number");
   	 	// 用构建好的HashMap建立一个BeanProcessor对象
   	 	BeanProcessor bean = new BeanProcessor(map);
   	 	RowProcessor processor = new BasicRowProcessor(bean);
   	 	////////////////////////////////////////////////////////////////////////
	   	String componentID = null;
	   	componentID = DBCheckcomponent.getComponentID(componentName);
	   	 QueryRunner runner1 = new QueryRunner();// 创建QueryRunner对象
	     String sql1 = "select number_stock from tb_stock where id_component = '"+ componentID+"';";// 定义查询语句
	     Connection conn = getConnection();// 获得连接
	     //ResultSetHandler<Integer> rsh1 = new ColumnListHandler();// 创建结果集处理类
	     try {	    	 	
	    	 	int result = runner1.query(conn, sql1, new ScalarHandler<Integer>());// 获得查询结果
	            result = result + number;
	            QueryRunner runner2 = new QueryRunner();// 创建QueryRunner对象
	   	     	String sql2 = "update tb_stock set number_stock = '"+result+"'where id_component = '"+ componentID+"';";// 定义查询语句
	   	     	//Connection conn2 = getConnection();// 获得连接
	   	     	int q = runner2.update(conn, sql2);
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
    }
    
    public static boolean existscpt(String componentID) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select id_component from tb_stock where id_component = '" + componentID + "';";// 定义查询语句
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
    
    public static boolean existscpr(String computerID) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select id_computer from tb_stock where id_computer = '" + computerID + "';";// 定义查询语句
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
}
