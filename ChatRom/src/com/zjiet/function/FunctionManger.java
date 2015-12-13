package com.zjiet.function;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.zjiet.empty.UserInfo;
import com.zjiet.ui.Login;
import com.zjiet.ui.Main;
import com.zjiet.ui.Regist;

public class FunctionManger {
	private FunctionManger(){}
	private static final FunctionManger  instance = new FunctionManger();
	public static FunctionManger getCM(){
		return instance;
	}
	Main mainWindow;
	Login loginwin;
	Socket socket;
	DatagramSocket dSocket;
	DatagramPacket packet;
	BufferedReader reader;
	BufferedWriter writer;
	String data ;
	String name;
	String count;
	LoginUIFuntion loginUIFuntion;
	Regist regist; 
	public void setWindow(Main main){
		this.mainWindow = main;
	}
	public void setLoginWindow(Login login){
		this.loginwin =  login;
	}
	public void setRegWin(Regist regist){
		this.regist = regist;
	}

	String IP;
	public void connect(String ip,int prot){
		this.IP=ip;
		new Thread(){
			public void run() {
				try {
					socket = new Socket(IP, prot);
					reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					writer = new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream()));
					String line;
					while ((line=reader.readLine())!=null) {
						System.out.println("FM:"+line);
						if (line.length()>=8) {
							
							if (line.substring(0, 9).equals("CheckPass")) {
								checkPass(line);
							}
			
						}
						if (line.length()>=7) {
							if (line.substring(0,7).equals("AddUser")) {
							  addUser(line);
							} 
						}
						if (line.length()==11) {
							//已在线不允许重复登录
							if (line.substring(0,11).equals("{OnLineITS}")) {
								checkPass(line);
							} 
						}
						
					
						
						//mainWindow.appendText("收到："+line+"\n");
					}
				
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "连接服务器失败，请检查你的网络或联系管理员！");
					loginwin.disable();
					e.printStackTrace();
				}
			};
		}.start();
	}

	public boolean send(String text){
		boolean send=true;
		if(writer!=null){
			try {
				writer.write(text+"\n");
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			JOptionPane.showMessageDialog(null, "你并不能连上服务器！也许是服务器IP改变，请联系管理员！");
			send  = false;
		}
		return send;
	}
	public void checkPass(String line){
	
			if (line.equals("CheckPass:False")) {
				loginwin.checkPass("False");
			}else if (line.equals("{OnLineITS}")) {
		
				loginwin.checkPass("OnLineITS");
			}else {
				String a[] = line.split("<>");
				ChatManger.getCM().setName(a[1]);
//				count = "当前在线人数："+a[2];
				loginwin.checkPass("True");
			
			} 

	}
	public void addUser(String line){
		String a[] = line.split("<>");
		regist.dipose();
		JOptionPane.showMessageDialog(null, "您注册的ID是： ("+a[1]+")");
		
	}
	
	
}
