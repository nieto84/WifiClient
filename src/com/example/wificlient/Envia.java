package com.example.wificlient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import Class.Files;
import Class.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedInputStream;
	private OutputStream outputStream;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.envia);


		socket = SingletonSocket.getInstance();

		lista = (ListView) findViewById(R.id.listView1);

		//recojo todos los archivos y directorios de la path indicada
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

		//Creo los objetos tipo y les asigno la imagen correspondiente segun extensión.
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
		//Muestro la lista
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

		try{
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

		}catch(Exception e){

			Toast t1 = Toast.makeText(getApplicationContext(),"El directorio seleccionado está vacio",Toast.LENGTH_LONG);
			t1.show();

		}

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


					final File file =new File(f+"/"+fichero);

					FileInputStream is = new FileInputStream(f+"/"+fichero);
					// Se instancia y rellena un mensaje de envio de fichero
					Files files = new Files();
					files.setNombreFichero(((Message) mensaje2).getPath()+"/"+fichero);


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


	public static void reiniciarActivity(Activity actividad){
		Intent intent=new Intent();
		intent.setClass(actividad, actividad.getClass());
		//llamamos a la actividad
		actividad.startActivity(intent);
		//finalizamos la actividad actual
		actividad.finish();
	}

	public void home(View arg){

		reiniciarActivity(this);
	}

	public void back(View arg){


		cdAnt(f.getParent());


	}
	public void cdAnt(String x){

		try{	
			if(!f.toString().equals("/")){


				directoris = new Vector<String>();

				archivos = new Vector<String>();

				File path = new File(x);

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

		}catch(Exception e){


		}

	}

}
