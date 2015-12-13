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
        		File file = new File(jChooser.getSelectedFile().getPath()); //Ҫ������ļ�·��  
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
	            sendFileUI.setTitle("������ "+title+" �����ļ�("+file.getName()+")");
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
								
								JOptionPane.showMessageDialog(null, "�����ļ��ȴ���ʱ���Է�û�л�Ӧ��");
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
											
											JOptionPane.showMessageDialog(null, "�Է��ܾ������ļ���");
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
	            
	            //��Ȼ�������Ͳ�ͬ����JAVA���Զ�ת������ͬ�������ͺ������Ƚ�  
	            if(sumL==l){  
	                bool = true;  
	            } 
	            
			}
           
        }catch (IOException e) {  
        	if (!sendFileUI.close()) {
        		JOptionPane.showMessageDialog(null, "���ִ����ļ�������ֹ��");
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
        	JOptionPane.showMessageDialog(null, "�ļ�������ɣ�");
        	sendFileUI.dispose();
        	for (int i = 0; i < ChatManger.getCM().sendFilePort.size(); i++) {
				if (ChatManger.getCM().sendFilePort.get(i)==port) {
					ChatManger.getCM().sendFilePort.removeElement(i);
				}
			}
        	
		}
	}

}
