package Class;

import java.io.File;
import java.io.Serializable;


public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orden;
	private final File[] lista=null;// =new String[]{"Paraguay","Bolivia","Peru","Ecuador","Brasil","Colombia","Venezuela"};
	

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}
	
	public File[] getDirectori(){
		
		
		return lista;
	}

}
