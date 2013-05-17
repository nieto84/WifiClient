package com.example.wificlient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.prefs.Preferences;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Inicio extends Activity {

	Socket socket;

	Button conecta,config;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);

		conecta=(Button)findViewById(R.id.conecta);
		config=(Button)findViewById(R.id.config);
	}


	public void connect(View arg) throws UnknownHostException, IOException{
		
		if (estadoWiFi(this)){

			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			// set dialog message
			alertDialogBuilder.setTitle("Error en la conexión");
			alertDialogBuilder.setMessage("Para usar la aplicación debe estar activa la conexión Wifi");
			alertDialogBuilder.setCancelable(false).
			setPositiveButton("OK", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog,int id)
			{} });//}
			//.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog,int id) {dialog.cancel(); }});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}else{


			try {	
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				String ip =  prefs.getString("ip", "");
				String ports = prefs.getString("port","");	
				int port = Integer.parseInt(ports);
				//DESCOMENTAR----------------------------------------------------------------------------
				SingletonSocket.getInstance(ip,port);
				Intent client = new Intent(this,Client.class);
				startActivity(client);

			}catch(Exception e){
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

				// set dialog message
				alertDialogBuilder.setTitle("Error en la conexión");
				alertDialogBuilder.setMessage(e.getMessage()+"Error indeterminado vuelva a conectar" );
				alertDialogBuilder.setCancelable(false).
				setPositiveButton("OK", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog,int id)
				{} });//}
				//.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog,int id) {dialog.cancel(); }});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				
				//socket.close();

			}

		}
	}

	public void configuration(View arg){

		Intent conf = new Intent(this,Config.class);
		startActivity(conf);
	}


	public static boolean estadoWiFi(Context contexto) {
		return ((WifiManager) contexto.getSystemService(Context.WIFI_SERVICE))
				.isWifiEnabled();
	}



}
