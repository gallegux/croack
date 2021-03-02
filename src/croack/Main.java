package croack;

import java.util.List;

import croack.gui.Consola;
import croack.gui.Configuracion;



public class Main 
{
	
	
	/* github */
	
	public static void main(String...arg)
	{
		
		try {
			/*
			AddJar masJars = new AddJar();
			masJars.addJar("commons-logging-1.1.1.jar");
			masJars.addJar("httpclient-4.2.3.jar");
			masJars.addJar("httpclient-cache-4.2.3.jar");
			masJars.addJar("httpcore-4-2-3.jar");
			masJars = null;
			*/
			//new Update().checkUpdate();
			
			Configuracion configuracion = new Configuracion();
			configuracion.init();
			configuracion.setVisible(true);
			
			List<String> credenciales = configuracion.getCredenciales();
			//System.out.println(credenciales);
			
			configuracion.dispose();
			configuracion = null;
			
			int i = 0;
			String s1 = credenciales.get(i++);
			String s2 = credenciales.get(i++);
			String s3 = credenciales.get(i++);
			String s4 = credenciales.get(i++);
			
			//System.out.println( String.format("LOGIN %s %s %s %s", s1, s2, s3, s4) );
			
			Consola consola = new Consola(s1, s2, s3, s4);
			consola.init();
			consola.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
