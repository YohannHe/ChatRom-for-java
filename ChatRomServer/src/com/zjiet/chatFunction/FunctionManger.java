package com.zjiet.chatFunction;
import java.util.Vector;

public class FunctionManger {
	private FunctionManger(){}
	private static final FunctionManger cm = new FunctionManger();
	public static FunctionManger getFunctionManger(){
		return cm;
	}
	Vector<FuntionSocket> vector = new Vector<FuntionSocket>();
	//添加socket
	public void add(FuntionSocket cs){
		vector.add(cs);

		
	}

//	//移除离线socket
	public void del(FuntionSocket cs){
		
		for(int i=0;i<vector.size();i++){
			if (vector.get(i).equals(cs)) {
				 vector.remove(i);
			}
			 
		}
	}
	//向登录用户发送在线用户列表
//	
	}
