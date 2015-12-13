package com.zjiet.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import javax.swing.event.CaretListener;

import com.zjiet.function.ChatManger;

import javax.swing.event.CaretEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangePassUI {

	private JFrame frame;
	private JPasswordField oldPass;
	private JPasswordField newPass;
	private JPasswordField reNewPass;
	private JButton btnNewButton;
	JLabel newPassLable,reNewPassLable;
	public ChangePassUI() {
		initialize();
	}

	
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
		frame.setResizable(false);
		frame.setTitle("\u4FEE\u6539\u5BC6\u7801");
		frame.setBounds(100, 100, 359, 263);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		oldPass = new JPasswordField();
		
		newPass = new JPasswordField();
		newPass.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (!reNewPass.getText().equals("")) {
					if (newPass.getText().equals(reNewPass.getText())) {
						newPassLable.setText("");
						reNewPassLable.setText("");
						btnNewButton.setEnabled(true);
					}else {
						newPassLable.setText("两次密码不一致！");
						btnNewButton.setEnabled(false);
					}
				}
			}
		});
	
		reNewPass = new JPasswordField();
		reNewPass.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (!newPass.getText().equals("")) {
					if (newPass.getText().equals(reNewPass.getText()) ) {
						reNewPassLable.setText("");
						newPassLable.setText("");
						btnNewButton.setEnabled(true);
					}else {
						reNewPassLable.setText("两次密码不一致！");
						btnNewButton.setEnabled(false);
					}
				}
				
			}
		});
		JLabel lblNewLabel = new JLabel("\u65E7\u5BC6\u7801\uFF1A");
		
		JLabel lblNewLabel_1 = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
		
		JLabel lblNewLabel_2 = new JLabel("\u91CD\u590D\u5BC6\u7801\uFF1A");
		
		btnNewButton = new JButton("\u786E\u5B9A\u4FEE\u6539");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (oldPass.getText().equals("")||newPass.getText().equals("")||reNewPass.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "信息没有填写全！");
				}else {
					String userid = ChatManger.getCM().getUserID();
					ChatManger.getCM().sendCmd("ChangePass<>"+userid+"<>"+oldPass.getText()+"<>"+newPass.getText());
				}
				
			}
		});
		
		newPassLable = new JLabel(" ");
		
		reNewPassLable = new JLabel(" ");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(48)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel_2))
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(reNewPassLable, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton, Alignment.LEADING)
						.addComponent(reNewPass, Alignment.LEADING)
						.addComponent(newPass, Alignment.LEADING)
						.addComponent(oldPass, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
						.addComponent(newPassLable, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(61, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(oldPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(newPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(newPassLable)
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(reNewPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(reNewPassLable)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
}
