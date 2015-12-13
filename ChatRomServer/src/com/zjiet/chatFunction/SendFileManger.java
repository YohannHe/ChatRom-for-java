package com.zjiet.chatFunction;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javax.swing.JFileChooser;

public class SendFileManger {
	
	
	public SendFileManger() {
		
	}
	
	public void Resave(String toip,String fromip,Socket fromsocket,Socket tosocket) {
		System.out.println("SendFileManger");
				   byte[] inputByte = null;  
			        int length = 0;  
			        DataInputStream dis = null;  
			        DataOutputStream dos = null;
			        try {  
			            try {  
			                dis = new DataInputStream(fromsocket.getInputStream());  
			                dos = new DataOutputStream(tosocket.getOutputStream());  
			                inputByte = new byte[40960];
			                while (dis.read(inputByte, 0, inputByte.length) > 0) {  
			                    dos.write(inputByte);
			                    dos.flush();
			                }  
			            } catch(IOException e) {  
			       
			                e.printStackTrace(); 
			            }  
			        } catch (Exception e) {  
			            e.printStackTrace();  
			        }    
				
	
            
    }  
 
  


}
