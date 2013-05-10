package com.example.wificlient;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class Inicio extends Activity {
	
	Button conecta,config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		
		conecta=(Button)findViewById(R.id.conecta);
		config=(Button)findViewById(R.id.config);
	}

	
	public void connect(View arg){
		
		if (!estadoWiFi(this)){
			
			
			
		}
		
	}
	
	
	
	
	
	public void configuration(View arg){
		
		
		
	}
	
	
	public static boolean estadoWiFi(Context contexto) {
		return ((WifiManager) contexto.getSystemService(Context.WIFI_SERVICE))
		.isWifiEnabled();
		}
	

}
