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
			

}
