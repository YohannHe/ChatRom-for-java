package com.zjiet.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.zjiet.empty.UserInfo;
import com.zjiet.function.FunctionManger;
import com.zjiet.function.LoginUIFuntion;

import javax.swing.event.CaretEvent;
import javax.swing.JPasswordField;

public class Regist {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;


	public Regist() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("\u6CE8\u518C");
		frame.setBounds(100, 100, 322, 335);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		JButton btnNewButton = new JButton("\u6CE8\u518C");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserName(textField.getText());
				userInfo.setUserPass(passwordField_1.getText());
				LoginUIFuntion loginUIFuntion = new LoginUIFuntion();
				loginUIFuntion.AddNewUser(userInfo);
			}
		});
		btnNewButton.setEnabled(false);
		btnNewButton.setBounds(42, 222, 93, 23);
		frame.getContentPane().add(btnNewButton);
		JButton btnNewButton_1 = new JButton("\u91CD\u7F6E");
		
		btnNewButton_1.setBounds(169, 222, 93, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		textField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (!textField.getText().equals("")&&!passwordField.getText().equals("")&&!passwordField_1.getText().equals("")) {
					if (passwordField.getText().equals(passwordField_1.getText())&&!passwordField.getText().equals("")) {
						btnNewButton.setEnabled(true);
					}
					
				}else {
					btnNewButton.setEnabled(false);
				}
			}
		});
		textField.setBounds(81, 59, 181, 21);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel label = new JLabel("\u6635\u79F0");
		label.setBounds(36, 62, 35, 15);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801");
		label_1.setBounds(36, 114, 35, 15);
		frame.getContentPane().add(label_1);
		frame.setDefaultCloseOperation(frame.HIDE_ON_CLOSE);
		JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801");
		label_2.setBounds(23, 169, 54, 15);
		frame.getContentPane().add(label_2);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(81, 143, 181, 15);
		frame.getContentPane().add(lblNewLabel);
		
		passwordField = new JPasswordField();
		passwordField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (!passwordField_1.getText().equals("")) {
					if (passwordField.getText().equals(passwordField_1.getText())&&!passwordField.getText().equals("")) {
						lblNewLabel.setText("");
						if (!textField.getText().equals("")&&!passwordField.getText().equals("")&&!passwordField_1.getText().equals("")) {
							btnNewButton.setEnabled(true);
							
						}
					}else{
					
						lblNewLabel.setText("两次密码不一致！");
						btnNewButton.setEnabled(false);
					}
				}else{
					lblNewLabel.setText("");
				}
				
				
			}
		});
		passwordField.setBounds(81, 111, 181, 21);
		frame.getContentPane().add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (passwordField.getText().equals(passwordField_1.getText())&&!passwordField.getText().equals("")) {
					lblNewLabel.setText("");
					if (!textField.getText().equals("")&&!passwordField.getText().equals("")&&!passwordField_1.getText().equals("")) {
						btnNewButton.setEnabled(true);
						
					}
				}else{
				
					lblNewLabel.setText("两次密码不一致！");
					btnNewButton.setEnabled(false);
				}
				
			}
		});
		passwordField_1.setBounds(81, 166, 181, 21);
		frame.getContentPane().add(passwordField_1);
		frame.setVisible(true);
		textField.setColumns(16);
		passwordField.setColumns(16);
		passwordField_1.setColumns(16);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				passwordField.setText("");
				passwordField_1.setText("");
			}
		});
	}
	public void dipose(){
		frame.dispose();
	}
}
