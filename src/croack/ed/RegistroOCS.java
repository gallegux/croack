package croack.ed;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import croack.util.Codigo;





public class RegistroOCS
{
	
	private Pattern patronHost = Pattern.compile("^(FJ|fj|EJ|ej|PJ|pj)[0-9]{12}[a-zA-Z0-9]$");
	
	public String lastLogin, computerName, usuario, sistOp, memRam, versionSO, cpu, marca, numSerie, modelo, arquitectura, mascara, urlOcs;
	public UsuarioAD datosUsuario = null;
	public String nombreUsuario = null;
	public String ip;
	private IP sortIP = null;
	
	
	protected RegistroOCS() {}
	
	// ojo! con el orden de los datos
	public RegistroOCS(List<String> datos)
	{
		Iterator<String> itDatos = datos.iterator();
		
		urlOcs = itDatos.next();
		lastLogin = itDatos.next();
		computerName = itDatos.next().toUpperCase();
		usuario = itDatos.next();
		sistOp = itDatos.next();
		memRam = itDatos.next();
		versionSO = itDatos.next();
		cpu = itDatos.next();
		marca = itDatos.next();
		numSerie = itDatos.next();
		modelo = itDatos.next();
		ip = itDatos.next();
		arquitectura = itDatos.next();
		mascara = itDatos.next();
		
		if (usuario.equals("&nbsp")) usuario = "";

		if (patronHost.matcher(computerName).find()) {
			computerName = String.format("%s %s%s %s %s %s %s",  
					computerName.substring(0,2),
					computerName.substring(2,4),
					computerName.substring(4,7),
					computerName.substring(7,9),
					computerName.substring(9,11),
					computerName.substring(11,14),
					computerName.substring(14) );
		}
		
		sortIP = new IP(ip);
	}
	
	
//	public RegistroOCS(UsuarioAD uad)
//	{
//		this.usuario = uad.getUid();
//		this.nombreUsuario = uad.getNombre();
//		this.ip = uad.getIP();
//	}
	
	
	
	public String toString()
	{
		return String.format("[%s] %s %s/%s [%s] [%s] %s [%s] %s [%s] [%s] [%s] %s", 
				lastLogin, computerName, ip, mascara, marca, modelo, numSerie, cpu, memRam, sistOp, versionSO, arquitectura, usuario);
	}
	
	
	public IP getIP()
	{
		return this.sortIP;
	}
	
	public void setIP(String ip) {
		this.ip = ip;
		this.sortIP = new IP(ip);
	}
	
	
	public void copiarDatosOCS(RegistroOCS otro) {
		this.setIP(otro.ip);
		this.arquitectura = otro.arquitectura;
		this.computerName = otro.computerName;
		this.cpu = otro.cpu;
		this.lastLogin = otro.lastLogin;
		this.marca = otro.marca;
		this.mascara = otro.mascara;
		this.memRam = otro.memRam;
		this.modelo = otro.modelo;
		this.numSerie = otro.numSerie;
		this.versionSO = otro.versionSO;
		this.sistOp = otro.sistOp;
		this.urlOcs = otro.urlOcs;
	}
	
	
	public void copiarDatosLdap(RegistroOCS otro)
	{
		this.nombreUsuario = otro.nombreUsuario;
	}
	
	
	
//	public String crearTexto(String patron)
//	{
//		Codigo.log("patron", patron);
//		
//		String nombreUsuario = (this.datosUsuario != null) ? this.datosUsuario.getNombre() : "";
//		String correoUsuario = (this.datosUsuario != null) ? this.datosUsuario.getCorreo() : "";
//		
//		String[] marcas = { "{computerName}", "{ip}", "{nombreUsuario}", "{email}" };
//		String[] valores  = { this.computerName, this.ip, nombreUsuario, correoUsuario };
//		
//		for (int i = 0; i < marcas.length; i++) {
//			try {
//				patron = patron.replace( marcas[i], valores[i]);
//			} catch (Exception e) {}
//		}
//		
//		return patron;
//	}
	
	

}
