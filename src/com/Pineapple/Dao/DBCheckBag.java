package com.Pineapple.Dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.Pineapple.Dao.model.Bag;
import com.Pineapple.Dao.model.Order;
import com.Pineapple.Dao.model.Order_detial;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DBCheckBag implements DBConfig{
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

	public static boolean addtoBag(int id_client, List<Bag> baglist) {
		QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        Connection conn = getConnection();// 获得连接
        Iterator iterator = baglist.iterator();
         
        while(iterator.hasNext()){
        	int number = 1;
        	int ID = 0;
        	Bag bag = (Bag) iterator.next();
        	String computerID = bag.getcomputerID();
        	double price = bag.getprice();
        	String color = bag.getcolor();
        	String size = bag.getsize();
        	String stock = bag.getstock();
        	String memory = bag.getmemory();
        	String graphics = bag.getgraphics();
        	String processor = bag.getprocessor();        	      	
	        String sql0 = "select ID from tb_bag where clientID ='"+id_client+"' and computerID ='"+computerID+"'"
	        		+ " and price = '" + price + "'"
	        		+ " and color ='"+ color +"' and size ='"+ size+"' and stock ='"+stock+"' and "
	        				+ "memory ='"+memory+"' and graphics ='"+graphics+"' and processor ='"+ processor+"';";// 定义查询语句
	        String sql1 = "insert into tb_bag(clientID,computerID,number,price,color,size,stock,memory,graphics,processor)"
	        		+ "values(?,?,?,?,?,?,?,?,?,?);";
	        ResultSetHandler<List<Object>> rsh = new ColumnListHandler();// 创建结果集处理类
	        Object[] params = { id_client,computerID,number,price,color,size,stock,memory,graphics,processor};
	        try {
				List<Object> result = runner.query(conn, sql0, rsh);
				if (result.size() > 0) {// 如果列表中存在数据
					ID = runner.query(conn,sql0,new ScalarHandler<Integer>());
					System.out.println("bag ID is:"+ID);
					number = runner.query(conn,"select number from tb_bag where ID ='"+ID+"' ;" , new ScalarHandler<Integer>());// 获得查询结果
					System.out.println("number of computer is: "+number);
					number = number+1;
					int q = runner.update(conn, "update tb_bag set number ='"+number+"' where ID = '"+ ID+"';");
	            } else {// 如果列表中没有数据
	            	int q = runner.update(conn, sql1, params);
	            }
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
        }        
		return true;		
	}

	public static List<Bag> getClientBag(int id_client) {
		QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
        String sql = "select * from tb_bag where clientID='"+id_client+"';";// 定义查询语句
        Connection conn = getConnection();// 获得连接
        ResultSetHandler<List<Bag>> rsh = new BeanListHandler<Bag>(Bag.class);// 创建结果集处理类
        List<Bag> result;
		try {
			result = (List<Bag>)runner.query(conn, sql, rsh);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 获得查询结果
        
	return null;
	}

	public static void DeleteBag(int id_bag) {
		QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
	     Connection conn = getConnection();// 获得连接
	     try {	    	 	
	   	     	String sql = "delete from tb_bag where ID = '"+id_bag+"';";// 定义查询语句
	   	     	int q = runner.update(conn, sql);	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            DbUtils.closeQuietly(conn);// 关闭连接
	        }
		
	}
	
	

}
