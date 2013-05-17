package com.example.wificlient;

import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Client extends Activity{

	Socket socket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conecta);
		
		
		socket = SingletonSocket.getInstance();
		
		
		Toast t3 = Toast.makeText(getApplicationContext(),"incoming "+String.valueOf(socket.getRemoteSocketAddress()) ,Toast.LENGTH_LONG);
		t3.show();
		

		
		
		}


	
	public void recibe(View arg){
	
	
		
		Intent r = new Intent(this,Recibe.class);
		startActivity(r);
		
		
	}
}
