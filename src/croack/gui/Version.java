package croack.gui;


import croack.CTE;


public class Version
{
	
	private final static String LETRAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toLowerCase(); // base32

	
	
	private static String getLetra(int n)
	{
		return LETRAS.substring(n,n+1);
	}
	
	
	/*
	private static String getVersion(int anio, int mes, int dia)
	{
		return getLetra(anio) + getLetra(mes) + getLetra(dia);
	}
	*/
	
	
	// fecha: "<anio> <mes> <dia>"
	// de 0 a 31
	public static String getVersion(String fecha)
	{
		String result = "", letra;
		int n;
		
		String[] partes = fecha.split("-");
		int[] numeros = new int[partes.length];
		
		for (int i = 0; i < partes.length; i++) {
			n = Integer.parseInt(partes[i]);
			letra = getLetra(n);
			result += letra;
		}
		
		return result;
	}
	
	
	
	public static String getAppTitle()
	{
		String title = CTE.APP_TITLE;
		
		title = title.replace("{version}", getVersion(CTE.VERSION));
		title = title.replace("{username}", System.getenv("USERNAME"));
		
		return title;
	}
	

}
