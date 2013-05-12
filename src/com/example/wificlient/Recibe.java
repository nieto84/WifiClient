package com.example.wificlient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Class.Files;
import Class.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Recibe extends Activity{


	EditText textOut;
	TextView textIn;
	Socket socket;
	Tipo tipo;
	File[] archivos;
	ObjectInputStream ois;
	//final String[] datos =new String[]{"Paraguay","Bolivia","Peru","Ecuador","Brasil","Colombia","Venezuela"};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recibe);


		socket = SingletonSocket.getInstance();


		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Message mensaje = new Message();
			mensaje.setOrden("CgetList");
			oos.writeObject(mensaje);

		}
		catch(Exception e) {
			System.out.print("Whoops! It didn't work!\n");
		}

		ListView lista = (ListView) findViewById(R.id.listView1);
		ArrayList<Tipo> arraytipo = new ArrayList<Tipo>();

		try {

			ois = new ObjectInputStream(socket.getInputStream());
			Object mensaje = ois.readObject();

			if (mensaje instanceof Message){


				archivos = ((Message) mensaje).getList();


				//Log.i("abbba",String.valueOf(archivos.length));

				int i = 0;
				
						
				Toast toast1 =Toast.makeText(getApplicationContext(),String.valueOf(archivos[4].isFile()),Toast.LENGTH_SHORT);
				toast1.show();
				
				
				
				while(archivos.length>i){
	
					
					Log.i("aa",String.valueOf(archivos[i].isDirectory())+"directori");
					if (archivos[i].isDirectory()){

						tipo=new Tipo(getResources().getDrawable(R.drawable.dir),archivos[i].getPath());
						arraytipo.add(tipo);
					}
		
				else
				{

					String nom =archivos[i].getPath();

					if (nom.endsWith("jpg")|| nom.endsWith("png")|| nom.endsWith("jpeg")){

						tipo=new Tipo(getResources().getDrawable(R.drawable.image),nom);
						arraytipo.add(tipo);
					}

					if (nom.endsWith("txt") || nom.endsWith("pdf") || nom.endsWith("docx") || nom.endsWith("doc") || nom.endsWith("odt")){
						tipo=new Tipo(getResources().getDrawable(R.drawable.doc),nom);
						arraytipo.add(tipo);

					}

					if (nom.endsWith("avi") || nom.endsWith("mpeg") || nom.endsWith("mp3") || nom.endsWith("mp4")){			
						tipo=new Tipo(getResources().getDrawable(R.drawable.media),nom);
						arraytipo.add(tipo);
					}
				}
				i++;
			}

		}

	}catch(Exception e){


	}

	Adapter adapter = new Adapter(this, arraytipo);   
	// Lo aplico
	lista.setAdapter(adapter);


	lista.setOnItemClickListener (new  OnItemClickListener() {


		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) {

			if (archivos[arg2].isDirectory()){

				cd(arg2);

			}else{


				AlertDialog.Builder builder = new AlertDialog.Builder(Recibe.this);
				builder.setMessage("¿Desea copiar el archivo seleccionado en su dispositivo?")
				.setTitle("Recepción de archivo")
				.setCancelable(false)
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})
				.setPositiveButton("Continuar",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						getFile(archivos[arg2].toString()); // metodo que se debe implementar
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	});
}


public void exit(View arg){

	finish();

}


public  void cd(int arg2){


	try {
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		Message mensaje = new Message();
		mensaje.setOrden("getList");
		mensaje.setPath(archivos[arg2].toString());
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
}


public void getFile(String fichero){
	try
	{
		// Se envía un mensaje de petición de fichero.
		ObjectOutputStream oos = new ObjectOutputStream(socket
				.getOutputStream());
		Message mensaje = new Message();
		mensaje.setOrden("CgetFile");
		mensaje.setPath(fichero);

		//Files files = new Files();
		//files.setNombreFichero(fichero);

		oos.writeObject(mensaje);

		// Se abre un fichero para empezar a copiar lo que se reciba.
		FileOutputStream fos = new FileOutputStream(mensaje.getPath()+ "_copia");

		// Se crea un ObjectInputStream del socket para leer los mensajes
		// que contienen el fichero.
		ObjectInputStream ois = new ObjectInputStream(socket
				.getInputStream());
		Files mensajeRecibido;
		Object mensajeAux;
		do
		{
			// Se lee el mensaje en una variabla auxiliar
			mensajeAux = ois.readObject();

			// Si es del tipo esperado, se trata
			if (mensajeAux instanceof Files)
			{
				mensajeRecibido = (Files) mensajeAux;
				// Se escribe en el ficher
				fos.write(mensajeRecibido.getContenidoFichero(), 0,
						mensajeRecibido.getBytesValidos());
			} else
			{
				// Si no es del tipo esperado, se marca error y se termina
				// el bucle
				System.err.println("Mensaje no esperado "
						+ mensajeAux.getClass().getName());
				break;
			}
		} while (!mensajeRecibido.isUltimoMensaje());

		// Se cierra socket y fichero
		fos.close();
		ois.close();
		socket.close();

	} catch (Exception e)
	{
		e.printStackTrace();
	}
}

}
