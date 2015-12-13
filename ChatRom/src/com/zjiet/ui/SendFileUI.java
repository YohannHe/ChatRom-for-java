package com.zjiet.ui;

import java.awt.EventQueue;


import javax.swing.JFrame;
import java.awt.Window.Type;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SendFileUI {

	private JFrame frame;
	private JProgressBar progressBar;
	private JLabel lblNewLabel;
	JLabel fileName;
	ServerSocket serverSocket;
	Socket socket;
	boolean close=false;
	public SendFileUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int flag = JOptionPane.showConfirmDialog(null, "文件还未传输完成，确定要退出吗?",  
                        "提示", JOptionPane.YES_NO_OPTION,  
                        JOptionPane.INFORMATION_MESSAGE);  
                if(JOptionPane.YES_OPTION == flag){  
                	close =true;
                	frame.dispose();
                	
                }
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 465, 110);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("\u53D6\u6D88");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int flag = JOptionPane.showConfirmDialog(null, "文件还未传输完成，确定要退出吗?",  
                        "提示", JOptionPane.YES_NO_OPTION,  
                        JOptionPane.INFORMATION_MESSAGE);  
                if(JOptionPane.YES_OPTION == flag){  
                	close=true;
                	frame.dispose();
                	
                }
				
			}
		});
		btnNewButton.setBounds(380, 53, 76, 23);
		frame.getContentPane().add(btnNewButton);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(18, 23, 365, 23);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		frame.getContentPane().add(progressBar);
		
		lblNewLabel = new JLabel("\u7B49\u5F85\u4E2D...");
		lblNewLabel.setBounds(397, 25, 54, 15);
		frame.getContentPane().add(lblNewLabel);
		
		fileName = new JLabel("\u6587\u4EF6\u540D\uFF1A");
		fileName.setBounds(21, 59, 362, 15);
		
		frame.getContentPane().add(fileName);
		frame.setVisible(true);
	}
	public void setTitle(String text){
		frame.setTitle(text);
	}
	public void setlable(String text) {
		lblNewLabel.setText(text);
	}
	public void setPRO(int num) {
		progressBar.setValue(num);
		
	}
	public void dispose(){
		frame.dispose();
	}
	public void setFileName(String text){
		fileName.setText("文件名："+text);
	}
	public void setSocket(ServerSocket serverSocket,Socket socket) {
		this.serverSocket=serverSocket;
		this.socket = socket;
	}
	public boolean close(){
		return close;
	}
}
