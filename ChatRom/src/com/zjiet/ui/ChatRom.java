package com.zjiet.ui;

import java.awt.EventQueue;
import java.awt.List;

import javax.swing.JFrame;
import javax.management.loading.PrivateClassLoader;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.zjiet.function.ChatManger;
import com.zjiet.function.GetLocalIP;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatRom {

	private JFrame frame;
	private JList list; 
	private JTextArea appendTextArea,sendTextArea;
	
	private String romID;
	Vector vector = null;

	public ChatRom() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 577, 427);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JButton sendButton = new JButton("\u53D1\u9001");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!sendTextArea.getText().trim().equals("")) {
					Date date=new Date(System.currentTimeMillis());
					DateFormat format=new SimpleDateFormat("HH:mm:ss");
					String time=format.format(date);
					ChatManger.getCM().sendChatRomMsg(romID+"<>"+GetLocalIP.getLocalIPForJava()+"<>"+time+"<>"+ChatManger.getCM().getName(),sendTextArea.getText());
					appendTextArea.setText(appendTextArea.getText()+"我说:"+" "+time+"\n");
					appendTextArea.setText(appendTextArea.getText()+sendTextArea.getText()+"\n");
					sendTextArea.setText("");
					sendTextArea.setCaretPosition(0);
				}else {
					JOptionPane.showMessageDialog(null, "发送内容不能为空！");
					sendTextArea.setText("");
				}
				
			}
		});
		
		JButton closeButton = new JButton("\u9000\u51FA");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int flag = JOptionPane.showConfirmDialog(null, "确定要退出聊天室吗?",  
                        "提示", JOptionPane.YES_NO_OPTION,  
                        JOptionPane.INFORMATION_MESSAGE);  
                if(JOptionPane.YES_OPTION == flag){  
                   frame.dispose();
                   ChatManger.getCM().sendCmd("ChatRomOffline<>"+romID+"<>"+ChatManger.getCM().getName()+"====("+GetLocalIP.getLocalIPForJava()+")");
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
							.addComponent(closeButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sendButton))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(sendButton)
								.addComponent(closeButton))))
					.addContainerGap())
		);
		
		list = new JList();
		scrollPane_1.setViewportView(list);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);
		
		JMenuItem menuItem = new JMenuItem("\u9080\u8BF7");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag = false;
				Vector<String> vector  =new Vector<>();
				FriendsList friendsList = new FriendsList();
				friendsList.setInfo(frame.getTitle(),romID);
				for (int i = 0; i < ChatManger.getCM().getList().getModel().getSize(); i++) {
					for (int j = 0; j < list.getModel().getSize(); j++) {
						if (ChatManger.getCM().getList().getModel().getElementAt(i).equals(list.getModel().getElementAt(j))) {
							flag = true;
						}
					}
					if (!flag) {
						vector.addElement(ChatManger.getCM().getList().getModel().getElementAt(i).toString());
					}
					flag =false;
				}
				friendsList.list.setListData(vector);
			}
		});
		popupMenu.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("\u79FB\u9664");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String a[]  = ((String)list.getSelectedValue()).split("====");
				String b[] =romID.split(":");
				if (b[0].equals(ChatManger.getCM().getUserID())) {
					if (!a[0].equals(ChatManger.getCM().getName())&&((String)list.getSelectedValue())!=null) {
						 ChatManger.getCM().sendCmd("removethisone<>"+romID+"<>"+list.getSelectedValue());
					}else {
						JOptionPane.showMessageDialog(null,"不能移除自己！");
					}
				}else {
					JOptionPane.showMessageDialog(null, "您无权限进行此操作！");
				}
				
				
			}
		});
		popupMenu.add(menuItem_1);
		
		 sendTextArea = new JTextArea();
		 sendTextArea.addKeyListener(new KeyAdapter() {
		 	@Override
		 	public void keyReleased(KeyEvent e) {
		 		if (e.getKeyCode()==e.VK_ENTER) {
		 			if (!sendTextArea.getText().trim().equals("")) {
						Date date=new Date(System.currentTimeMillis());
						DateFormat format=new SimpleDateFormat("HH:mm:ss");
						String time=format.format(date);
						ChatManger.getCM().sendChatRomMsg(romID+"<>"+GetLocalIP.getLocalIPForJava()+"<>"+time+"<>"+ChatManger.getCM().getName(),sendTextArea.getText());
						appendTextArea.setText(appendTextArea.getText()+"我说:"+" "+time+"\n");
						appendTextArea.setText(appendTextArea.getText()+sendTextArea.getText()+"\n");
						sendTextArea.setText("");
					}else {
						JOptionPane.showMessageDialog(null, "发送内容不能为空！");
						sendTextArea.setText("");
					}
					
		 			
		 		}
		 		
		 		
		 	}
		 	
		 	
		 });
		scrollPane_2.setViewportView(sendTextArea);
		
		appendTextArea = new JTextArea();
		scrollPane.setViewportView(appendTextArea);
		frame.getContentPane().setLayout(groupLayout);
		
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	public void setTitle(String title){
		frame.setTitle(title);
	}
	public void setVisible(){
		frame.setVisible(true);
	}
	public void listInit(String text){
		vector = new Vector<>();
		vector.addElement(text);
		list.setListData(vector);
		vector = null;
	}
	public String getRomID() {
		return romID;
	}

	public void setRomID(String romID) {
		this.romID = romID;
	}
	public void updateChatRom(List iplist){
		if (vector!=null) {
			vector.removeAllElements();
		}else {
			vector = new Vector<>();
		}

		for (int i = 0; i < iplist.getItemCount(); i++) {
			
			vector.addElement(iplist.getItem(i));
		}
		list.setListData(vector);
		System.err.println("update List");
	}
	public void appendText(String line){
		String a[] = line.split("<>");
		appendTextArea.setText(appendTextArea.getText()+a[4]+"("+a[3]+"):"+"\n");
		appendTextArea.setText(appendTextArea.getText()+a[5]+"\n\n");
		appendTextArea.setCaretPosition(appendTextArea.getText().length());
		
	}
	
	public void dispose(){
		frame.dispose();
	}
	public String getTitle(){
		return frame.getTitle();
	}
}
