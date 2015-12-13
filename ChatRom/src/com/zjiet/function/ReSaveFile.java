package com.zjiet.function;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.zjiet.ui.SendFileUI;

public class ReSaveFile {
	SendFileUI sendFileUI;
	int length;
	double count;
	double maxlen;
	public ReSaveFile(String fileinfo) {
		new Thread(){
			public void run() {
				SaveFile(fileinfo);
			};
		}.start();
	}
	public SendFileUI CreatUI(){
		return new SendFileUI(); 
	}
	public void SaveFile(String fileinfo) {
			String a[] = fileinfo.split("<>");
			String b[] = fileinfo.split("\\.");
			maxlen= Double.parseDouble(a[5]);
			byte[] inputByte = null;  
	        Socket socket = null;
	        DataInputStream dis = null;  
	        FileOutputStream fos = null;  
	        File file = null;
	        try {  
	            try {  
	            	
	                
	                JFileChooser jFileChooser = new JFileChooser();
	                jFileChooser.setSelectedFile(new File(a[4]));
	                int y = jFileChooser.showSaveDialog(null);
	                /*   
	                 * 文件存储位置   
	                 */  
	        
	                if (y==jFileChooser.CANCEL_OPTION) {
						ChatManger.getCM().sendCmd("FileAccept<>"+"Refuse"+"<>"+GetLocalIP.getLocalIPForJava()+"<>"+a[2]);
					}else {
						ChatManger.getCM().sendCmd("FileAccept<>"+"Accept"+"<>"+GetLocalIP.getLocalIPForJava()+"<>"+a[2]);
						file = new File(jFileChooser.getSelectedFile().getPath()); 
//						  if (jFileChooser.getSelectedFile().getPath().split("\\.").length==0) {
//			                	// fos = new FileOutputStream(new File(jFileChooser.getSelectedFile().getPath()+"."+b[b.length-1])); 
//							}else{
//								fos = new FileOutputStream(new File(jFileChooser.getSelectedFile().getPath()));
//							}
						SwingUtilities.invokeLater(new Runnable() {
			    			public void run() {
			    				try {
			    					sendFileUI= CreatUI();
			    					
			    				} catch (Exception e) {
			    					e.printStackTrace();
			    				}
			    			}
			    		});
						fos = new FileOutputStream(file);
			                socket = new Socket(a[2], Integer.parseInt(a[3]));
			                dis = new DataInputStream(socket.getInputStream()); 
			                inputByte = new byte[40960];     
			                Thread.sleep(1000);
			                sendFileUI.setFileName(file.getName());
				            sendFileUI.setTitle("正在接收来自 "+a[1]+"发送的文件("+file.getName()+")");
			                while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {  
			                	if (sendFileUI.close()) {
									socket.close();
									sendFileUI.dispose();
								}
			                	count = count+length;
					    		sendFileUI.setlable((int)((count/maxlen)*100)+"%");
								sendFileUI.setPRO((int)((count/maxlen)*100));
			                    fos.write(inputByte, 0, length); 
			                    fos.flush();      
			                }  
			               JOptionPane.showMessageDialog(null, "完成接收："+jFileChooser.getSelectedFile().getPath());
			               sendFileUI.dispose();
					}
	              
	            } finally {  
	                if (fos != null)  
	                    fos.close();  
	                if (dis != null)  
	                    dis.close();  
	                if (socket != null)  
	                    socket.close();   
	            }  
	        } catch (Exception e) { 
	        	e.printStackTrace();
	        	if (!sendFileUI.close()) {
	        		JOptionPane.showMessageDialog(null, "由于种种原因，文件传输中断！");
				}
	        	sendFileUI.dispose();
	        	if (file.exists()) {
					file.delete();
				}
	            e.printStackTrace();  
	        }  
	}
	


}
