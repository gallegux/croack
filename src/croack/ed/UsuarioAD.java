package croack.ed;


import java.io.Serializable;
import java.util.HashMap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;



public class UsuarioAD implements Serializable
{
	
	private static HashMap<String, String> consejerias = new HashMap<>();
	
	
	static {
		consejerias.put("AD", "Agencia de Gestión de la Energía de CLM");
		consejerias.put("56", "Agencia del Agua de CLM");
		consejerias.put("21", "Agricultura, Agua y Desarrollo Rural");
		consejerias.put("27", "Bienestar Social");
		consejerias.put("23", "Desarrollo Sostenible");
		consejerias.put("19", "Economía, Empresas y Empleo");
		consejerias.put("18", "Educación, Cultura y Deportes");
		consejerias.put("17", "Fomento");
		consejerias.put("15", "Hacienda y Administraciones Públicas");
		consejerias.put("26", "Sanidad");
		consejerias.put("04", "Consejo Consultivo de CLM");
		consejerias.put("AB", "Empr.Pública D.Quijote 2005");
		consejerias.put("AA", "Entidades Locales");
		consejerias.put("70", "Instituto de la Mujer de CLM");
		consejerias.put("55", "IPEX");
		consejerias.put("51", "IRIAF");
		consejerias.put("11", "Presidencia de la Junta de Comunidades");
		consejerias.put("61", "SESCAM");
		consejerias.put("AE", "Sindicadura de Cuentas");
		consejerias.put("", "");
	}
	
	
	
	private String consejeria = null;
	private String direccionGeneral = null;
	private String uid = null;
	private String nombre = null;
	private String correo = null;
	private String cargo = null;
	private String cargo2 = "";
	private String telefono = null;
	private long fechaDatos = 0;
	private String ip = null;

	
    
    
    public UsuarioAD(Attributes attr) throws NamingException 
    {
    	//System.out.println(attr);
    	this.ip = getAtributo(attr, "us-direccionip");
    	this.uid = getAtributo(attr, "uid");
    	this.nombre = getAtributo(attr, "cn");
    	this.correo = getAtributo(attr, "mail");
    	this.cargo = getAtributo(attr, "title");
    	//this.cargo2 = getAtributo(attr, "title");
    	this.consejeria = getAtributo(attr, "us-consejeria");
    	this.consejeria = consejerias.get(this.consejeria);
    	this.direccionGeneral = getAtributo(attr, "us-direcciongeneral");
    	
    	String t1 = getAtributo(attr, "us-telephoneNumberPrefix");
    	String t2 = getAtributo(attr, "telephoneNumber");
    	
    	this.telefono = "";
    	
    	if (t1 != null && t2 != null)	this.telefono = getAtributo(attr, "us-telephoneNumberPrefix")+" "+getAtributo(attr, "telephoneNumber");
    	else if (t1 != null && t2 == null)	this.telefono = getAtributo(attr, "us-telephoneNumberPrefix");
    	else if (t1 == null && t2 != null)	this.telefono = getAtributo(attr, "telephoneNumber");
    }
 
    
    
    private String getAtributo(Attributes aa, String nombreAtributo)
    {
    	try {
    		return (String) aa.get(nombreAtributo).get();
    	}
    	catch (Exception e) {
    		return null;
    	}
    }


	public String getConsejeria() {
		return consejeria;
	}


	public String getDireccionGeneral() {
		return direccionGeneral;
	}


	public String getUid() {
		return uid;
	}


	public String getNombre() {
		return nombre;
	}


	public String getCorreo() {
		return correo;
	}


	public String getCargo() {
		return cargo;
	}


	public String getCargo2() {
		return cargo2;
	}
	
	
	
	public String getCargoBueno()
	{
		if (cargo == null & cargo2 == null) {
			return "";
		}
		else if (cargo != null && cargo2 != null) {
			if (cargo.equals(cargo2)) {
				return cargo;
			}
			else return String.format("%s / %s", cargo, cargo2);
		}
		else if (cargo != null && cargo2 == null) {
			return cargo;
		}
		else if (cargo == null && cargo2 != null) {
			return cargo2;
		}
		return "?";
	}
	


	public String getTelefono() {
		return telefono;
	}
	
	
	public String getIP() {
		return ip;
	}
	
	
	public void setFechaDatos(long fd) {
		fechaDatos = fd;
	}
	
	public long getFechaDatos() {
		return fechaDatos;
	}
	
	
	public RegistroOCS getRegistroOCS() {
		RegistroOCS r = new RegistroOCS();
		
		r.nombreUsuario = this.nombre;
		r.usuario = this.uid;
		
		return r;
	}

	
	public String toString()
	{
		return String.format("%s [%s] %s [%s] [%s] [%s] [%s] [%s] [%s]", 
				uid, nombre, correo, cargo, cargo2, telefono, consejeria, direccionGeneral, ip);
	}
    

}
