package com.zjiet.chatFunction;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class FunctionServerListenner extends Thread {
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("�������Ӹö˿ڣ�");
				System.out.println(socket.getInetAddress());
				FuntionSocket cs = new FuntionSocket(socket);
						cs.start();
						FunctionManger.getFunctionManger().add(cs);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
