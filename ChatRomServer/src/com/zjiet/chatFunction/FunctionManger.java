package com.zjiet.chatFunction;
import java.util.Vector;

public class FunctionManger {
	private FunctionManger(){}
	private static final FunctionManger cm = new FunctionManger();
	public static FunctionManger getFunctionManger(){
		return cm;
	}
	Vector<FuntionSocket> vector = new Vector<FuntionSocket>();
	//���socket
	public void add(FuntionSocket cs){
		vector.add(cs);

		
	}

//	//�Ƴ�����socket
	public void del(FuntionSocket cs){
		
		for(int i=0;i<vector.size();i++){
			if (vector.get(i).equals(cs)) {
				 vector.remove(i);
			}
			 
		}
	}
	//���¼�û����������û��б�
//	
	}
