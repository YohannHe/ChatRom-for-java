package com.zjiet.function;

import javax.swing.JOptionPane;

import com.zjiet.empty.UserInfo;


public class LoginUIFuntion {
	private String reg="Null";
	
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public void CheckPass(UserInfo userInfo){
		FunctionManger.getCM().send("CheckPass<>"+userInfo.getUserID()+"<>"+userInfo.getUserPass());	
	}
	public void AddNewUser(UserInfo userInfo){

		FunctionManger.getCM().send("AddUser<>"+userInfo.getUserName()+"<>"+userInfo.getUserPass());
	
	}
	
	
	
}
