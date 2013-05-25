package com.example.wificlient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import Class.Files;
import Class.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class Origen extends Activity {


	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedInputStream;
	private OutputStream outputStream;
	private static int SELECT_PICTURE = 2;
	private Socket socket;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.origen);

		socket = SingletonSocket.getInstance();

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

		//Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("video/*");
		
		int code = SELECT_PICTURE; 
		startActivityForResult(intent,code);
	}
	


	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		

		try {

			boolean enviadoUltimo=false;

			//cogemos la imagen seleccionada por el usuario
			Uri selectedImage = data.getData();
			String path = getRealPathFromURI(selectedImage);
			final File file =new File(path);

			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			Message mensaje = new Message();
			mensaje.setOrden("CputFile");
			mensaje.setPath(selectedImage.getLastPathSegment());
			oos.writeObject(mensaje);



			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


			Object mensaje2 = ois.readObject();


			if (mensaje2 instanceof Message){

				if (((Message) mensaje2).getOrden().equals("SgetFile")){

					FileInputStream is = new FileInputStream(path);
				

					byte[] mybytearray = new byte[(int) file.length()]; //create a byte array to file

					fileInputStream = new FileInputStream(file);
					bufferedInputStream = new BufferedInputStream(fileInputStream); 

					bufferedInputStream.read(mybytearray, 0, mybytearray.length); //read the file

					outputStream = socket.getOutputStream();

					outputStream.write(mybytearray, 0, mybytearray.length); //write file to the output stream byte by byte

					//1outputStream.flush();

					bufferedInputStream.close();

					// outputStream.close();

					Toast t1 = Toast.makeText(getApplicationContext(),String.valueOf(socket.isConnected()),Toast.LENGTH_LONG);
					t1.show();

				}
			}
		}
				catch (UnknownHostException e) {
					e.printStackTrace();

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				} catch(Exception e){
							
				}
			
		}


		public String getRealPathFromURI(Uri contentUri) {

			// can post image
			String [] proj={MediaStore.Images.Media.DATA};
			@SuppressWarnings("deprecation")
			Cursor cursor = managedQuery( contentUri,
					proj, // Which columns to return
					null,       // WHERE clause; which rows to return (all rows)
					null,       // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			return cursor.getString(column_index);
		}

	}
