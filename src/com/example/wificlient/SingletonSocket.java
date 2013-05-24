package com.example.wificlient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SingletonSocket {

	private static Socket socket=null;
	
	protected SingletonSocket(){
		
	}
	
	
	public static Socket getInstance(String ip, int port) throws UnknownHostException, IOException{

		
		if(socket==null)socket = new Socket(ip,port);	
		return socket;
	}
	
	public static Socket getInstance(){

		return socket;
	}
	
	public static void removeInstance(){
		if(socket!=null)
			try {
				socket.close();
				socket=null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
			

}
