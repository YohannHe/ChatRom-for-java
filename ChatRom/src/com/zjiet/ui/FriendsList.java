package com.zjiet.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.zjiet.function.ChatManger;
import com.zjiet.function.GetLocalIP;

import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FriendsList {

	private JFrame frame;
	JList list;
	String romName,romID;
	public FriendsList() {
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
				frame.dispose();
			}
		});
		frame.setTitle("\u9080\u8BF7\u597D\u53CB");
		frame.setType(Type.UTILITY);
		frame.setResizable(false);
		frame.setBounds(100, 100, 247, 358);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JButton button = new JButton("\u5173\u95ED");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
						.addComponent(button, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(button)
					.addContainerGap())
		);
		
		list = new JList();
		scrollPane.setViewportView(list);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(list, popupMenu);
		
		JMenuItem menuItem = new JMenuItem("\u9080\u8BF7");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ChatRomInfo = "";
				for (int i = 0; i < list.getSelectedValuesList().size(); i++) {
					String a[] =((String) list.getSelectedValuesList().get(i)).split("\\)") ;
					String c[] = a[0].split("\\(");
					if (i== list.getSelectedValuesList().size()-1) {
						ChatRomInfo =  ChatRomInfo+c[1];
					}else {
						ChatRomInfo =  ChatRomInfo+c[1]+"<>";
					}
					
				}
				if ( list.getSelectedValuesList().size()>0) {
					ChatManger.getCM().sendCmd("ChatRomInfo<>"+ChatManger.getCM().getName()+"<>"+romName+"<>"+romID+"<>"+ChatManger.getCM().getName()+"====("+GetLocalIP.getLocalIPForJava()+")"+"<>"+ChatRomInfo);
				}
				frame.dispose();
				
			}
		});
		popupMenu.add(menuItem);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
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
	public void setInfo(String romName,String romID){
		this.romID=romID;
		this.romName = romName;
	}
	
}
