package com.example.wificlient;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class Envia extends Activity{


	private Tipo tipo;
	private Vector<String> directoris = new Vector<String>();
	private Vector<String> archivos = new Vector<String>();
	private ArrayList<Tipo> arraytipo = new ArrayList<Tipo>();
	private ListView lista;
	private Adapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.envia);


		lista = (ListView) findViewById(R.id.listView1);
		
		File f = new File("/sdcard");

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



	}

}
