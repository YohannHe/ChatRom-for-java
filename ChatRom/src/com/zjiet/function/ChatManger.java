package com.zjiet.function;
import java.awt.EventQueue;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import com.zjiet.empty.UserInfo;
import com.zjiet.ui.ChatMainWin;
import com.zjiet.ui.ChatRom;
import com.zjiet.ui.Main;

public class ChatManger {
	private ChatManger(){}
	private static final ChatManger  instance = new ChatManger();
	public static ChatManger getCM(){
		return instance;
	}
	Main mainWindow;
	Socket socket;
	BufferedReader reader;
	BufferedWriter writer;
	String data ;
	String name;
	String fileinfo;
	String changeinfo;
	String count;
	List iplist =new List();
	Vector<ChatMainWin> chatMainWins = new Vector<>() ;
	Vector<ChatRom> chatRoms = new Vector<>();
	public Vector<Integer> sendFilePort = new  Vector<>();
	public Vector<String> FileAccept = new Vector<>();
 	public void setWindow(Main main){
		this.mainWindow = main;
	}
	public void addChatWin(ChatMainWin chatMainWin){
		chatMainWins.addElement(chatMainWin);
	}
	public void delChatWin(ChatMainWin chatMainWin){
		for (int i = 0; i < chatMainWins.size(); i++) {
			if (chatMainWins.get(i).equals(chatMainWin)) {
				chatMainWins.remove(i);
			}
		}
	}
	public void delChatRomWin(ChatRom chatRom){
		for (int i = 0; i < chatRoms.size(); i++) {
			if (chatRoms.get(i).equals(chatRom)) {
				chatRoms.remove(i);
			}
		}
	}
	public void addChatRomWin(ChatRom chatRom){
		chatRoms.addElement(chatRom);
	}
	public String getLocalIP(){
		return socket.getLocalAddress().getHostAddress();
	}
	
