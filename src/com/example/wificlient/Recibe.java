package com.example.wificlient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Class.Message;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Recibe extends Activity{


	EditText textOut;
	TextView textIn;
	Socket socket;
	Tipo tipo;
	//final String[] datos =new String[]{"Paraguay","Bolivia","Peru","Ecuador","Brasil","Colombia","Venezuela"};

	String[] datos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recibe);

		/*textOut = (EditText)findViewById(R.id.textout);
		Button buttonSend = (Button)findViewById(R.id.send);
		textIn = (TextView)findViewById(R.id.textin);
		buttonSend.setOnClickListener(buttonSendOnClickListener);*/

		socket = SingletonSocket.getInstance();

		ObjectInputStream ois;


		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Message mensaje = new Message();
			mensaje.setOrden("Esta es la Orden");
			oos.writeObject(mensaje);

		}
		catch(Exception e) {
			System.out.print("Whoops! It didn't work!\n");
		}



		try {
			
			ListView lista = (ListView) findViewById(R.id.listView1);
		    ArrayList<Tipo> arraydir = new ArrayList<Tipo>();

			ois = new ObjectInputStream(socket.getInputStream());
			Object mensaje = ois.readObject();

			if (mensaje instanceof Message){

				//datos=((Message) mensaje).getDirectori();
				//datos = {"",""};
				File[] archivos = ((Message) mensaje).getDirectori();
				
				int i = 0;
				while(archivos.length>i){
					String nom =archivos[i].getName();
					
				if (archivos[i].isDirectory()){
				        tipo = new Tipo(getResources().getDrawable(R.drawable.dir), nom, "");
				        arraydir.add(tipo);
				}
					
					i++;
				}


			}

		}catch(Exception e){


		}

		
		
		
		ArrayAdapter<String> adaptador =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, datos);

		ListView lstOpciones =(ListView)findViewById(R.id.listView1);
		
		lstOpciones.setAdapter(adaptador);

		

		lstOpciones.setOnItemClickListener (new  OnItemClickListener() {


			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				

			}	});
	}



	public void exit(View arg){

		finish();

	}



	public  void cd(){


		ObjectInputStream ois;


		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Message mensaje = new Message();
			mensaje.setOrden("cd");
			oos.writeObject(mensaje);

		}
		catch(Exception e) {
			System.out.print("Whoops! It didn't work!\n");
		}



		try {

			ois = new ObjectInputStream(socket.getInputStream());
			Object mensaje = ois.readObject();

			if (mensaje instanceof Message){

				//datos=((Message) mensaje).getDirectori();


			}

		}catch(Exception e){


		}


		ArrayAdapter<String> adaptador =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, datos);

		ListView lstOpciones =(ListView)findViewById(R.id.listView1);

		lstOpciones.setAdapter(adaptador);


	}
	

}
