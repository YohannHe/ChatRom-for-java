package com.zjiet.chatFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.zjiet.empty.UserInfo;
import com.zjiet.util.SQLConnection;

public class FuntionSocket extends Thread{
	Socket socket ;
	String username;
	public FuntionSocket(Socket s) {
		this.socket=s;
	}
	public void setUserName(String name){
		this.username = name;
	}
	//发送数据！
	public void out(String out){
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(out+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new  InputStreamReader(socket.getInputStream(),"gbk"));
			String line = null;
			
			while ((line=br.readLine())!=null) {
				//System.out.println(line);
				//获取接受的数据，判断是否为特殊指令！
			
				if (line.length()>=7) {
					
					if (line.substring(0, 9).equals("CheckPass")) {
						String a[] = line.split("<>");
						UserInfo userInfo = new UserInfo();
						userInfo.setUserID(a[1]);
						userInfo.setUserPass(a[2]);
						CheckPass(userInfo);
						
					}
					if (line.substring(0, 7).equals("AddUser")) {
						String a[] = line.split("<>");
						UserInfo userInfo = new UserInfo();
						userInfo.setUserName(a[1]);
						userInfo.setUserPass(a[2]);
						userInfo.setUserID(CreatUserID());
						AddUser(userInfo);
						out("AddUser<>"+userInfo.getUserID());
					
					
				}
					
				}
			}
			
		} catch (Exception e) {
			
			FunctionManger.getFunctionManger().del(this);//从数组中删除socket
			//e.printStackTrace();
		}
	}
	
	public void CheckPass(UserInfo userInfo){
		
	String pass=SQLConnection.getConnection().Select(userInfo).getUserPass();
	if (pass!=null) {
		if (pass.equals(userInfo.getUserPass())) { 
			String ip = SQLConnection.getConnection().selectIP(userInfo.getUserID());
			if (ip.equals("NULL")) { //验证是否已登录
				String name = SQLConnection.getConnection().Select(userInfo).getUserName();
				this.out("CheckPass:True<>"+name);//账号密码验证通过，向客户端发送True
				SQLConnection.getConnection().updateIP(this.socket.getInetAddress().getHostAddress(),userInfo.getUserID());
			}else{
				this.out("{OnLineITS}");
			}
			
		}else {
			this.out("CheckPass:False");
			
		}
	}else {
		this.out("CheckPass:False");
	}
		
	}
	

	public void AddUser(UserInfo userInfo){
		
		SQLConnection.getConnection().Insert(userInfo);

	}
	public String CreatUserID(){
		long userID=0;
		UserInfo user = new UserInfo();
		boolean have = true;
		while (have) {
			
			userID =(long)(Math.random()*999999);
			user.setUserID(String.valueOf(userID));
			String pass=SQLConnection.getConnection().Select(user).getUserPass();
			if (pass==null) {
				have=false;
			}
		}
		
		return String.valueOf(userID);
		
	}
	

}	
