package com.zjiet.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.UIManager;

import com.mysql.jdbc.CharsetMapping;
import com.zjiet.function.ChatManger;
import com.zjiet.function.SendFile;

import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatMainWin {

	private JFrame frame;
	private String ip;
	private String title;
	private JTextArea appendtextArea,sendtextArea;

	public ChatMainWin() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
	
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int flag = JOptionPane.showConfirmDialog(null, "确定要退出吗?",  
                        "提示", JOptionPane.YES_NO_OPTION,  
                        JOptionPane.INFORMATION_MESSAGE);  
                if(JOptionPane.YES_OPTION == flag){  
                    frame.dispose();  
                    for(int i=0;i<Main.ipchat.size();i++){
                    	if (Main.ipchat.get(i).equals(title)) {
							Main.ipchat.removeElementAt(i);
						}
                    }
                    delChatwin();
                } 
			}
		});
		
		frame.setBounds(100, 100, 487, 388);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JButton closeButton = new JButton("\u5173\u95ED");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				 for(int i=0;i<Main.ipchat.size();i++){
                 	if (Main.ipchat.get(i).equals(title)) {
							Main.ipchat.removeElementAt(i);
						}
                 }
				delChatwin();
			}
		});
		
		JButton sendButton = new JButton("\u53D1\u9001");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!sendtextArea.getText().equals("")) {
					ChatManger.getCM().sendMsg(ip,sendtextArea.getText());
					Date date=new Date(System.currentTimeMillis());
					DateFormat format=new SimpleDateFormat("HH:mm:ss");
					String time=format.format(date);
					appendtextArea.setText(appendtextArea.getText()+"我说:"+" "+time+"\n");
					appendtextArea.setText(appendtextArea.getText()+sendtextArea.getText()+"\n");
					sendtextArea.setText("");
				}else {
					JOptionPane.showMessageDialog(null, "发送内容不能为空！");
				}
				
				
			}
		});
		sendButton.setMnemonic(java.awt.event.KeyEvent.VK_ENTER); 
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JButton sendFile = new JButton("\u53D1\u9001\u6587\u4EF6");
		sendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int Port = 0;
				if (ChatManger.getCM().sendFilePort.size()==0) {
					Port=25000;
					ChatManger.getCM().sendFilePort.addElement(Port);
				}else {
					boolean flag=true;
					while (flag) {
						Port = (int)(25000+Math.random()*(26000-25000+1));
						for (int i = 0; i <ChatManger.getCM().sendFilePort.size(); i++) {
							if (Port==ChatManger.getCM().sendFilePort.get(i)) {
								flag = true;
								break;
							}else {
								flag =false;
							}
						}
					}
					ChatManger.getCM().sendFilePort.addElement(Port);
					
				}
				
				if (Port!=0) {
					final int a = Port;
					System.out.println("端口是："+Port);
					new Thread(){
						public void run() {
							
							new SendFile(a,ip,title);
						};
					}.start();
					
				}
				
				
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(sendFile)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(sendButton)
						.addComponent(closeButton)
						.addComponent(sendFile))
					.addContainerGap())
		);
		
		appendtextArea = new JTextArea();
		appendtextArea.setBackground(UIManager.getColor("Button.light"));
		scrollPane_1.setViewportView(appendtextArea);
		
		sendtextArea = new JTextArea();
		sendtextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==e.VK_ENTER) {
					if (!sendtextArea.getText().equals("")) {
						ChatManger.getCM().sendMsg(ip,sendtextArea.getText());
						Date date=new Date(System.currentTimeMillis());
						DateFormat format=new SimpleDateFormat("HH:mm:ss");
						String time=format.format(date);
						appendtextArea.setText(appendtextArea.getText()+"我说:"+" "+time+"\n");
						appendtextArea.setText(appendtextArea.getText()+sendtextArea.getText()+"\n");
						sendtextArea.setText("");
					}else {
						JOptionPane.showMessageDialog(null, "发送内容不能为空！");
					}
					
					
				}
			}
		});
		sendtextArea.setBackground(UIManager.getColor("Button.light"));
		scrollPane.setViewportView(sendtextArea);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		sendtextArea.setFocusable(true);
	}
	public void setInfo(String ip,String title){
		this.ip=ip;
		this.title=title;
		frame.setTitle("正在与"+title+"聊天");
	}
	public void delChatwin(){
		 ChatManger.getCM().delChatWin(this);
	}
	
	public String getIP(){
		
		return this.ip;
	}
	public void appendText(String text){
		Date date=new Date(System.currentTimeMillis());
		DateFormat format=new SimpleDateFormat("HH:mm:ss");
		String time=format.format(date);
		appendtextArea.setText(appendtextArea.getText()+title+" "+time+"\n");
		appendtextArea.setText(appendtextArea.getText()+text+"\n");
		appendtextArea.setCaretPosition(appendtextArea.getText().length());
	}
	
	public void close(){
		JOptionPane.showMessageDialog(null, title+" "+"已下线！");
		frame.dispose();
	}
	
	public void delChatWIn() {
		frame.dispose();
		 for(int i=0;i<Main.ipchat.size();i++){
        	if (Main.ipchat.get(i).equals(title)) {
					Main.ipchat.removeElementAt(i);
				}
        }
		delChatwin();
	}
}