	public void delOpenedChatwin(String titleIP){
		for (int i = 0; i < chatMainWins.size(); i++) {
			String ip = "("+chatMainWins.get(i).getIP()+")";
			if (ip.equals(titleIP)) {
				chatMainWins.get(i).close();
				chatMainWins.remove(i);
			}
		}
	}
	//test for git

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
					System.out.println("cm准备接收数据");
					while ((line=reader.readLine())!=null) {
						System.out.println("CM:"+line);
						changeinfo = line;
						if (line.length()>=5) {
							if (line.substring(0, 5).equals("Fired")) {
								chatRomFired(line);
							}
						}
						if (line.length()>=6) {
							if (line.substring(0, 6).equals("ROMMSG")) {
								appendChatRomMsg(line);
							}
						}
						if (line.length()>=11) {
							if (line.substring(0, 11).equals("ChatRomInfo")) {
								joinChatRom(line);
							}
						}
						if (line.length()>=13) {
							if (line.substring(0,13).equals("updateChatRom")) {
								updateChatRom(line);
							}
						}
						new Thread(){
							public void run() {
								switch (changeinfo) {
								case "ChangePassTrue":
									JOptionPane.showMessageDialog(null, "密码修改成功！");
									break;
								case "ChangePassERROR":
									JOptionPane.showMessageDialog(null, "发生错误，密码修改失败！");
									break;
								case "ChangePassFalse":
									JOptionPane.showMessageDialog(null, "密码验证失败！");
									break;
								case "ChangeNameTrue":
									JOptionPane.showMessageDialog(null, "昵称修改成功！");
									break;
								case "ChangeNameERROR":
									JOptionPane.showMessageDialog(null, "发生错误，昵称修改失败！");
									break;
								}
							};
						}.start();
						if (line.length()>=8) {
							if (line.substring(0, 8).equals("FileInfo")) {
								fileinfo = line;
								new Thread(){
										public void run() {
											doReSaveFile(fileinfo);
										};
									}.start();
								
								
							}
						}
						if (line.length()>=10) {
							if (line.substring(0, 10).equals("FileAccept")) {
								String a[] = line.split("<>");
								System.out.println(line);
								FileAccept.addElement(line);
							}
						}
						if (line.length()>=8) {
							if (line.substring(0, 8).equals("IPOnLine")) {
								
								String a[] = line.split("<>");
								
								if (mainWindow!=null) {
									mainWindow.addIP(a[2]+"====("+a[1]+")");
								}else {
									iplist.add(a[2]+"====("+a[1]+")");
								}
								
							}
						}
						if (line.length()>=11) {
							if (line.substring(0, 11).equals("UPDataCount")) {
								UPData(line);
								if (mainWindow!=null) {
									mainWindow.setCount(count);
								}
							}
							
						}
						if (line.length()>=13) {
							if (line.substring(0, 13).equals("{userOffline}")) {
								String a[] = line.split("<>");
								mainWindow.delIP("("+a[1]+")");
								for(int i=0;i<chatMainWins.size();i++){
									if (chatMainWins.get(i).getIP().equals(a[1])) {
										chatMainWins.get(i).delChatWIn();
									}
								}
							}
						}
						if (line.substring(0, 3).equals("MSG")) {
							String a[] = line.split("<>");
							String flag="null";
							for(int i=0;i<chatMainWins.size();i++){
								if (chatMainWins.get(i).getIP().equals(a[1])) {
									chatMainWins.get(i).appendText(a[2]);
									flag="have";
								}
							}
							if (flag.equals("null")) {
								mainWindow.revMsgCreatWin(a[1]);
								for(int i=0;i<chatMainWins.size();i++){
									if (chatMainWins.get(i).getIP().equals(a[1])) {
										chatMainWins.get(i).appendText(a[2]);
										
									}
								}
							}
							
						}
						
						//mainWindow.appendText("收到："+line+"\n");
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
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
	public void sendMsg(String ip,String text){
		
		BufferedReader br = new BufferedReader(new StringReader(text));
		String line = "";
		try {
			while ((line=br.readLine())!=null) {
				if (!line.equals("")) {
					String msg = "MSG<>"+ip+"<>"+line;
					writer.write(msg+"\n");
					writer.flush();
				}

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//VSC 测试推送github
	public void sendChatRomMsg(String ip,String text){
		
		BufferedReader br = new BufferedReader(new StringReader(text));
		String line = "";
		try {
			while ((line=br.readLine())!=null) {
				if (!line.equals("")) {
					String msg = "ROMMSG<>"+ip+"<>"+line;
					writer.write(msg+"\n");
					writer.flush();
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SendFileInfo(String fromIp ,String toip, int fromPort,String fileName,Long filelength) {
		try {
			
			writer.write("FileInfo<>"+mainWindow.getTitle()+"<>"+fromIp+"<>"+fromPort+"<>"+fileName+"<>"+filelength+"<>"+toip+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void doReSaveFile(String fileinfo){
		String a[] = fileinfo.split("<>");
		int flag = JOptionPane.showConfirmDialog(null, a[1]+"向你发送文件("+a[4]+")是否接收？",  
                "提示", JOptionPane.YES_NO_OPTION,  
                JOptionPane.INFORMATION_MESSAGE);  
        if(JOptionPane.YES_OPTION == flag){  
        	
        	new ReSaveFile(fileinfo);
        	
        	
        }else {
	
				sendCmd("FileAccept<>"+"Refuse"+"<>"+GetLocalIP.getLocalIPForJava()+"<>"+a[2]);
		
		}
	}
	public void appendChatRomMsg(String line){
		new Thread(){
			public void run() {
				String a[] = line.split("<>");
				for (int i = 0; i < chatRoms.size(); i++) {
					if (chatRoms.get(i).getRomID().equals(a[1])) {
						chatRoms.get(i).appendText(line);
						break;
					}
					
				}
			};
		}.start();
	}
	public void sendCmd(String cmd){
		try {
			writer.write(cmd+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//设置主界面 用户名和当前在线人数
	public void setting(){
		
		mainWindow.setCount(count);
		mainWindow.setTitle(name);
		for (int i = 0; i < iplist.getItemCount(); i++) {
			mainWindow.addIP(iplist.getItem(i));
		}
		
	}
	public String getUserID(){
		return mainWindow.getUserID();
	}
	public void UPData(String data){
		if (data.substring(0,11).equals("UPDataCount")) {
			String a[] = data.split("<>");
			count="当前在线人数:"+a[1];
		}
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public JList getList(){
		return mainWindow.getList();
	}
	public void updateChatRom(String ip){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		String a[] = ip.split("<>");
		List list = new List();
		for (int i = 0; i < chatRoms.size(); i++) {
			if (chatRoms.get(i).getRomID().equals(a[1])) {
				for (int j = 2; j< a.length; j++) {
					list.add(a[j]);
				}
				chatRoms.get(i).updateChatRom(list);
				break;
			}
		}
		
		
	}
	public void chatRomFired(String line){
		new Thread(){
			public void run() {
				String a[] = line.split("<>");
				for (int i = 0; i < chatRoms.size(); i++) {
					if (chatRoms.get(i).getRomID().equals(a[1])) {
						chatRoms.get(i).dispose();
						JOptionPane.showMessageDialog(null, "您被移除聊天室("+chatRoms.get(i).getTitle()+")");
						chatRoms.removeElementAt(i);
					}
				}
			};
		}.start();
	
	}
	public void joinChatRom(String info){
		new Thread(){
			public void run() {
				String a[] = info.split("<>");
				int flag = JOptionPane.showConfirmDialog(null, a[1]+"邀请你加入聊天室("+a[2]+")是否接受？",  
		                "提示", JOptionPane.YES_NO_OPTION,  
		                JOptionPane.INFORMATION_MESSAGE);  
				if (JOptionPane.YES_OPTION==flag) {	
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							ChatRom chatRom = new ChatRom();
							chatRom.setVisible();
							addChatRomWin(chatRom);
							chatRom.setRomID(a[3]);
							chatRom.setTitle(a[2]);	
						}
					});
					String self = ChatManger.getCM().mainWindow.getTitle()+"====("+GetLocalIP.getLocalIPForJava()+")";
					sendCmd("joinChatRom<>"+a[3]+"<>"+self);
				}
			};
		}.start();
	}

}
