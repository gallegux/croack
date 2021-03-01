package croack.noto;

import croack.util.Codigo;

public class HiloAntiTimeout extends Thread 
{
	public final static int TIEMPO_COMPROBACION = 5;
	public final static int TIEMPO_ANTI_TIMEOUT = 52;
	
	
	private IAntiTimeOut antito = null;
	private int tiempoComprobacion = TIEMPO_COMPROBACION;  // segundos
	private int tiempoAntiTimeOut = TIEMPO_ANTI_TIMEOUT; // segundos
	private boolean seguir = true;
	private long ultimoAcceso = 0;
	
	
	
	
	public HiloAntiTimeout(IAntiTimeOut c)
	{
		super();
		this.antito = c;
		updateUltimoAcceso();
	}
	
	
	
	public void setTiempoComprobacion(int t)
	{
		this.tiempoComprobacion = t;
	}
	
	
	
	public void setTiempoAntiTimeOut(int t)
	{
		this.tiempoAntiTimeOut = t;
	}
		
	
	
	private long getTime()
	{
		return System.currentTimeMillis() / 1000;
	}
	
	
	
	public void updateUltimoAcceso()
	{
		this.ultimoAcceso = getTime();
	}
	
	
	
	public void run()
	{
		Codigo.log("hiloAntiTO.start()");
		while (seguir) {
			esperar(this.tiempoComprobacion);
			if ( (getTime() - this.ultimoAcceso) > this.tiempoAntiTimeOut ) this.antito.antiTimeOutMethod();
		}
	}
	
	
	
	public void parar()
	{
		seguir = false;
	}
	
	
	
    private static void esperar(int segundos) 
	{
    	try {
    		Thread.currentThread().sleep(segundos * 1000);
    	}
    	catch (Exception e) {}
    }


}
