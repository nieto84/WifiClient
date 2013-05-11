package com.example.wificlient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Client extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conecta);

	}

	
	
	public void recibe(View arg){
		
		Intent r = new Intent(this,Recibe.class);
		startActivity(r);
		
		
	}
}
