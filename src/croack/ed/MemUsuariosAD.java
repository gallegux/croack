package croack.ed;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import croack.CTE;
import croack.util.Codigo;



public class MemUsuariosAD
{
	
	private HashMap<String, UsuarioAD> mem = null;
	private static MemUsuariosAD _instancia = null;
	
	
	private MemUsuariosAD() 
	{
		cargar();
		Codigo.log("mem", mem.size());
	}
	
	
	public static MemUsuariosAD getInstance()
	{
		if (_instancia == null) _instancia = new MemUsuariosAD();
		return _instancia;
	}
	
	
	public String getUsuarioAzar()
	{
		Set<String> setClaves = mem.keySet();
		int tam = setClaves.size();
		if (tam != 0) {
			Object[] claves = setClaves.toArray();
			return (String) claves[new Random().nextInt(tam)];
		}
		else {
			return "eegc90";
		}
	}
	
	
	
	public void add(UsuarioAD u)
	{
		u.setFechaDatos(System.currentTimeMillis());
		mem.put(u.getUid(), u);
	}
	
	
	public void delete(String uid)
	{
		Codigo.log("del", uid);
		mem.remove(uid);
	}
	
	public void clear()
	{
		Codigo.log("mem clear");
		mem.clear();
	}
	
	
	public UsuarioAD get(String uid)
	{
		try {
			return mem.get(uid);
		}
		catch (Exception ex) {
			return null;
		}
	}
	
	
	private void cargar() 
	{
		try {
			InputStream is = new FileInputStream(CTE.F_BD_USUARIOS);
			ObjectInputStream ois = new ObjectInputStream(is);
			mem = (HashMap<String, UsuarioAD>) ois.readObject();
		}
		catch (Exception e) {
			Codigo.log("cargar", e);
			mem = new HashMap<String, UsuarioAD>(); 
		}
	}
	
	
	
	public void grabar()
	{
		try {
			Codigo.log("grabar", mem.size());
			OutputStream is = new FileOutputStream(CTE.F_BD_USUARIOS);
			ObjectOutputStream oos = new ObjectOutputStream(is);
			oos.writeObject(mem);
		}
		catch (Exception e) {
			Codigo.log("grabar", e);
		}
	}
	
	
	
	public void cerrar()
	{
		Codigo.log("cerrar mem usu");
		eliminar();
		grabar();
	}

	
	
	
	private void eliminar()
	{
		String clave = null;
		UsuarioAD usu = null;
		long caducidad = System.currentTimeMillis() - CTE.DIAS_CADUCIDAD_DATOS * 24 * 60 * 60 * 1000;
		
		Iterator<String> itKeys = mem.keySet().iterator();
		
		while (itKeys.hasNext()) {
			clave = itKeys.next();
			usu = mem.get(clave);
			
			if (usu.getFechaDatos() < caducidad) {
				Codigo.log("-", clave);
				itKeys.remove();
				mem.remove(clave);
			}
		}
		
	}
	
	
}
