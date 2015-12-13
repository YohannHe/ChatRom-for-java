package com.zjiet.function;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetLocalIP {
	public static String getLocalIPForJava(){
//	    StringBuilder sb = new StringBuilder();
//	    try {
//	    	Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
//	        while (en.hasMoreElements()) {
//	            NetworkInterface intf = (NetworkInterface) en.nextElement();
//	            Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
//	            while (enumIpAddr.hasMoreElements()) {
//	                 InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
//	                 if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress() 
//	                		 	&& inetAddress.isSiteLocalAddress()) {
//	                	 sb.append(inetAddress.getHostAddress().toString());
//	                 }
//	             }
//	          }
//	    } catch (SocketException e) {  }
//	    return sb.toString();
		return ChatManger.getCM().getLocalIP();
	}
}
