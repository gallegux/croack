package croack.gui;



import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import croack.CTE;
import croack.ed.RegistroOCS;
import croack.ed.UsuarioAD;
import croack.util.Codigo;
import croack.util.GBC;



public class PanelInfo extends JPanel 
implements MouseListener
{
	
	
	private final static int SEPARACION = 7;
	private final static Color COLOR_DATO = new Color(0,0,128);
	
	private JLabel lHostname, lIp, lMarca, lModelo, lSistOp, lVersionSO, lCpu, lMem, lUsuario, lNombre, lCargo, lConsejeria, lCorreo, lTelefono, lUltConex;
	private String fechaHoy, fechaHace1anio;
	private String urlOcs;
	private Luz chkEnLinea;
	//private Font fontKey;
	private String ip, windowsUser;
	

	
	public PanelInfo()
	{
		super();
		build();
		
		calcularFechas();
	}
	
	
	
	private void calcularFechas() 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		fechaHoy = sdf.format(new Date());
		long msAnio = 366l * 24l * 60l * 60l * 1000l;
		Date d = new Date(new Date().getTime() - msAnio);
		fechaHace1anio = sdf.format(d);
	}
	
	
	private void build()
	{
		JLabel _lHostname, _lIp, _lMarca, _lModelo, _lSistOp, _lVersionSO, _lCpu, _lMem,
			_lUsuario, _lNombre, _lCargo, _lConsejeria, _lTelefono, _lCorreo, _lUltConex, _lEnLinea;
		
		_lHostname = crearLabelEtiqueta("Equipo:");
		_lIp = crearLabelEtiqueta("IP/Máscara:");
		_lMarca = crearLabelEtiqueta("Marca:");
		_lModelo = crearLabelEtiqueta("Modelo:");
		_lSistOp = crearLabelEtiqueta("Sist. Operativo:");
		_lVersionSO = crearLabelEtiqueta("Versión:");
		_lCpu = crearLabelEtiqueta("CPU:");
		_lMem = crearLabelEtiqueta("RAM:");
		_lUsuario = crearLabelEtiqueta("Usuario:");
		_lNombre = crearLabelEtiqueta("Nombre:");
		_lCargo = crearLabelEtiqueta("Cargo:");
		_lConsejeria = crearLabelEtiqueta("Consejería:");
		_lTelefono = crearLabelEtiqueta("Teléfono:");
		_lCorreo = crearLabelEtiqueta("Correo:");
		_lUltConex = crearLabelEtiqueta("Fecha datos:");
		_lEnLinea = crearLabelEtiqueta("Ping");

		lHostname = crearLabelDato();
		lIp = crearLabelDato();
		lMarca = crearLabelDato();
		lModelo = crearLabelDato();
		lSistOp = crearLabelDato();
		lVersionSO = crearLabelDato();
		lCpu = crearLabelDato();
		lMem = crearLabelDato();
		lUsuario = crearLabelDato();
		lNombre = crearLabelDato();
		lCargo = crearLabelDato();
		lConsejeria = crearLabelDato();
		lTelefono = crearLabelDato();
		lCorreo = crearLabelDato();
		lUltConex = crearLabelDato();
		chkEnLinea = new Luz();
		
		JPanel p1 = crearPanelColumna(_lHostname, lHostname, _lMarca, lMarca, _lSistOp, lSistOp, _lCpu, lCpu,
				_lUsuario, lUsuario,  _lCargo, lCargo, _lTelefono, lTelefono, _lUltConex, lUltConex);
		
		JPanel p2 = crearPanelColumna(_lIp, lIp, _lModelo, lModelo, _lVersionSO, lVersionSO, _lMem, lMem, 
				_lNombre, lNombre, _lConsejeria, lConsejeria, _lCorreo, lCorreo, _lEnLinea, chkEnLinea);
	
		setLayout(new GridLayout(1,2,SEPARACION*2,SEPARACION*2));
		add(p1);
		add(p2);
	}
	
	
	
	private JPanel crearPanelConEnlace(JLabel eti, JLabel lnk)
	{
		JPanel p = new JPanel(new GridBagLayout());
		
		p.add(eti,          new GBC("anchor=LINE_START ipadx=10 gridx=0"));
		p.add(lnk,          new GBC("anchor=LINE_START ipadx=10 gridx=1"));
		p.add(new JLabel(), new GBC("anchor=LINE_START fill=HORIZONTAL weightx=1 ipadx=10 gridx=2"));

		lnk.addMouseListener(this);
		lnk.setVisible(false);
		lnk.setForeground(new Color(151, 24, 127));
		lnk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		return p;
	}
	
	
	
	private JPanel crearPanelColumna(JComponent...etiquetas)
	{
		JPanel panel = new JPanel(new GridBagLayout());
		int fila = 0, eti = 0;
		
		try {
			while (true) {
				anadirEtiquetaDato(etiquetas[eti++], etiquetas[eti++], fila++, panel);
			}
		}
		catch (Exception e) {}
		
		return panel;
	}
	
	
	
	private void anadirEtiquetaDato(JComponent etiqueta, JComponent dato, int fila, JPanel panel)
	{
		panel.add(etiqueta, new GBC("anchor=LINE_START ipadx=10 ipady=4 gridx=0 gridy=" + fila));
		panel.add(dato, new GBC("anchor=LINE_START fill=HORIZONTAL weightx=1 ipadx=10 ipady=4 gridx=1 gridy=" + fila));
	}
	
	
	
	private JLabel crearLabelEtiqueta(String texto)
	{
		JLabel l = new JLabel(texto);
		l.setFont(CTE.INFO_LABEL_FONT);
		return l;
	}



	private JLabel crearLabelDato()
	{
		JLabel l = new JLabel("");
		l.setForeground(COLOR_DATO);
		l.setFont( CTE.INFO_VALUE_FONT );
		l.addMouseListener(this);
		
		return l;
	}
	
	
	
	public void limpiar()
	{
		this.ip = null;
		
		lHostname.setText(" ");
		lIp.setText(" ");
		lMarca.setText(" ");
		lModelo.setText(" ");
		lSistOp.setText(" ");
		lVersionSO.setText(" ");
		lCpu.setText(" ");
		lMem.setText(" ");
		lUsuario.setText(" ");
		lUltConex.setText(" ");
		lNombre.setText(" ");
		lCargo.setText(" ");
		lConsejeria.setText(" ");
		lCorreo.setText(" ");
		lTelefono.setText(" ");
		chkEnLinea.setMostrar(false);
	}
	
	
	
	
	public void setInfoEquipo(RegistroOCS registro)
	{
		this.ip = registro.ip;
		
		try {
			lHostname.setText(registro.computerName);
		} catch (Exception ex) {}
		
		try {
			lIp.setText("<html><b>" + registro.ip.trim() + "</b>&nbsp;&nbsp;&nbsp;&nbsp;" + registro.mascara + "</html>");
		} catch (Exception ex) {}

		try {
			lMarca.setText(registro.marca);
		} catch (Exception ex) {}
		
		try {
			lModelo.setText(registro.modelo);
		} catch (Exception ex) {}
		
		try {
			lSistOp.setText(registro.sistOp);
		} catch (Exception ex) {}
		
		try {
			lVersionSO.setText(registro.versionSO);
		} catch (Exception ex) {}
		
		try {
			lCpu.setText(registro.cpu);
		} catch (Exception ex) {}
		
		try {
			lMem.setText(registro.memRam);
		} catch (Exception ex) {}
		
		try {
			lUsuario.setText(registro.usuario);
		} catch (Exception ex) {}
		
		try {
			lUltConex.setText(registro.lastLogin);
		} catch (Exception ex) {}
		
		try {
			urlOcs = registro.urlOcs;
		} catch (Exception ex) {}
		
		setInfoUsuario(registro.datosUsuario);
	}
	
	
	
	public void setInfoUsuario(UsuarioAD usu)
	{
		if (usu != null) {
			this.lNombre.setText(usu.getNombre());
			this.lCargo.setText(usu.getCargoBueno());
			this.lConsejeria.setText(usu.getConsejeria());
			this.lCorreo.setText(usu.getCorreo());
			this.lTelefono.setText( (null != usu.getTelefono()) ? usu.getTelefono() : "" );
				
		}
		else {
			this.lNombre.setText(" ");
			this.lCargo.setText(" ");
			this.lConsejeria.setText(" ");
			this.lCorreo.setText(" ");
			this.lTelefono.setText(" ");
		}
	}
	
	
	
	public String getIP()
	{
		return this.ip;
	}
	
	
	
	public void setEstadoPing(Boolean estado)
	{
		if (estado == null) {
			chkEnLinea.setMostrar(false);
		}
		else {
			chkEnLinea.setEstado(estado);
		}
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		if (e.getClickCount() == 2) {
			JLabel label = (JLabel) source;
			String texto = label.getText();
			
			if (label == lIp) texto = this.ip;
			else if (label == lHostname) texto = texto.replaceAll(" ", "");
			else if (label == lUsuario) texto = lUsuario.getText().trim();
			else if (label == lCorreo) texto = lCorreo.getText().trim();
			else if (label == lTelefono) texto = lTelefono.getText().trim();
			
			if (!texto.equals("")) {			
				StringSelection ss = new StringSelection(texto);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
			}
		} 
	}
	
	
	
	
	public String replaces(String plantilla) 
	{
		String tlf = this.lTelefono.getText().replace(" ", "");
		
		if (tlf.length() == 9) {
			tlf = '0' + tlf;
		}
		else if (tlf.length() == 5) {
			//tlf = "09262" + tlf;
		}
		
		String[] marcas = { "{computerName}", "{ip}", "{nombreUsuario}", "{email}", "{tel}", "{ocs}", "{uid}", "{winusr}",
				"{fecha_hoy}", "{fecha_-1anio}" };
		String[] valores= { lHostname.getText().replace(" ", ""), this.ip, this.lNombre.getText(), 
				this.lCorreo.getText(), tlf, this.urlOcs, this.lUsuario.getText(), getWindowsUser(),
				fechaHoy, fechaHace1anio};
		
		for (int i = 0; i < marcas.length; i++) {
			try {
				plantilla = plantilla.replace(marcas[i], valores[i]);
			} catch (Exception e) {}
		}
		return plantilla.trim();
	}

	
	
	private String getWindowsUser() {
		if (windowsUser == null) {
			//System.out.println(System.getProperties());
			windowsUser = System.getProperty("user.name").toLowerCase();
		}
		return windowsUser;
	}

	

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	
}
