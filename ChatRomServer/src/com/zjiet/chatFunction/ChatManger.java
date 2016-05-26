package com.zjiet.chatFunction;
import java.util.Vector;

public class ChatManger {
	private ChatManger(){}
	private static final ChatManger cm = new ChatManger();
	public static ChatManger getChatManger(){
		return cm;
	}
	Vector<ChatSocket> vector = new Vector<ChatSocket>();
	Vector<Vector<String>> ChatRomManger = new Vector<>();
	//添加socket
	public void add(ChatSocket cs){
		vector.add(cs);
		
	}
	public void CreateChatRom(Vector<String> vector){
		ChatRomManger.addElement(vector);
	}
	public void publish(ChatSocket cs,String out){
		for(int i=0;i<vector.size();i++){
			ChatSocket cSocket = vector.get(i);
			
		   // System.out.println(cSocket.socket.getInetAddress().getHostAddress());
		if (!cs.equals(cSocket)) {
			cSocket.out(out);
		}
		
//			if (cSocket.socket.getInetAddress().getHostAddress().equals("192.5.0.112")) {
//				cSocket.out(out);
//			}
			
		}
	}
	
	public void sendToClient(String toIP,String fromIP,String msg){
		for(int i=0;i<vector.size();i++){
			ChatSocket cSocket = vector.get(i);
		
			if (cSocket.socket.getInetAddress().getHostAddress().equals(toIP)) {
				cSocket.out("MSG<>"+fromIP+"<>"+msg);
			}
			
		}
	}
	public void sendChatRomInfoToClient(String info){
		String a[] = info.split("<>");
		for (int i = 5; i < a.length; i++) {
			for(int j=0;j<vector.size();j++){
				if (vector.get(j).socket.getInetAddress().getHostAddress().equals(a[i])) {
					String ChatRomInfo="ChatRomInfo";
					for (int j2 = 1; j2 < a.length; j2++) {
						if (!a[j2].equals(a[i])) {
							ChatRomInfo = ChatRomInfo+"<>"+a[j2];
						}
					}
					vector.get(j).out(ChatRomInfo);
				}
			}
		}
	}
	public void sendFileMsg(String toIP,String cmd){
		for(int i=0;i<vector.size();i++){
			ChatSocket cSocket = vector.get(i);
			if (cSocket.socket.getInetAddress().getHostAddress().equals(toIP)) {
				cSocket.out(cmd);
			}
			
		}
	}
	public void updateChatRom(String ip,String cmd){
		System.out.println("ip:"+ip);
		System.out.println("cmd:"+cmd);
		for(int i=0;i<vector.size();i++){
			ChatSocket cSocket = vector.get(i);
			if (cSocket.socket.getInetAddress().getHostAddress().equals(ip)) {
				cSocket.out(cmd);
			}
			
		}
	}
	public void sendChatRomMsg(String line){
		new Thread(){
			public void run() {
				String a[] = line.split("<>");
				for (int i = 0; i < ChatRomManger.size(); i++) {
					if (ChatRomManger.get(i).get(0).equals(a[1])) {
						for (int j = 1; j < ChatRomManger.get(i).size(); j++) {
							String b[] =ChatManger.getChatManger().ChatRomManger.get(i).get(j).split("\\)") ;
							String c[] = b[0].split("\\(");
							for(int k=0;k<vector.size();k++){
								ChatSocket cSocket = vector.get(k);
								if (cSocket.socket.getInetAddress().getHostAddress().equals(c[1])&&!cSocket.socket.getInetAddress().getHostAddress().equals(a[2])) {
									System.err.println("socket："+line);
									cSocket.out(line);
									
								}
								
							}
						}
						
						break;
					}
				}
			};
		}.start();
	}

	//获取当前在线人数
	public int getCount(){
		int count=0;
		for (int i = 0; i < vector.size(); i++) {
			count++;
		}
		return count;
	}
	//移除离线socket
	public void del(ChatSocket cs){
		
		for(int i=0;i<vector.size();i++){
			if (vector.get(i).equals(cs)) {
				 vector.remove(i);
			}
			 
		}
		int count = getCount();
		for(int i=0;i<vector.size();i++){
		
				vector.get(i).out("UPDataCount<>"+count);
			
		}
	}
	
	//向登录用户发送在线用户列表
	public void sendIPList(ChatSocket cs){
		for(int j=0;j<vector.size();j++){
			if (!vector.get(j).equals(cs)) {
				cs.out("IPOnLine<>"+vector.get(j).socket.getInetAddress().getHostAddress()+"<>"+vector.get(j).username);
				System.out.println(j);
			}
			 
		 }
		int count = getCount();
		for(int i=0;i<vector.size();i++){
			if (!vector.get(i).equals(cs)) {
				 vector.get(i).out("IPOnLine<>"+cs.socket.getInetAddress().getHostAddress()+"<>"+cs.username);
			}
			 
		}
	
		//向客户端更新在线人数！
		for(int i=0;i<vector.size();i++){
			
				vector.get(i).out("UPDataCount<>"+count);
			
		}
	}
	
	//向在线用户 发送 用户下线消息
	public void sendOffline(ChatSocket cs){
		for(int i=0;i<vector.size();i++){
			if (!vector.get(i).equals(cs)) {
				 vector.get(i).out("{userOffline}<>"+cs.socket.getInetAddress().getHostAddress());
			}
			 
		}
	}

	
	}
