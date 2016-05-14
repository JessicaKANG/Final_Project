package com.Pineapple.SocketSever;

import java.io.DataInputStream;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import com.Pineapple.Dao.DBAddorder;
import com.Pineapple.Dao.DBCheckBag;
import com.Pineapple.Dao.DBCheckcomponent;
import com.Pineapple.Dao.DBCheckcomputer;
import com.Pineapple.Dao.DBCheckstock;
import com.Pineapple.Dao.DBClientLogin;
import com.Pineapple.Dao.model.Bag;
import com.Pineapple.Dao.model.Client;
import com.Pineapple.Dao.model.Computer;
import com.Pineapple.Dao.model.Order;
import com.Pineapple.Dao.model.Order_detial;

public class ServerThread implements Runnable {    
    
    private Socket socket;  
    private String accept = "NULL";  
    private String username,passwd;  
    private DataInputStream in = null;  
    private DataOutputStream out = null;  
    private ObjectInputStream inBean = null;
    private ObjectOutputStream outBean = null;
    private Client client;
    private Boolean check = false;
    
    public ServerThread(Socket socket)  
    {            
        this.socket = socket;             
    }    
    
    // 任务是为一个用户提供服务    
    @Override    
    public void run()  
    {    
    	boolean exitLable= true;
    	while(exitLable){
    	   try  
           {    
               // 读取客户端传过来信息的DataInputStream    
               in = new DataInputStream(socket.getInputStream());    
               
               System.out.println("等待接收用户信息");  
               // 读取来自客户端的信息    
               accept = in.readUTF();               
           }                 
           catch (IOException e)  
           {  
        	   try {
        		
   				in.close();
   				socket.close();
   				exitLable = false;
           	   } catch (IOException e1) {
           	   }                  
           } 
    	   ///////////////////////////////登陆部分逻辑检测////////////////////////
           if(accept.equals("LOGIN"))  
           { 
        	   System.out.println("接收到LOGIN");
               try  
               {  
            	// 向客户端发送信息的DataOutputStream    
                   out = new DataOutputStream(socket.getOutputStream());     
                     username = in.readUTF(); 
                     passwd = in.readUTF();
                     if (DBClientLogin.exists(username)) {
   						out.writeUTF("true"); 
   						out.flush();
   						if (DBClientLogin.check(username, passwd)) {
   							out.writeUTF("true");   
   							out.flush();
   							}
   						else{
   							out.writeUTF("false");
   							out.flush();
   							}  					
   						}
                     else{
                   	  out.writeUTF("false");
                   	  out.flush();
                     }                                      
               }
               catch(IOException e)  
               { }  
           } 
           //////////////////////////////////////注册逻辑检测/////////////////////////////
           else if(accept.equals("SIGNUP"))  
           {
        	   System.out.println("接收到SIGNUP");
                 try {
                	out = new DataOutputStream(socket.getOutputStream());
                	username = in.readUTF();					
					if(DBClientLogin.exists(username)){
						out.writeUTF("true");
						out.flush();
					}
					else{
						out.writeUTF("false");
						out.flush();
						inBean = new ObjectInputStream(socket.getInputStream());
						Client client = (Client) inBean.readObject();
						if(DBClientLogin.save(client)){
						out.writeUTF("true");
						out.flush();
						}
						else{
							out.writeUTF("false");
							out.flush();
						}					
					}					
				} catch (IOException | ClassNotFoundException e) {	
					e.printStackTrace();
				}
           } 
           /////////////////////////////////////////////////////////////////////////////
           else if(accept.equals("SHOWALLCPR")){
        	   System.out.println("接收到SHOWALLCPR");
        	   List<Computer> list = DBCheckcomputer.select();
	        	   try {
					outBean = new ObjectOutputStream(socket.getOutputStream());
					outBean.writeObject(list);
					outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	   
           }
           /////////////////////////////////////////////////////////////////////////////
           else if(accept.equals("SHOWACTIVEORDER")){
        	   System.out.println("接收到SHOWACTIVEORDER");        	           	   
	        	   try {
					in = new DataInputStream(socket.getInputStream());
					String name_client = in.readUTF();
					int id_client = DBClientLogin.getClientID(name_client);
	        		outBean = new ObjectOutputStream(socket.getOutputStream());
	        		List<Order> list = DBAddorder.getClientActiveOrder(id_client);
					outBean.writeObject(list);
					outBean.flush();										
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	   
           }
           else if(accept.equals("SHOWDEADORDER")){
        	   System.out.println("接收到SHOWADEADORDER");        	           	   
	        	   try {
					in = new DataInputStream(socket.getInputStream());
					String name_client = in.readUTF();
					int id_client = DBClientLogin.getClientID(name_client);
	        		outBean = new ObjectOutputStream(socket.getOutputStream());
	        		List<Order> list = DBAddorder.getClientDeadOrder(id_client);
					outBean.writeObject(list);
					outBean.flush();										
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	   
           }
           /////////////////////////////////////////////////////////////////////////////
           else if(accept.equals("BAG")){
        	   System.out.println("BAG");
        	   try {				
				in = new DataInputStream(socket.getInputStream());
				String name_client = (String) in.readUTF();
				int id_client = DBClientLogin.getClientID(name_client);
				inBean = new ObjectInputStream(socket.getInputStream());
				List<Bag> baglist = (List<Bag>) inBean.readObject();
				if(DBCheckBag.addtoBag(id_client,baglist)){
					out = new DataOutputStream(socket.getOutputStream());
	     			   out.writeUTF("True");
	     			   out.flush();
				}else{
					out = new DataOutputStream(socket.getOutputStream());
     			   out.writeUTF("False");
     			   out.flush();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
    		   
           }
           else if(accept.equals("SHOWALLBAG")){
        	   System.out.println("接收到SHOWALLBAG");        	           	   
	        	   try {
					in = new DataInputStream(socket.getInputStream());
					String name_client = in.readUTF();
					int id_client = DBClientLogin.getClientID(name_client);
	        		outBean = new ObjectOutputStream(socket.getOutputStream());
	        		List<Bag> list = DBCheckBag.getClientBag(id_client);
					outBean.writeObject(list);
					outBean.flush();										
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	   
           }
           ////////////////////////////////////////////////////////////////////////////
           else if(accept.equals("ORDER")){
        	   System.out.println("接收到ORDER");
        	   
	        	   try {
	        		   inBean = new ObjectInputStream(socket.getInputStream());
	        		   Map<String,Integer> computermap = (Map<String,Integer>) inBean.readObject();
	        		   Map<String,Integer> componentmap = (Map<String,Integer>) inBean.readObject();	        		  
	        		   Iterator<Map.Entry<String, Integer>> entries = computermap.entrySet().iterator();
	        		   while (entries.hasNext()){
	        			   Entry<String, Integer> entry = entries.next();	        			   
	        			   check = DBCheckstock.checkcpr(entry.getKey(),entry.getValue());
	        			   if(!check){
	        				   out = new DataOutputStream(socket.getOutputStream());
		        			   out.writeUTF("False");
		        			   out.flush();			        			
	        				   break;
	        			   }
	        		   }	        		   
	        		   if (check){	//电脑库存核查完毕，开始核查配件库存
	        			   entries = componentmap.entrySet().iterator();
	        			   while (entries.hasNext()){
	        				   Entry<String, Integer> entry = entries.next();
	        				   check = DBCheckstock.checkcpt(entry.getKey(), entry.getValue());	        				  
	        				   if(!check){
	        					   out = new DataOutputStream(socket.getOutputStream());
			        			   out.writeUTF("False");
			        			   out.flush();		        					   
			        			   break;
	        				   }
	        			   }//配件库存和电脑库存都核查完毕
	        			   if(check){
	        				   out = new DataOutputStream(socket.getOutputStream());
	        				   out.writeUTF("True");//核查库存后判断是否弹出付款对话框
	        				   out.flush();			        			   
	        			   }
	        				  		        			   	        			           			   	        			   
	        		   }
	        		   	        		   	        		   
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	   
           }
           else if (accept.equals("SAVEORDER")){
        	   try {
        		   inBean = new ObjectInputStream(socket.getInputStream());
        		   Map<String,Integer> computermap = (Map<String,Integer>) inBean.readObject();
        		   Map<String,Integer> componentmap = (Map<String,Integer>) inBean.readObject();	       		   
				inBean = new ObjectInputStream(socket.getInputStream());
				Order order = (Order)inBean.readObject();
				List<Order_detial> detiallist = (List<Order_detial>) inBean.readObject(); 
				if(DBAddorder.save(order,detiallist)){
				   out.writeUTF("True");
				   out.flush();	
				   Iterator<Map.Entry<String, Integer>> entries = computermap.entrySet().iterator();
    			   while(entries.hasNext()){
    				   Entry<String, Integer> entry = entries.next();
    				   DBCheckstock.modifycpr(entry.getKey(),entry.getValue());
    			   	}//电脑库存修改
    			   Iterator<Map.Entry<String, Integer>> entries2 = componentmap.entrySet().iterator();
    			   while (entries2.hasNext()){
    				   Entry<String, Integer> entry2 = entries2.next();
    				   DBCheckstock.modifycpt(entry2.getKey(),entry2.getValue());
    			   }//配件库存修改
    			   System.out.println("库存修改完毕");	  			
				  ////////////////////////////////////////////////////////////////////////////
				   //邮件发送订单详情
				   	// 收件人电子邮箱
				   	
				      String to = DBClientLogin.getEmail(order.getClient());
				      // 发件人电子邮箱
				      String from = "computer.company.pineapple@gamil.com";
				      // 指定发送邮件的主机为 localhost
				      String host = "smtp.gmail.com";  // 邮件服务器
				      // 获取系统属性
				      Properties properties = System.getProperties();
				      // 设置邮件服务器
				      properties.setProperty("mail.smtp.host", host);
				      properties.put("mail.smtp.auth", "true");
				      properties.put("mail.smtp.starttls.enable", "true");
				      properties.put("mail.smtp.port", "587");
				      // 获取默认session对象
				      Session session = Session.getDefaultInstance(properties,new Authenticator(){
					    public PasswordAuthentication getPasswordAuthentication()
					    {
					     return new PasswordAuthentication("computer.company.pineapple@gmail.com", "pineapple123456"); //发件人邮件用户名、密码
					    }
					   });
				      try{
				         // 创建默认的 MimeMessage 对象
				         MimeMessage message = new MimeMessage(session);
				         // Set From: 头部头字段
				         message.setFrom(new InternetAddress(from));
				         // Set To: 头部头字段
				         message.addRecipient(Message.RecipientType.TO,
				                                  new InternetAddress(to));
				         // Set Subject: 头部头字段
				         message.setSubject("Order Confirmation");
				         String orderdetial = getmessage(detiallist);
				         // 设置消息体
				         message.setText("Dear "+order.getClient()+"，\n"+"Your order is being processing. The details are  as follows.\n"
				         +"OrderID: "+order.getID()+"\n"
				         +"Total Price: $"+order.getPrice()+"\n"
				         +"Order Date: "+order.getDatetime()+"\n"
				         +"Order State: To be shipped\n"
				         +"Payment Method: "+order.getPayment()+"\n"
				         +"Delivery Address: "+order.getDelivery()+"\n"
						+orderdetial
				         );
				         
				         // 发送消息
				         Transport.send(message);		        		
				      }catch (MessagingException mex) {
				         mex.printStackTrace();
				      }
			       //////////////////////////////////////////////////////////邮件发送完毕		        			       
			   }      	   
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			   
           }
           //////////////////////////////////////////////////////////////////////////////
           else if(accept.equals("ARRIVEORDER")){
        	   System.out.println("接收到ARRIVEORDER");
        	   try {
					in = new DataInputStream(socket.getInputStream());
					String id_order = in.readUTF();
					DBAddorder.ChangeOrderState(id_order, "2");	
					out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF("True");
				    out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if(accept.equals("CANCELORDER")){
        	   System.out.println("接收到CANCELORDER");
        	   try {
					in = new DataInputStream(socket.getInputStream());
					String id_order = in.readUTF();
					DBAddorder.ChangeOrderState(id_order, "3");	
					//////////////////////////////////////////////库存补偿///////////////////
					List<Order_detial> detiallist = DBAddorder.getDetiallist(id_order);
					Map<String,Integer> computermap = new HashMap<String,Integer>();
					  Map<String,Integer> componentmap = new HashMap<String,Integer>();
					Iterator iterator = detiallist.iterator();
					while(iterator.hasNext()){
						Order_detial detial = (Order_detial) iterator.next();
						int p =computermap.getOrDefault(detial.getcomputerID(), 0);
						if(p==0){computermap.put(detial.getcomputerID(), detial.getnumber());}
						else{computermap.put(detial.getcomputerID(), p+detial.getnumber());}
						p = componentmap.getOrDefault(detial.getcolor(), 0);
						if(p==0){componentmap.put(detial.getcolor(),detial.getnumber());}
						else{componentmap.put(detial.getcolor(), p+detial.getnumber());}
						p = componentmap.getOrDefault(detial.getsize(), 0);
						if(p==0){componentmap.put(detial.getsize(),detial.getnumber());}
						else{componentmap.put(detial.getsize(), p+detial.getnumber());}
						p = componentmap.getOrDefault(detial.getstock(), 0);
						if(p==0){componentmap.put(detial.getstock(),detial.getnumber());}
						else{componentmap.put(detial.getstock(), p+detial.getnumber());}
						p = componentmap.getOrDefault(detial.getmemory(), 0);
						if(p==0){componentmap.put(detial.getmemory(),detial.getnumber());}
						else{componentmap.put(detial.getmemory(), p+detial.getnumber());}
						p = componentmap.getOrDefault(detial.getgraphics(), 0);
						if(p==0){componentmap.put(detial.getgraphics(),detial.getnumber());}
						else{componentmap.put(detial.getgraphics(), p+detial.getnumber());}
						p = componentmap.getOrDefault(detial.getprocessor(), 0);
						if(p==0){componentmap.put(detial.getprocessor(),detial.getnumber());}
						else{componentmap.put(detial.getprocessor(), p+detial.getnumber());}
					}
					System.out.println(computermap);
					System.out.println(componentmap);
					Iterator<Map.Entry<String, Integer>> entries = computermap.entrySet().iterator();
	    			   while(entries.hasNext()){
	    				   Entry<String, Integer> entry = entries.next();
	    				   DBCheckstock.modifybackcpr(entry.getKey(),entry.getValue());
	    			   	}//电脑库存修改
	    			   Iterator<Map.Entry<String, Integer>> entries2 = componentmap.entrySet().iterator();
	    			   while (entries2.hasNext()){
	    				   Entry<String, Integer> entry2 = entries2.next();
	    				   DBCheckstock.modifybackcpt(entry2.getKey(),entry2.getValue());
	    			   }//配件库存修改
					out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF("True");
				    out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if(accept.equals("DELETEORDER")){
        	   System.out.println("接收到DELETEORDER");
        	   try {
					in = new DataInputStream(socket.getInputStream());
					String id_order = in.readUTF();
					DBAddorder.DeleteOrder(id_order);	//删除对应订单
					out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF("True");
				    out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if(accept.equals("DELETEBAG")){
        	   System.out.println("接收到DELETEBAG");
        	   try {
					in = new DataInputStream(socket.getInputStream());
					int id_bag = in.readInt();
					DBCheckBag.DeleteBag(id_bag);
					out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF("True");
				    out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           ///////////////////////////////////////////////////////////////////////////
           //搜索可选配件
           else if (accept.equals("COMBOCOLOR")){
        	   List<String> itemlist = DBCheckcomponent.getComponentNameList("Color");
	        	   try {
					outBean = new ObjectOutputStream(socket.getOutputStream());
					outBean.writeObject(itemlist);
					outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if (accept.equals("COMBOSIZE")){
        	   List<String> itemlist = DBCheckcomponent.getComponentNameList("Screen");
	        	   try {
					outBean = new ObjectOutputStream(socket.getOutputStream());
					outBean.writeObject(itemlist);
					outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if (accept.equals("COMBOSTOCK")){
        	   List<String> itemlist = DBCheckcomponent.getComponentNameList("Stock");
	        	   try {
					outBean = new ObjectOutputStream(socket.getOutputStream());
					outBean.writeObject(itemlist);
					outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if (accept.equals("COMBOMEMORY")){
        	   List<String> itemlist = DBCheckcomponent.getComponentNameList("Memory");
	        	   try {
					outBean = new ObjectOutputStream(socket.getOutputStream());
					outBean.writeObject(itemlist);
					outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if (accept.equals("COMBOGRAPHIC")){
        	   List<String> itemlist = DBCheckcomponent.getComponentNameList("Graphics");
	        	   try {
					outBean = new ObjectOutputStream(socket.getOutputStream());
					outBean.writeObject(itemlist);
					outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           else if (accept.equals("COMBOPROCESSOR")){
        	   List<String> itemlist = DBCheckcomponent.getComponentNameList("Processor");
	        	   try {
					outBean = new ObjectOutputStream(socket.getOutputStream());
					outBean.writeObject(itemlist);
					outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           //获得新价格
           else if (accept.equals("NEWPRICE")){
	        	   try {
	        		   	in = new DataInputStream(socket.getInputStream());    
	        		   	String itemname = in.readUTF();
	        		   	String precomponentname = in.readUTF();
	        		   	double price = in.readDouble();
	        		   	double newPrice = DBCheckcomponent.getnewPrice(itemname,precomponentname,price);
						outBean = new ObjectOutputStream(socket.getOutputStream());
						outBean.writeObject(newPrice);
						outBean.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           /////////////////////////////////////////////////////////////////////////////
           try  
           {  
               Thread.sleep(500);  
           }  
           catch(Exception e)  
           {
        	             	
           }  
       }      
    }

	private String getmessage(List<Order_detial> detiallist) {
		Iterator iterator = detiallist.iterator();
		String content = "";
		while(iterator.hasNext()){
			Order_detial detial = (Order_detial) iterator.next();
			String idcomputer = flushLeft(' ',12 , detial.getcomputerID());
			String price = flushLeft(' ',8 , "$"+Double.toString(detial.getprice()));
			String color = flushLeft(' ',5 , detial.getcolor());
			String size = flushLeft(' ',5 , detial.getsize());
			String stock = flushLeft(' ',5 , detial.getstock());
			String memory = flushLeft(' ',8 , detial.getmemory());
			String graphics = flushLeft(' ',8 , detial.getgraphics());
			String processor = flushLeft(' ',10 , detial.getprocessor());
			String number = flushLeft(' ',8 , "*"+Integer.toString(detial.getnumber()));
			content = content+ idcomputer + price+number+ color+ size+ stock+memory+ graphics+processor+"\n";					
		}
		return content;
	} 
	 /* c 要填充的字符    
   *  length 填充后字符串的总长度    
   *  content 要格式化的字符串   
   *  格式化字符串，左对齐 
   * */  
		public String flushLeft(char c, long length, String content){             
		      String str = "";     
		      long cl = 0;    
		      String cs = "";     
		      if (content.length() > length){     
		           str = content;     
		      }else{    
		           for (int i = 0; i < length - content.length(); i++){     
		               cs = cs + c;     
		           }  
		         }  
		       str = content + cs;      
		       return str;      
		  }   
} 
 
  
   