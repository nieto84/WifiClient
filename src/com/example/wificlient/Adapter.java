package com.example.wificlient;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends BaseAdapter {

	
	protected Activity activity;
    protected ArrayList<Tipo> items;
 
    public Adapter(Activity activity, ArrayList<Tipo> items) {
        this.activity = activity;
        this.items = items;
      }

	@Override
	public int getCount() {
	
		  return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		// Generamos una convertView por motivos de eficiencia
        View v = convertView;
 
        //Asociamos el layout de la lista que hemos creado
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista2, null);
        }
 
        // Creamos un objeto directivo
        Tipo tipo = items.get(position);
        //Rellenamos la fotografía
        ImageView foto = (ImageView) v.findViewById(R.id.foto);
        foto.setImageDrawable(tipo.getFoto());
        //Rellenamos el nombre
        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        nombre.setText(tipo.getNombre());
    

        // Retornamos la vista
        return v;
    }
	
	
}
