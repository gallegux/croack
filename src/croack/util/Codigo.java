package croack.util;

import java.awt.Image;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.zip.CRC32;

import javax.swing.ImageIcon;



public class Codigo
{

	private static SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss ");
	
	
	public static boolean estaEn(String opcion, String[] opciones)
	{
		for (String o: opciones) {
			if (o.equals(opcion)) {
				return true;
			}
		}
		return false;
	}
	
	
	
 	public static void log(Object...ooo) 
 	{
 		Date now = new Date();
 	    System.out.print( sdfDate.format(now) );
 	    
		for (Object o: ooo) {
			System.out.print(o);
			System.out.print(' ');
		}
		System.out.println();
	}
 	
 	
 	
 	public static byte[] loadFromClasspath(String fichero, long crc)
 	{
 		try {
	 		InputStream is = new Object().getClass().getResourceAsStream(fichero);
	 		DataInputStream dis = new DataInputStream(is);
	 		int tam = dis.available();
	 		byte[] buffer = new byte[tam];
	 		int cont = 0;
	 		
	 		while (cont < tam) {
	 			cont += dis.read(buffer, cont, tam-cont);
	 		}
	 		dis.close();
	 		is.close();
	 		
	 		CRC32 crc32 = new CRC32();
			crc32.update(buffer);
			//log(fichero, crc32.getValue());
			
			return (crc32.getValue() == crc) ? buffer : null;
 		}
 		catch (IOException e) {
 			return null;
 		}
 	}
 	
 	
 	public static byte[] loadFromClasspath(String fichero)
 	{
 		try {
 			int BUFFER_SIZE = 2000000;
			InputStream is = new Codigo().getClass().getResourceAsStream(fichero);
	 		byte[] buffer = new byte[BUFFER_SIZE];
	 		int leidos = 0, tam = 0;
	 		
	 		while ( (leidos = is.read(buffer, tam, BUFFER_SIZE-tam)) != -1 ) {
	 			//Codigo.log("leidos", leidos);
	 			tam += leidos;
	 		}
	 		
	 		is.close();
	 		
	 		return Arrays.copyOf(buffer, tam);
 		}
 		catch (Exception e) {
 			return null;
 		}
 	}
 	


}
