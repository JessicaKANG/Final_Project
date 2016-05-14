package com.Pineapple.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.Pineapple.Dao.model.Component;
import com.Pineapple.Dao.model.Computer;
import com.Pineapple.Dao.model.Order;
import com.Pineapple.Dao.model.Order_detial;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBAddorder implements DBConfig{
	private static int id_client;
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
     * 保存用户输入的订单信息
     * @param detiallist 
     *
     * @return：如果保存成功返回true，保存失败返回false
     */
    public static boolean save(Order order, List<Order_detial> detiallist) {
        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "insert into tb_order (id_order,price_order,date_order,state_order,id_client,payment,delivery) values (?, ?, ?, ?,?,?,?);";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        String sql2 = "select id_client from tb_client where name_client = '"+order.getClient()+"';";
        String sql3 = "insert into tb_order_detial(orderID,computerID,number,price,color,size,stock,memory,graphics,processor)"
        		+ "values(?,?,?,?,?,?,?,?,?,?);";
        try {
			 id_client = runner.query(conn, sql2, new ScalarHandler<Integer>());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}// 获得查询结果
        Object[] params = { order.getID(),order.getPrice(),order.getDatetime(),order.getState(),id_client,order.getPayment(),order.getDelivery()};// 获得传递的参数
        try {
            int result = runner.update(conn, sql, params);// 
            if( result >0){
            	Iterator iterator = detiallist.iterator();
            	while(iterator.hasNext()){
            		Order_detial detial = (Order_detial) iterator.next();
            		Object[] params3 = {detial.getorderID(),detial.getcomputerID(),detial.getnumber(),detial.getprice(),
            				detial.getcolor(),detial.getsize(),detial.getstock(),detial.getmemory(),detial.getgraphics(),
            				detial.getprocessor()};
            		int result3 = runner.update(conn,sql3,params3);
            	}
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

	public static List<Order> getActiveOrderList() {
		//先解决映射问题
		 Map<String, String> map = new HashMap<String, String>();
	   	 map.put("id_order", "id");
	   	 map.put("price_order", "price");
	   	 map.put("date_order", "datetime");
	   	 map.put("state_order", "state");
	   	 map.put("id_client", "client");
	   	 map.put("payment", "payment");
	   	 map.put("delivery", "delivery");
	   	 // 用构建好的HashMap建立一个BeanProcessor对象
	   	 BeanProcessor bean = new BeanProcessor(map);
	   	 RowProcessor processor = new BasicRowProcessor(bean);
	   	 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	        String sql = "select * from tb_order where state_order='"+"0"+"';";// 定义查询语句
	        Connection conn = getConnection();// 获得连接
	        ResultSetHandler<List<Order>> rsh = new BeanListHandler<Order>(Order.class,processor);// 创建结果集处理类
	        List<Order> result;
			try {
				result = (List<Order>)runner.query(conn, sql, rsh);
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 获得查询结果
            
		return null;
	}

	public static List<Order> getOrderList() {
		//先解决映射问题
		 Map<String, String> map = new HashMap<String, String>();
	   	 map.put("id_order", "id");
	   	 map.put("price_order", "price");
	   	 map.put("date_order", "datetime");
	   	 map.put("state_order", "state");
	   	 map.put("id_client", "client");
	   	 map.put("payment", "payment");
	   	 map.put("delivery", "delivery");
	   	 // 用构建好的HashMap建立一个BeanProcessor对象
	   	 BeanProcessor bean = new BeanProcessor(map);
	   	 RowProcessor processor = new BasicRowProcessor(bean);
	   	 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	        String sql = "select * from tb_order ";// 定义查询语句
	        Connection conn = getConnection();// 获得连接
	        ResultSetHandler<List<Order>> rsh = new BeanListHandler<Order>(Order.class,processor);// 创建结果集处理类
	        List<Order> result;
			try {
				result = (List<Order>)runner.query(conn, sql, rsh);
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 获得查询结果
           
		return null;
	}

	public static void ChangeOrderState(String id_order,String orderstate) {
		 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	     Connection conn = getConnection();// 获得连接
	     try {	    	 	
	   	     	String sql = "update tb_order set state_order = '"+orderstate+"'where id_order = '"+ id_order+"';";// 定义查询语句
	   	     	int q = runner.update(conn, sql);
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
	}

	public static List<Order> getClientActiveOrder(int id_client2) {
		//先解决映射问题
		 Map<String, String> map = new HashMap<String, String>();
	   	 map.put("id_order", "id");
	   	 map.put("price_order", "price");
	   	 map.put("date_order", "datetime");
	   	 map.put("state_order", "state");
	   	 map.put("id_client", "client");
	   	 map.put("payment", "payment");
	   	 map.put("delivery", "delivery");
	   	 // 用构建好的HashMap建立一个BeanProcessor对象
	   	 BeanProcessor bean = new BeanProcessor(map);
	   	 RowProcessor processor = new BasicRowProcessor(bean);
	   	 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	        String sql = "select * from tb_order where state_order<2 and id_client ='"+id_client2+"';";// 定义查询语句
	        Connection conn = getConnection();// 获得连接
	        ResultSetHandler<List<Order>> rsh = new BeanListHandler<Order>(Order.class,processor);// 创建结果集处理类
	        List<Order> result;
			try {
				result = (List<Order>)runner.query(conn, sql, rsh);
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 获得查询结果
		return null;
	}
	public static List<Order> getClientDeadOrder(int id_client2) {
		//先解决映射问题
		 Map<String, String> map = new HashMap<String, String>();
	   	 map.put("id_order", "id");
	   	 map.put("price_order", "price");
	   	 map.put("date_order", "datetime");
	   	 map.put("state_order", "state");
	   	 map.put("id_client", "client");
	   	 map.put("payment", "payment");
	   	 map.put("delivery", "delivery");
	   	 // 用构建好的HashMap建立一个BeanProcessor对象
	   	 BeanProcessor bean = new BeanProcessor(map);
	   	 RowProcessor processor = new BasicRowProcessor(bean);
	   	 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	        String sql = "select * from tb_order where state_order>1 and id_client ='"+id_client2+"';";// 定义查询语句
	        Connection conn = getConnection();// 获得连接
	        ResultSetHandler<List<Order>> rsh = new BeanListHandler<Order>(Order.class,processor);// 创建结果集处理类
	        List<Order> result;
			try {
				result = (List<Order>)runner.query(conn, sql, rsh);
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 获得查询结果
		return null;
	}
	public static void DeleteOrder(String id_order) {
		 QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	     Connection conn = getConnection();// 获得连接
	     try {	    	 	
	   	     	String sql = "delete from tb_order where id_order = '"+id_order+"';";// 定义查询语句
	   	     	int q = runner.update(conn, sql);
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
	}
	/**
	 * 返回订单细节链表，可用来库存补偿或显示订单细节
	 * @param id_order
	 * @return
	 */
	public static List<Order_detial> getDetiallist(String id_order) {
		QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select * from tb_order_detial where orderID = '"+id_order+"';";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        ResultSetHandler<List<Order_detial>> rsh = new BeanListHandler<Order_detial>(Order_detial.class);// 创建结果集处理类
        List<Order_detial> result;
		try {
			result = (List<Order_detial>)runner.query(conn, sql, rsh);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 获得查询结果
		return null;
	}

}
