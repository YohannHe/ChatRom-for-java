package com.zjiet.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.zjiet.empty.UserInfo;
import com.zjiet.function.ChatManger;
import com.zjiet.function.FunctionManger;
import com.zjiet.function.LoginUIFuntion;


import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.awt.event.ActionEvent;

public class Login {

	private JFrame frmChatrom;
	private JTextField textField;
	private JPasswordField passwordField;
	JButton btnNewButton,btnNewButton_1;
	int time = 0;
	String ok = "no";
	private static final String IP="192.5.0.101";//172.30.123.88  172.30.108.101
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
//				try {
//					ServerSocket serverSocket = new ServerSocket(12341);
//			
//				} catch (IOException e) {
//				
//					JOptionPane.showMessageDialog(null, "客户端已启动，本次启动失败！");
//					System.exit(0);
//				}
					Login window = new Login();
					FunctionManger.getCM().setLoginWindow(window);
					window.frmChatrom.setVisible(true);
					FunctionManger.getCM().connect(IP,12345);
					
					
			}
		});
	}

	/**
	 * Create the application.
	 */ 
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmChatrom = new JFrame();
		frmChatrom.setTitle("ChatRom(v1.0.0)");
		frmChatrom.setResizable(false);
		frmChatrom.setBounds(100, 100, 454, 313);
		frmChatrom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatrom.getContentPane().setLayout(null);
		frmChatrom.setLocationRelativeTo(null);
		btnNewButton = new JButton("\u767B\u5F55");
		
		btnNewButton.setBounds(126, 172, 93, 23);
		frmChatrom.getContentPane().add(btnNewButton);
		
		btnNewButton_1 = new JButton("\u6CE8\u518C");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Regist window = new Regist();	
							FunctionManger.getCM().setRegWin(window);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnNewButton_1.setBounds(229, 172, 93, 23);
		frmChatrom.getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(124, 60, 198, 21);
		frmChatrom.getContentPane().add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(124, 103, 198, 21);
		frmChatrom.getContentPane().add(passwordField);
		
		JLabel label = new JLabel("\u8D26\u53F7\uFF1A");
		label.setBounds(60, 63, 54, 15);
		frmChatrom.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801\uFF1A");
		label_1.setBounds(60, 106, 54, 15);
		frmChatrom.getContentPane().add(label_1);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals("")&&!passwordField.getText().equals("")){
					UserInfo userInfo = new UserInfo();
					userInfo.setUserID(textField.getText());
					userInfo.setUserPass(passwordField.getText());
					LoginUIFuntion loginUIFuntion = new LoginUIFuntion();
					loginUIFuntion.CheckPass(userInfo);
					btnNewButton.setEnabled(false);
					
					new Thread(){
						public void run() {
							while (true) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								time++;
								if (time>=30) {
									JOptionPane.showMessageDialog(null, "连接超时！");
									btnNewButton.setEnabled(true);;
									break;
								}else if (!ok.equals("no")) {
									time=0;
									btnNewButton.setEnabled(true);
									ok =  "no";
									break;
								}
							}
						};
					}.start();
				}else{
					JOptionPane.showMessageDialog(null, "账号密码不能为空！");
				}
			}
		});
	}
	public void disable(){
		btnNewButton.setEnabled(false);
		btnNewButton_1.setEnabled(false);
	}
	public void checkPass(String login){
		if(login.equals("True")){
			ok = "yes";
			JOptionPane.showMessageDialog(null, "验证成功！");
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						
						new Thread(){
							public void run() {
								ChatManger.getCM().connect(IP,12346);
							};
						}.start();
						
						Thread.sleep(700);
						ChatManger.getCM().send("@/0GetIPOnline/0@");
						frmChatrom.dispose();
						
						Thread.sleep(350);
						Main window = new Main();
						Thread.sleep(350);
						ChatManger.getCM().setWindow(window);
						window.setUserID(textField.getText());
						ChatManger.getCM().setting();
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}else if(login.equals("False")){
			ok="yes";
			JOptionPane.showMessageDialog(null, "账号或密码错误！");
		}else if (login.equals("OnLineITS")) {
			ok = "yes";
			JOptionPane.showMessageDialog(null, "此账户已在线，不允许重复登录！");
		}
	}
}
