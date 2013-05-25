package com.example.wificlient;

import java.io.IOException;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Client extends Activity{

	private Socket socket;
	private ImageView enviar, recibir;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conecta);

		enviar = (ImageView)findViewById(R.id.envia);
		recibir = (ImageView)findViewById(R.id.recibe);

		socket = SingletonSocket.getInstance();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e){

			}

		}
		return super.onKeyDown(keyCode, event);
	}


	public void recibe(View arg){

		Intent r = new Intent(this,Recibe.class);
		startActivity(r);


	}

	public void envia(View arg){

		Intent o = new Intent(this,Origen.class);
		startActivity(o);
	}
}
