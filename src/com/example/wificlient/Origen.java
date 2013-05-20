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
			 File file =new File(path);
			 
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

				Message mensaje = new Message();
				mensaje.setOrden("CputFile");
				mensaje.setPath(selectedImage.getLastPathSegment());
				oos.writeObject(mensaje);

			//is =  (FileInputStream) getContentResolver().openInputStream(selectedImage);

/*
				Toast t2= Toast.makeText(getApplicationContext(),path+" ",Toast.LENGTH_LONG);
				t2.show();
			 
	

			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


			Object mensaje2 = ois.readObject();

		
			if (mensaje2 instanceof Message){

				if (((Message) mensaje2).getOrden().equals("SgetFile")){
				
				 FileInputStream is = new FileInputStream(path);
					// Se instancia y rellena un mensaje de envio de fichero
					Files files = new Files();
					files.setNombreFichero(((Message) mensaje2).getPath()+"/"+selectedImage.getLastPathSegment());
					
					
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
						
						
						try
						{
					
						File ruta_sd =
						Environment.getExternalStorageDirectory();
		
						File f = new File(ruta_sd.getAbsolutePath(),"prueba_sd.txt");
				
						OutputStreamWriter fout =new OutputStreamWriter(new FileOutputStream(f));
						
						fout.write(convertToHex(files.getContenidoFichero()));
					
						fout.close();
						}
						catch (Exception ex)
					{
						Log.e("Ficheros", "E rror al escribir fichero a ta rjeta SD");
						}
		
						convertToHex(files.getContenidoFichero());
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

				Toast t1 = Toast.makeText(getApplicationContext(),e.getMessage()+" a",Toast.LENGTH_LONG);
				t1.show();

			}*/
	/*
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				Message mensaje = new Message();
				mensaje.setOrden("CputFile");
				mensaje.setPath(selectedImage.getLastPathSegment());
				oos.writeObject(mensaje);
				
				// Se abre el fichero.
				FileInputStream fis = new FileInputStream(path);

				// Se instancia y rellena un mensaje de envio de fichero
				Files files = new Files();
				files.setNombreFichero(selectedImage.getLastPathSegment());

				// Se leen los primeros bytes del fichero en un campo del mensaje
				int leidos = fis.read(files.getContenidoFichero());

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
					files.setNombreFichero(selectedImage.getLastPathSegment());

					// y se leen sus bytes.
					leidos = fis.read(files.getContenidoFichero());
					
					//System.out.println(convertToHex(files.getContenidoFichero()));
					
					
				}

				if (enviadoUltimo==false)
				{
					files.setUltimoMensaje(true);
					files.setBytesValidos(0);
					oos.writeObject(files);

				}
		}catch(Exception e){

					Toast t1 = Toast.makeText(getApplicationContext(),e.getMessage()+"a",Toast.LENGTH_LONG);
					t1.show();

				}
		
		*/
			 
			 
			 byte[] mybytearray = new byte[(int) file.length()]; //create a byte array to file
			 
		     fileInputStream = new FileInputStream(file);
		     bufferedInputStream = new BufferedInputStream(fileInputStream); 
		 
		     bufferedInputStream.read(mybytearray, 0, mybytearray.length); //read the file
		 
		     outputStream = socket.getOutputStream();
		 
		     outputStream.write(mybytearray, 0, mybytearray.length); //write file to the output stream byte by byte
		    // outputStream.flush();
		     bufferedInputStream.close();
		     outputStream.close();
		     
		     
		    	      
		    } catch (UnknownHostException e) {
		     e.printStackTrace();
		    } catch (IOException e) {
		     e.printStackTrace();
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
	
	
	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
	
	
		

	}
