package com.zjiet.chatFunction;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ChatServerListenner extends Thread {
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(12346);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("����·���������Ӹö˿ڣ�");
				ChatSocket cs = new ChatSocket(socket);
						cs.start();
						ChatManger.getChatManger().add(cs);
					//ChatManger.getChatManger().publish(cs, "���Ѿ����뱾��������");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
