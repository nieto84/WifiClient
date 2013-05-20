package com.example.wificlient;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import Class.Files;
import Class.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Envia extends Activity{


	private Tipo tipo;
	private Vector<String> directoris = new Vector<String>();
	private Vector<String> archivos = new Vector<String>();
	private ArrayList<Tipo> arraytipo = new ArrayList<Tipo>();
	private ListView lista;
	private Adapter adapter;
	private File f = new File("/");
	private FileInputStream is;
	private Socket socket;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.envia);

		
		socket = SingletonSocket.getInstance();

		lista = (ListView) findViewById(R.id.listView1);
		

		File[] listfiles;

		listfiles = f.listFiles();
		int i = 0;


		while(i<listfiles.length)
		{

			if(listfiles[i].isDirectory()){

				directoris.add(listfiles[i].getName());


			}
			else{
				archivos.add(listfiles[i].getName());		

			}
			i++;
		}


		Iterator<String> a = directoris.iterator();




		while (a.hasNext()){

			String dire = a.next();

			tipo=new Tipo(getResources().getDrawable(R.drawable.dir),dire);
			arraytipo.add(tipo);

		}

		while(archivos.size()>i){


			if (archivos.get(i).toString().endsWith("jpg")|| archivos.get(i).endsWith("png")|| archivos.get(i).endsWith("jpeg")){

				tipo=new Tipo(getResources().getDrawable(R.drawable.image),archivos.get(i));
				arraytipo.add(tipo);

			}else if (archivos.get(i).endsWith("txt") || archivos.get(i).endsWith("pdf") || archivos.get(i).endsWith("docx") || archivos.get(i).toString().endsWith("doc") || archivos.get(i).endsWith("odt")){
				tipo=new Tipo(getResources().getDrawable(R.drawable.doc),archivos.get(i));
				arraytipo.add(tipo);

			}else if (archivos.get(i).endsWith("avi") || archivos.get(i).endsWith("mpeg") || archivos.get(i).endsWith("mp3") || archivos.get(i).endsWith("mp4")){			
				tipo=new Tipo(getResources().getDrawable(R.drawable.media),archivos.get(i));
				arraytipo.add(tipo);
			}else{

				tipo=new Tipo(getResources().getDrawable(R.drawable.unrecognized),archivos.get(i));
				arraytipo.add(tipo);
			}


			i++;
		}


		adapter = new Adapter(this, arraytipo);   
		// Lo aplico
		lista.setAdapter(adapter);

		lista.setOnItemClickListener (new  OnItemClickListener() {


			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) {


				if (arg2<directoris.size()){

					cd(directoris.get(arg2));

				}else{


					AlertDialog.Builder builder = new AlertDialog.Builder(Envia.this);
					builder.setMessage("¿Desea enviar el archivo seleccionado a su PC?")
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
							sendFile(archivos.get(arg2-directoris.size()));
							//getFile((Message)mensaje,archivos.get(arg2-directoris.size())); // metodo que se debe implementar

						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});


	}
	
	public void cd(String directori){
		
		
		directoris = new Vector<String>();
		
		archivos = new Vector<String>();
		
		File path = new File(f+"/"+directori);
		
		f=path;
		
		File[] listfiles;

		listfiles = path.listFiles();
		int i = 0;


		while(i<listfiles.length)
		{

			if(listfiles[i].isDirectory()){

				directoris.add(listfiles[i].getName());


			}
			else{
				archivos.add(listfiles[i].getName());		

			}
			i++;
		}
		
		recargaVista(archivos,directoris);
		
	}
		
	
	public void recargaVista(final Vector<String> archivos,  final Vector<String> directoris){

		arraytipo = new ArrayList<Tipo>();

		try{
			int i = 0;

			Iterator<String> a = directoris.iterator();

			while (a.hasNext()){

				String dire = a.next();

				tipo=new Tipo(getResources().getDrawable(R.drawable.dir),dire);
				arraytipo.add(tipo);

			}

			while(archivos.size()>i){


				if (archivos.get(i).toString().endsWith("jpg")|| archivos.get(i).endsWith("png")|| archivos.get(i).endsWith("jpeg")){

					tipo=new Tipo(getResources().getDrawable(R.drawable.image),archivos.get(i));
					arraytipo.add(tipo);

				}else if (archivos.get(i).endsWith("txt") || archivos.get(i).endsWith("pdf") || archivos.get(i).endsWith("docx") || archivos.get(i).toString().endsWith("doc") || archivos.get(i).endsWith("odt")){
					tipo=new Tipo(getResources().getDrawable(R.drawable.doc),archivos.get(i));
					arraytipo.add(tipo);

				}else if (archivos.get(i).endsWith("avi") || archivos.get(i).endsWith("mpeg") || archivos.get(i).endsWith("mp3") || archivos.get(i).endsWith("mp4")){			
					tipo=new Tipo(getResources().getDrawable(R.drawable.media),archivos.get(i));
					arraytipo.add(tipo);
				}else{

					tipo=new Tipo(getResources().getDrawable(R.drawable.unrecognized),archivos.get(i));
					arraytipo.add(tipo);
				}

				i++;
			}

		}catch(Exception e){


		}
		lista = (ListView) findViewById(R.id.listView1);
		adapter = new Adapter(this, arraytipo); 
		adapter.notifyDataSetChanged();
		// Lo aplico
		lista.setAdapter(adapter);


		lista.setOnItemClickListener (new  OnItemClickListener() {


			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) {

			
				if (arg2<directoris.size()){				

					cd(directoris.get(arg2));

				}else{

					AlertDialog.Builder builder = new AlertDialog.Builder(Envia.this);
					builder.setMessage("¿Desea enviar el archivo seleccionado a su PC?")
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
							Toast t1 = Toast.makeText(getApplicationContext(),archivos.get(arg2-directoris.size()),Toast.LENGTH_LONG);
							t1.show();

							sendFile(archivos.get(arg2-directoris.size()));
							//getFile((Message)mensaje,archivos.get(arg2-directoris.size())); // metodo que se debe implementar
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});

	}
	
	public void sendFile(String fichero){
		
		

		
		try {

			boolean enviadoUltimo=false;


			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			Message mensaje = new Message();	
			mensaje.setOrden("CputFile");	
			mensaje.setPath(fichero);
			oos.writeObject(mensaje);

			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


			Object mensaje2 = ois.readObject();

	
			if (mensaje2 instanceof Message){

		
				if (((Message) mensaje2).getOrden().equals("SgetFile")){
						
					
					is = new FileInputStream(f+"/"+fichero);
		
					// Se instancia y rellena un mensaje de envio de fichero
					Files files = new Files();
				
					files.setNombreFichero(((Message) mensaje2).getPath()+"/"+fichero);
			
					
					int leidos = is.read(files.getContenidoFichero());

					// Bucle mientras se vayan leyendo datos del fichero
					while (leidos > -1)
					{
						
						// Se rellena el número de bytes leidos
						files.setBytesValidos(leidos);

						// Si no se han leido el máximo de bytes, es porque el fichero
						// se ha acabado y este es el último mensaje
						if (leidos <Files.LONGITUD_MAXIMA)
						{
							// Se marca que este es el último mensaje
							files.setUltimoMensaje(true);
							enviadoUltimo=true;
						}
						else files.setUltimoMensaje(false);


						oos.writeObject(files);

						// Si es el último mensaje, salimos del bucle.
						if (files.isUltimoMensaje())
							break;

						// Se crea un nuevo mensaje
						files = new Files();
						files.setNombreFichero(mensaje.getPath());

						// y se leen sus bytes.
						leidos = is.read(files.getContenidoFichero());
					}

					if (enviadoUltimo==false)
					{
						files.setUltimoMensaje(true);
						files.setBytesValidos(0);
						oos.writeObject(files);

					}

				}
			}

				//oos.close();

			}catch(Exception e){


				Toast t2 = Toast.makeText(getApplicationContext(),e.getMessage()+" a",Toast.LENGTH_LONG);
				t2.show();

			}
		
	}

}
