package com.example.wificlient;

import android.graphics.drawable.Drawable;

public class Tipo {


	protected Drawable foto;
	protected String nombre;

	public Tipo(Drawable foto, String nombre) {
		super();
		this.foto = foto;
		this.nombre = nombre;
	}

	public Drawable getFoto() {
		return foto;
	}

	public void setFoto(Drawable foto) {
		this.foto = foto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
