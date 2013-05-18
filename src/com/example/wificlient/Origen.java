package com.example.wificlient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Origen extends Activity {
	
	
	private static int SELECT_PICTURE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.origen);
		
	}
	
	
	public void sd(View arg){
		
			Intent e = new Intent(this,Envia.class);
			startActivity(e);
		
	}
	
	public void images(View arg){
		
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		int code = SELECT_PICTURE;
		startActivityForResult(intent,code);
	}
	
	
	public void videos(View arg){
		
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
		int code = SELECT_PICTURE;
		startActivityForResult(intent,code);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
	}
	
}
