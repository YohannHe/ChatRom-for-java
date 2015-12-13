package com.zjiet.util;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.zjiet.chatFunction.FunctionManger;
import com.zjiet.chatFunction.FuntionSocket;
import com.zjiet.empty.UserInfo;



public class SQLConnection {
	 public static final String url = "jdbc:mysql://127.0.0.1/db_chatrom?characterEncoding=UTF-8";  
	    public static final String name = "com.mysql.jdbc.Driver";  
	    public static final String user = "root";  
	    public static final String password = "";  
	    public Connection conn = null;  
	    public PreparedStatement pst = null;  
	    public ResultSet rst;
	    private static final SQLConnection cm = new SQLConnection();
		public static SQLConnection getConnection(){
			return cm;
		}
	    private SQLConnection() {  
	        try {  
	            Class.forName(name);//指定连接类型  
	            conn = DriverManager.getConnection(url, user, password);//获取连接  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	  
	    public void close() {  
	        try {  
	            this.conn.close();  
	            this.pst.close();  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        }  
	    }
	    
	    public UserInfo Select(UserInfo userInfo){
	    	String pass=null;
	    	UserInfo  user = new UserInfo();
	    	try {
				pst = conn.prepareStatement("select * from tb_user_info where user_id=?");
				pst.setString(1, userInfo.getUserID());
				rst=pst.executeQuery();
				while (rst.next()) {
					user.setUserID(rst.getString(2));
					user.setUserName(rst.getString(3));
					user.setUserPass(rst.getString(4));
					user.setUserIP(rst.getString(5));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return user;
	    	
	    }
	    
	    public void Insert(UserInfo userInfo){
	    	try {
				pst = conn.prepareStatement("insert into tb_user_info(user_id,user_name,user_pass,user_ip)values(?,?,?,?)");
				pst.setString(1, userInfo.getUserID());
			
					pst.setString(2 , userInfo.getUserName());
					

				pst.setString(3, userInfo.getUserPass());
				pst.setString(4, "NULL");
				pst.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//再此处有必要添加 写出错误日志！
			}
			
	    }
	    public void updateIP(String ip,String id){
	    	try {
				pst = conn.prepareStatement("update tb_user_info set user_ip=? where user_id=?");
				pst.setString(1,ip);
				pst.setString(2, id);
				pst.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//再此处有必要添加 写出错误日志！
			}
	    }
	    
	    public void initIP(){
	    	try {
				pst = conn.prepareStatement("update tb_user_info set user_ip=? ");
				pst.setString(1,"NULL");
				pst.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//再此处有必要添加 写出错误日志！
			}
	    }
	    public String selectName(String ip){
	    	String username = null;
	    	try {
				pst = conn.prepareStatement("select user_name from tb_user_info where user_ip=?");
				pst.setString(1, ip);
				rst=pst.executeQuery();
				while (rst.next()) {
					username = rst.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return username;
	    }
	    public String selectIP(String id){
	    	String userip = null;
	    	try {
				pst = conn.prepareStatement("select user_ip from tb_user_info where user_id=?");
				pst.setString(1, id);
				rst=pst.executeQuery();
				while (rst.next()) {
					userip = rst.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return userip;
	    }
	    public String selectID(String ip){
	    	String userid = null;
	    	try {
				pst = conn.prepareStatement("select user_id from tb_user_info where user_ip=?");
				pst.setString(1, ip);
				rst=pst.executeQuery();
				while (rst.next()) {
					userid = rst.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return userid;
	    }
	    public String selectPass(String userid){
	    	String userPass=null;
	    	try {
				pst = conn.prepareStatement("select user_pass from tb_user_info where user_id=?");
				pst.setString(1, userid);
				rst=pst.executeQuery();
				while (rst.next()) {
					userPass= rst.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return userPass;
	    }
	    public boolean UPDatePass(String pass,String userid){
	    	boolean flag=false;
	    	try {
				pst = conn.prepareStatement("update tb_user_info set user_pass=? where user_id=?");
				pst.setString(1,pass);
				pst.setString(2, userid);
				pst.execute();
				flag=true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//再此处有必要添加 写出错误日志！
			}
	    	return flag;
	    }
	    
	    public boolean UPDateName(String userName,String userid){
	    	boolean flag = false;
	    	try {
				pst = conn.prepareStatement("update tb_user_info set user_name=? where user_id=?");
				pst.setString(1,userName);
				pst.setString(2, userid);
				pst.execute();
				flag=true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//再此处有必要添加 写出错误日志！
			}
	    	return flag;
	    }

}
	    
