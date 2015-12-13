package com.zjiet.chatFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.text.AbstractDocument.LeafElement;

import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

import com.zjiet.empty.UserInfo;
import com.zjiet.util.SQLConnection;

public class ChatSocket extends Thread{
	Socket socket ;
	String username;
	public ChatSocket(Socket s) {
		this.socket=s;
		
		username = SQLConnection.getConnection().selectName(s.getInetAddress().getHostAddress());
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
				System.out.println("服务端:"+line);
				//获取接受的数据，判断是否为特殊指令！
				if (line.length()>=6) {
					if (line.substring(0, 6).equals("ROMMSG")) {
						ChatManger.getChatManger().sendChatRomMsg(line);
					}
				}
				if (line.length()>=14) {
					if(line.substring(0, 14).equals("ChatRomOffline")){
						offlineChatRom(line);
					}
				}
				if (line.length()>=13) {
					if (line.substring(0,13).equals("removethisone")) {
						removethisone(line);
					}
				}
				if (line.length()>=11) {
					if (line.substring(0, 11).equals("joinChatRom")) {
						updateChatRom(line);
					}
				}
				if (line.length()>=11) {
					if (line.substring(0, 11).equals("ChatRomInfo")) {
						sendChatRomInfo(line);
					}
				}
				if (line.length()>=10) {
					if (line.substring(0,10).equals("ChangePass")) {
						ChangePass(line);
					}
				}
				if (line.length()>=10) {
					if (line.substring(0,10).equals("ChangeName")) {
						ChangeName(line);
					}
				}
				if (line.length()>=8) {
					if (line.substring(0,8).equals("FileInfo")) {
						String a[] = line.split("<>");
						ChatManger.getChatManger().sendFileMsg(a[6], line);
					}else if (line.substring(0,10).equals("FileAccept")) {
						String a[] = line.split("<>");
						ChatManger.getChatManger().sendFileMsg(a[3], line);
					} 
				}
				if (line.length()>=7) {
					if(line.substring(0, 7).equals("SendMsg")){
						String a[] = line.split("<>");
						for (int i = 0; i < ChatManger.getChatManger().vector.size(); i++) {
							System.out.println(ChatManger.getChatManger().vector.get(i).socket.getInetAddress().getHostAddress());
							String ip = ChatManger.getChatManger().vector.get(i).socket.getInetAddress().getHostAddress();
							if (ChatManger.getChatManger().vector.get(i).socket.getInetAddress().getHostAddress().equals(a[1])) {
								ChatManger.getChatManger().vector.get(i).out("SendFile");
								new SendFileManger().Resave(a[1], socket.getInetAddress().getHostAddress(), socket,ChatManger.getChatManger().vector.get(i).socket);
								break;
							}
							
						}
						
						
					}
					
				}
				if (line.length()>=3) {
					if (line.substring(0, 3).equals("MSG")) {
						String a[] = line.split("<>");
						String toIP = a[1];
						String fromIP = this.socket.getInetAddress().getHostAddress();
						ChatManger.getChatManger().sendToClient(toIP, fromIP, a[2]);
					}
				}
				if (line.length()==17) {
					if (line.substring(0, 17).equals("@/0GetIPOnline/0@")) {
						ChatManger.getChatManger().sendIPList(this);
					}
				}else {
					//ChatManger.getChatManger().publish(this, line);
				}
				
			}
		} catch (IOException e1) {
			System.out.println("用户已断开连接！");;
			ChatManger.getChatManger().sendOffline(this);
			ErrofflineChatRom(this.socket.getInetAddress().getHostAddress());
			SQLConnection.getConnection().updateIP("NULL",SQLConnection.getConnection().selectID(this.socket.getInetAddress().getHostAddress()) );
			ChatManger.getChatManger().del(this);//从数组中删除socket
			System.out.println(ChatManger.getChatManger().getCount());
			//e.printStackTrace();
			e1.printStackTrace();
		}
	
	}
	public void removethisone(String line){
		new Thread(){
			public void run() {
				offlineChatRom(line);
				String a[] = line.split("<>");
				String b[] =a[2].split("\\)") ;
				String c[] = b[0].split("\\(");
				for (int i = 0; i < ChatManger.getChatManger().vector.size(); i++) {
					if (ChatManger.getChatManger().vector.get(i).socket.getInetAddress().getHostAddress().equals(c[1])) {
					
						ChatManger.getChatManger().vector.get(i).out("Fired<>"+a[1]);
						break;
					}
				}
			};
		}.start();
		
		
		
	}
	
	public void ChangePass(String info){
		String a[] = info.split("<>");
		String pass = SQLConnection.getConnection().selectPass(a[1]);
		if (pass.equals(a[2])) {
			if (SQLConnection.getConnection().UPDatePass(a[3], a[1])) {
				out("ChangePassTrue");
			}else {
				out("ChangePassERROR");
			}
		}else {
			out("ChangePassFalse");
		}
	}
	public void ChangeName(String info){
		String a[] = info.split("<>");
		if (SQLConnection.getConnection().UPDateName(a[2],a[1])) {
			out("ChangeNameTrue");
		}else {
			out("ChangeNameERROR");
		}
	}
	public void sendChatRomInfo(String info){
		new Thread(){
			public void run() {
				String a[] = info.split("<>");
				Vector<String> vector = new Vector<>();
						vector.addElement(a[3]);
						vector.addElement(a[4]);
				ChatManger.getChatManger().CreateChatRom(vector);
				ChatManger.getChatManger().sendChatRomInfoToClient(info);
			};
		}.start();
	}
	public void ErrofflineChatRom(String ip){
		new Thread(){
			public void run() {
				for (int i = 0; i < ChatManger.getChatManger().ChatRomManger.size(); i++) {
					for (int j = 1; j < ChatManger.getChatManger().ChatRomManger.get(i).size(); j++) {
						String b[] =ChatManger.getChatManger().ChatRomManger.get(i).get(j).split("\\)") ;
						String c[] = b[0].split("\\(");
						if (c[1].equals(ip)) {
							ChatManger.getChatManger().ChatRomManger.get(i).remove(j);
						}
						if (ChatManger.getChatManger().ChatRomManger.get(i).size()==1) {
							ChatManger.getChatManger().ChatRomManger.remove(i);
						}
						
					}
				}
				for (int i = 0; i <ChatManger.getChatManger().ChatRomManger.size(); i++) {
					String updateChatRom="updateChatRom<>"+ChatManger.getChatManger().ChatRomManger.get(i).get(0);
					for (int j = 1; j < ChatManger.getChatManger().ChatRomManger.get(i).size(); j++) {
							updateChatRom = updateChatRom+"<>"+ChatManger.getChatManger().ChatRomManger.get(i).get(j);
						}
					for (int j = 1; j <ChatManger.getChatManger().ChatRomManger.get(i).size(); j++) {
						String b[] =ChatManger.getChatManger().ChatRomManger.get(i).get(j).split("\\)") ;
						String c[] = b[0].split("\\(");
						
						ChatManger.getChatManger().updateChatRom(c[1], updateChatRom);
					}

				}
				
			};
		}.start();
	}
	public void offlineChatRom(String line){
		new Thread(){
			public void run() {
				String a[] = line.split("<>");
				for (int i = 0; i < ChatManger.getChatManger().ChatRomManger.size(); i++) {
					if (ChatManger.getChatManger().ChatRomManger.get(i).get(0).equals(a[1])) {
						for (int j = 1; j < ChatManger.getChatManger().ChatRomManger.get(i).size(); j++) {
							if (ChatManger.getChatManger().ChatRomManger.get(i).get(j).equals(a[2])) {
								ChatManger.getChatManger().ChatRomManger.get(i).remove(j);
								if (ChatManger.getChatManger().ChatRomManger.get(i).size()==1) {
									ChatManger.getChatManger().ChatRomManger.remove(i);
								
								}
								break;
							}
		
						}
						String updateChatRom="updateChatRom<>"+a[1];
						Vector<String> ipList = new Vector<>();
						System.err.println("打印："+i);
						System.err.println("打印 ："+ChatManger.getChatManger().ChatRomManger.size());
							for (int j = 1; j < ChatManger.getChatManger().ChatRomManger.get(i).size(); j++) {
								updateChatRom = updateChatRom+"<>"+ChatManger.getChatManger().ChatRomManger.get(i).get(j);
								String b[] =ChatManger.getChatManger().ChatRomManger.get(i).get(j).split("\\)") ;
								String c[] = b[0].split("\\(");
						
								ipList.addElement(c[1]);
							}
							
							for (int j = 0; j <ipList.size(); j++) {
								System.out.println("updata:2");
								ChatManger.getChatManger().updateChatRom(ipList.get(j), updateChatRom);
								
							}
							
						break;
					}
				}
			};
		}.start();
	}
	public void updateChatRom(String line){
	System.out.println("updata:1");
		new Thread(){
			public void run() {
				String a[] = line.split("<>");
				for (int i = 0; i < ChatManger.getChatManger().ChatRomManger.size(); i++) {
					if (ChatManger.getChatManger().ChatRomManger.get(i).get(0).equals(a[1])) {
						ChatManger.getChatManger().ChatRomManger.get(i).addElement(a[2]);
						String updateChatRom="updateChatRom<>"+a[1];
						Vector<String> ipList = new Vector<>();
							for (int j = 1; j < ChatManger.getChatManger().ChatRomManger.get(i).size(); j++) {
								updateChatRom = updateChatRom+"<>"+ChatManger.getChatManger().ChatRomManger.get(i).get(j);
								String b[] =ChatManger.getChatManger().ChatRomManger.get(i).get(j).split("\\)") ;
								System.err.println(j+":"+ChatManger.getChatManger().ChatRomManger.get(i).get(j));

								String c[] = b[0].split("\\(");
						
								ipList.addElement(c[1]);
							}
							
							for (int j = 0; j <ipList.size(); j++) {
								ChatManger.getChatManger().updateChatRom(ipList.get(j), updateChatRom);
								
							}
							
						break;
					}
				}
			};
		}.start();
	}
}	
