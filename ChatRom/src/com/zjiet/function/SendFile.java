package com.zjiet.function;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.zjiet.ui.SendFileUI;


public class SendFile {
	ServerSocket serverSocket ;
	/**
	 * @wbp.parser.entryPoint
	 */
	public SendFile(int port,String ip,String title) { 
		new Thread(){
			public void run() {
				Send(port,ip,title);
			};
		}.start();
		
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	SendFileUI sendFileUI ;
	int length = 0;  
    double sumL = 0 ;  
    byte[] sendBytes = null;  
    
    DataOutputStream dos = null;  
    FileInputStream fis = null;  
    Socket socket = null;
    boolean bool = false;
	public void Send(int port,String ip,String title) {
	
        
        try {  
        	JFileChooser jChooser = new JFileChooser();
        	jChooser.showOpenDialog(null);
        	if (jChooser.getSelectedFile()!=null) {
        		File file = new File(jChooser.getSelectedFile().getPath()); //要传输的文件路径  
	            long l = file.length();  
	            ChatManger.getCM().SendFileInfo(GetLocalIP.getLocalIPForJava(), ip,port,file.getName(),l);
	            
	            EventQueue.invokeLater(new Runnable() {
	    			public void run() {
	    				try {
	    					sendFileUI = new SendFileUI();
	    					
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				}
	    			}
	    		});
	            try {
					Thread.sleep(500);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	            sendFileUI.setFileName(file.getName());
	            sendFileUI.setTitle("正在向 "+title+" 发送文件("+file.getName()+")");
	            try {
					Thread.sleep(500);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	            
	            serverSocket = new ServerSocket(port);
	            sendFileUI.setSocket(serverSocket,socket);
	            new Thread(){
	            	public void run() {
	            		int time = 0;
	            		boolean flag = true;
	            		
	            		while (flag) {
	            			time++;
							try {
								sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (time==30) {
								
								JOptionPane.showMessageDialog(null, "发送文件等待超时！对方没有回应！");
								try {
									sendFileUI.dispose();
									serverSocket.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								time=0;
								break;
							}else {
								for (int j = 0; j < ChatManger.getCM().FileAccept.size(); j++) {
									String info = ChatManger.getCM().FileAccept.get(j);
									String f[]  = info.split("<>");
									
										if (f[1].equals("Accept")&&f[2].equals(ip)) {
											flag=false;
											for(int i = 0; i < ChatManger.getCM().FileAccept.size(); i++){
												if ( ChatManger.getCM().FileAccept.get(i).equals(info)) {
													ChatManger.getCM().FileAccept.remove(i);
												}
											}
										}else if(f[1].equals("Refuse")&&f[2].equals(ip)){
											
											JOptionPane.showMessageDialog(null, "对方拒绝接受文件！");
											for(int i = 0; i < ChatManger.getCM().FileAccept.size(); i++){
												if ( ChatManger.getCM().FileAccept.get(i).equals(info)) {
													ChatManger.getCM().FileAccept.remove(i);
												}
											}
											try {
												sendFileUI.dispose();
												serverSocket.close();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											flag=false;
											break;
										}
									
								}
								
							}
							
						}
	            	};
	            }.start();
	            socket = serverSocket.accept();
	            dos = new DataOutputStream(socket.getOutputStream());  
	            fis = new FileInputStream(file);        
	            sendBytes = new byte[40960];
	            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
	            	if (sendFileUI.close()) {
						serverSocket.close();
						socket.close();
						sendFileUI.dispose();
					}
	                sumL += length;    
	                sendFileUI.setlable((int)((sumL/l)*100)+"%");
	                sendFileUI.setPRO((int)((sumL/l)*100));
	                dos.write(sendBytes, 0, length);  
	                dos.flush();  
	            } 
	            
	            //虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较  
	            if(sumL==l){  
	                bool = true;  
	            } 
	            
			}
           
        }catch (IOException e) {  
        	if (!sendFileUI.close()) {
        		JOptionPane.showMessageDialog(null, "出现错误，文件传输终止！");
			}
            sendFileUI.dispose();
            bool = false;  
            e.printStackTrace();    
        } finally{   
        	try {
            if (dos != null)
				dos.close();
            if (fis != null)  
                fis.close();     
            if (socket != null)  
                socket.close();   
        	} catch (IOException e) {
				e.printStackTrace();	
			} 
        	try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        }  
        
        if (bool) {
        	JOptionPane.showMessageDialog(null, "文件传输完成！");
        	sendFileUI.dispose();
        	for (int i = 0; i < ChatManger.getCM().sendFilePort.size(); i++) {
				if (ChatManger.getCM().sendFilePort.get(i)==port) {
					ChatManger.getCM().sendFilePort.removeElement(i);
				}
			}
        	
		}
	}

}
