package com.zjiet.ui;

import java.awt.EventQueue;
import java.awt.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.zjiet.function.ChatManger;
import com.zjiet.function.GetLocalIP;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;

public class Main {

	private JFrame frmChatrom;
	JLabel lblNewLabel;
	JList list;
	Vector vector =new Vector();
	static Vector ipchat = new Vector();
	private String userID;
	int ChatRomCount=0;
	private String ChatRomInfo;
	private String chatRomTitle;
	public Main() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatrom = new JFrame();
		frmChatrom.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int flag = JOptionPane.showConfirmDialog(null, "确定要退出吗?",  
                        "提示", JOptionPane.YES_NO_OPTION,  
                        JOptionPane.INFORMATION_MESSAGE);  
                if(JOptionPane.YES_OPTION == flag){  
                    System.exit(0);
                }
				
			}
		});
		frmChatrom.setResizable(false);
		frmChatrom.setTitle("ChatRom");
		frmChatrom.setBounds(100, 100, 277, 455);
		frmChatrom.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		lblNewLabel = new JLabel();
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(frmChatrom.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
		);
		
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					String chatting="NULL";
					if (ipchat.size()>0) {
						for (int i = 0; i < ipchat.size(); i++) {
							if (ipchat.get(i).equals(list.getSelectedValue())) {
								chatting="have";
							}
						}
						if (chatting.equals("NULL")) {
							ChatMainWin chatMainWin = new ChatMainWin();
							ChatManger.getCM().addChatWin(chatMainWin);
							String a[] =((String) list.getSelectedValue()).split("\\)") ;
							String c[] = a[0].split("\\(");
							chatMainWin.setInfo(c[1], (String) list.getSelectedValue());
							ipchat.addElement(((String) list.getSelectedValue()));
						}
					}else {
						
								ChatMainWin chatMainWin = new ChatMainWin();
					 			String a[] =((String) list.getSelectedValue()).split("\\)") ;
								String c[] = a[0].split("\\(");
								chatMainWin.setInfo(c[1], (String) list.getSelectedValue());
								ipchat.addElement(((String) list.getSelectedValue()));
								ChatManger.getCM().addChatWin(chatMainWin);
					}
					
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				
				
			}
		});
		scrollPane.setViewportView(list);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);
		
		JMenuItem menuItem = new JMenuItem("\u521B\u5EFA\u7FA4\u804A");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatRomInfo="";
				for (int i = 0; i < list.getSelectedValuesList().size(); i++) {
					String a[] =((String) list.getSelectedValuesList().get(i)).split("\\)") ;
					String c[] = a[0].split("\\(");
					if (i== list.getSelectedValuesList().size()-1) {
						ChatRomInfo =  ChatRomInfo+c[1];
					}else {
						ChatRomInfo =  ChatRomInfo+c[1]+"<>";
					}
					
				}
				if (list.getSelectedValuesList().size()>0) {
					while (true) {
						chatRomTitle = JOptionPane.showInputDialog("请输入聊天室名称：");	
						if (chatRomTitle==null) {
							break;
						}else if (!chatRomTitle.equals("")) {
							
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									ChatRom chatRom = new ChatRom();
									chatRom.setVisible();
									chatRom.setTitle(chatRomTitle);
									chatRom.listInit(frmChatrom.getTitle()+"====("+GetLocalIP.getLocalIPForJava()+")");
									ChatManger.getCM().addChatRomWin(chatRom);
									chatRom.setRomID(userID+":"+ChatRomCount);
									ChatManger.getCM().sendCmd("ChatRomInfo<>"+frmChatrom.getTitle()+"<>"+chatRomTitle+"<>"+userID+":"+ChatRomCount+++"<>"+frmChatrom.getTitle()+"====("+GetLocalIP.getLocalIPForJava()+")"+"<>"+ChatRomInfo);
								}
							});
							break;
						}
					}
				}

		
			}
		});
		
		popupMenu.add(menuItem);
		frmChatrom.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmChatrom.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("\u4FEE\u6539\u5BC6\u7801");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangePassUI();
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("\u4FEE\u6539\u6635\u79F0");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangeNameUI();
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		frmChatrom.setVisible(true);
	}
	
	public void setTitle(String name){
		frmChatrom.setTitle(name);
	}
	
	public void setCount(String count){
		lblNewLabel.setText(count);
	}
	
	public void addIP(String ip){
		vector.addElement(ip);
		list.setListData(vector);
	}
	
	public void delIP(String ip){
		for (int i = 0; i < vector.size(); i++) {
			String b =  (String) vector.get(i);
			if (b!=null) {
				String a[] = b.split("====");
				if(a[1].equals(ip)){
					
					vector.removeElementAt(i);
					list.setListData(vector);
				}	
			}
			new Thread(){
				public void run() {
					ChatManger.getCM().delOpenedChatwin(ip);	
				};
			}.start();
			
			
			
		} 
	}
	public void revMsgCreatWin(String ip){
		ChatMainWin chatMainWin = new ChatMainWin();
		for (int i = 0; i < vector.size(); i++) {
			String a[] =((String)vector.get(i)).split("\\)") ;
			String c[] = a[0].split("\\(");
			if (ip.equals(c[1])) {
				chatMainWin.setInfo(c[1], (String) vector.get(i));
				ipchat.addElement(((String) vector.get(i)));
				ChatManger.getCM().addChatWin(chatMainWin);
			}
		}
	
	}
	public String getTitle(){
		return frmChatrom.getTitle();
	}
	
	public void setUserID(String userID){
		this.userID=userID;
	}
	public String getUserID(){
		return userID;
	}
	public JList getList(){
		return list;
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
}
