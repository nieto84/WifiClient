package com.example.wificlient;


import java.io.File;
import java.io.FileOutputStream;
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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Recibe extends Activity{

	private Object mensaje;
	private EditText textOut;
	private TextView textIn;
	private Socket socket;
	private Tipo tipo;
	private ListView lista;
	private Adapter adapter;
	private Vector<String> archivos,directoris;
	private ObjectInputStream ois;
	private ArrayList<Tipo> arraytipo = new ArrayList<Tipo>();



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

		lista = (ListView) findViewById(R.id.listView1);


		try {

			ois = new ObjectInputStream(socket.getInputStream());
			mensaje = ois.readObject();

			if (mensaje instanceof Message){


				archivos = ((Message) mensaje).getDocs();

				directoris = ((Message) mensaje).getDire();

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
			}
		}catch(Exception e){


		}

		adapter = new Adapter(this, arraytipo);   
		// Lo aplico
		lista.setAdapter(adapter);


		lista.setOnItemClickListener (new  OnItemClickListener() {


			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) {


		

				if (arg2<directoris.size()){

					cd(arg2, (Message)mensaje);

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
							getFile((Message)mensaje,archivos.get(arg2-directoris.size())); // metodo que se debe implementar

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


	public  void cd(int arg2,Message mensajeAnt){


		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			Message mensaje = new Message();
			mensaje.setOrden("CgetList");

			if (mensajeAnt.getSo().startsWith("win")){
				mensaje.setPath(mensajeAnt.getPath()+"\\"+directoris.get(arg2));

			}else{

				mensaje.setPath(mensajeAnt.getPath()+"/"+directoris.get(arg2));
			}
			//mensaje.setPath(mensajeAnt.getPath()+"/"+directoris.get(arg2));

			oos.writeObject(mensaje);

		}
		catch(Exception e) {
			System.out.print("Whoops! It didn't work!\n");
		}

		try {

			ois = new ObjectInputStream(socket.getInputStream());
			mensaje = ois.readObject();

			if (mensaje instanceof Message){

				archivos = ((Message) mensaje).getDocs();

				directoris = ((Message) mensaje).getDire();

				recargaVista(archivos,directoris);

			}

		}catch(Exception e){


		}
	}



	public void cdAnt(Message mensajeAnt){


		String pathAnt = "";
		int x=0;
		String[] path;


		if (mensajeAnt.getSo().startsWith("win")){
			path = mensajeAnt.getPath().split("\\");

		}else{

			path = mensajeAnt.getPath().split("/");
		}



		while (x<path.length-1){

			pathAnt = pathAnt+path[x]+"/";

			x++;
		}



		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			Message mensaje = new Message();
			mensaje.setOrden("CgetList");

			mensaje.setPath(pathAnt);

			oos.writeObject(mensaje);

		}
		catch(Exception e) {
			System.out.print("Whoops! It didn't work!\n");
		}

		try {

			ois = new ObjectInputStream(socket.getInputStream());
			mensaje = ois.readObject();

			if (mensaje instanceof Message){

				archivos = ((Message) mensaje).getDocs();

				directoris = ((Message) mensaje).getDire();

				recargaVista(archivos,directoris);

			}

		}catch(Exception e){


		}

	}




	public void getFile(final Message mensajeAnt, final String fichero){



		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Por favor espere");
		progressDialog.setMessage("Recibiendo el archivo...");
		progressDialog.show();


		new Thread() {
			public void run() {
				try{


					try
					{
						// Se envía un mensaje de petición de fichero.
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						Message mensaje = new Message();
						mensaje.setOrden("CgetFile");
						mensaje.setPath(fichero);


						if (mensajeAnt.getSo().startsWith("win")){
							mensaje.setPath(mensajeAnt.getPath()+"\\"+fichero);

						}else{

							mensaje.setPath(mensajeAnt.getPath()+"/"+fichero);
						}


						File ruta_sd = Environment.getExternalStorageDirectory();

						File f = new File(ruta_sd.getAbsolutePath(),fichero);

						oos.writeObject(mensaje);

						// Se abre un fichero para empezar a copiar lo que se reciba.
						FileOutputStream fos = new FileOutputStream(f);

						// Se crea un ObjectInputStream del socket para leer los mensajes
						// que contienen el fichero.
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

						Files mensajeRecibido;
						Object mensajeAux;


						do
						{

							// Se lee el mensaje en una variabla auxiliar
							mensajeAux = ois.readObject();

							// Si es del tipo esperado, se trata
							if (mensajeAux instanceof Files){

								mensajeRecibido = (Files) mensajeAux;

								// Se escribe en el fichero
								fos.write(mensajeRecibido.getContenidoFichero(), 0,mensajeRecibido.getBytesValidos());


							} else{
								// Si no es del tipo esperado, se marca error y se termina
								// el bucle
								System.err.println("Mensaje no esperado"
										+ mensajeAux.getClass().getName());
								//Toast t1 = Toast.makeText(getApplicationContext(),"Mensaje no espeado" ,Toast.LENGTH_LONG);
								//t1.show();

								break;
							}


						} while (!mensajeRecibido.isUltimoMensaje());


						// Se cierra  fichero
						fos.close();
						//ois.close();


					} catch (Exception e)
					{

						Toast t1 = Toast.makeText(getApplicationContext(),"Error al recibir el archivo",Toast.LENGTH_LONG);
						t1.show();
					}

					// Do some Fake-Work
					sleep(3000);


				} catch (Exception e) {  }
				// Dismiss the Dialog
				progressDialog.dismiss();
			}
		}.start();


		Toast t1 = Toast.makeText(getApplicationContext(),"Transferencia finalizada con éxito",Toast.LENGTH_LONG);
		t1.show();
			
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

		cdAnt((Message)mensaje);

	}


	public void recargaVista(final Vector<String> archivos, final Vector<String> directoris){

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

				Toast t1 = Toast.makeText(getApplicationContext(),"SI",Toast.LENGTH_LONG);
				t1.show();

				if (arg2<directoris.size()){				

					cd(arg2,(Message)mensaje);

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
							getFile((Message)mensaje,archivos.get(arg2-directoris.size())); // metodo que se debe implementar
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});

	}

}
