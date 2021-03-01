package croack.gui;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;




public class Boton extends JButton
{

	public final static String TITULO_PROP = ".titulo";
	public final static String CMD_PROP = ".cmd";
	public final static String URI_PROP = ".uri";
	public final static String CMD_ADM_PROP = ".cmdadm";
	public final static String PREGUNTA_PROP = ".pregunta";
	public final static String BAT_PROP = ".bat";
	public final static String CHECK = ".check";
	
	
	private String pregunta = null;
	private String uri = null;
	private String cmdline = null;
	private String cmdlineAdm = null;
	private boolean bat = false;
	private String check = null;
	
	
	
//	private Boton(String texto, String cmdline, String cmdlineAdm, String pregunta, String bat)
//	{
//		super();
//		
//		this.cmdline = cmdline;
//		this.cmdlineAdm = cmdlineAdm;
//		this.pregunta = pregunta;
//		this.bat = Boolean.parseBoolean(bat);
//		
//		//Codigo.log("boton:", texto);
//		setText("<html><center>" + texto + "</center></html>");
//		
//		setMargin(new Insets(1,1,1,1));
//	}
	
	
	
	public Boton(String nombre, Properties props, ActionListener actionListener)
	{
		super();

		setText("<html><center>" + props.getProperty(nombre + TITULO_PROP) + "</center></html>");

		this.uri = props.getProperty(nombre + URI_PROP, null);
		this.cmdline = props.getProperty(nombre + CMD_PROP);
		this.cmdlineAdm = props.getProperty(nombre + CMD_ADM_PROP);
		this.pregunta = props.getProperty(nombre + PREGUNTA_PROP);
		this.bat = Boolean.parseBoolean( props.getProperty(nombre + BAT_PROP, "false") );
		this.check = props.getProperty(nombre + CHECK);
		
		setMargin(new Insets(1,1,1,1));
		
		if (actionListener != null) addActionListener(actionListener);
	}
	

	
	public String getPregunta() {
		return this.pregunta;
	}
	
	
	
	public String getUri() {
		return this.uri;
	}
	
	
	public String getCmd() {
		return this.cmdline;
	}
	
	
	public String getCmdAdm() {
		return this.cmdlineAdm;
	}
	
	
	public boolean getBat() {
		return this.bat;
	}
	
		
	public String getCheck() {
		return this.check;
	}
	
		
	
	public String toString() {
		return String.format("[%s] [%s] [%s]", getText(), cmdline, pregunta);
	}
	

}
