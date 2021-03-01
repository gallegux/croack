package croack.gui;

import java.awt.Color;

import javax.swing.JLabel;


public class Luz extends JLabel
{
	public final static String TEXTO = "\u25CF";
	
	public final static Color ON = new Color(0,160,0);
	public final static Color OFF = new Color(224,0,0);
	
	private boolean estado = false;
	//private boolean mostrar = false;
	
	
	public Luz()
	{
		super();
	}
	
	
	
	public void setMostrar(boolean b)
	{
		setText( (b) ? TEXTO : "");
	}
	
	
	
	public void setEstado(boolean b)
	{
		setMostrar(true);
		estado = b;
		setForeground( (b) ? ON : OFF);
		repaint();
	}
	
	
	
	public boolean getEstado()
	{
		return estado;
	}

	
	

}
