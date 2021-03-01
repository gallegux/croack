package croack.gui;



import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import croack.CTE;
import croack.ed.RegistroOCS;
import croack.ed.UsuarioAD;
import croack.util.GBC;



public class PanelInfo2 extends JPanel 
implements MouseListener
{
	
	
	private final static String TEXT_GAP = "         ";
	private final static int SEPARACION = 7;
	private final static Color COLOR_DATO = new Color(0,0,128);
	
	private JLabel lHostname, lIp, lMarca, lModelo, lSistOp, lServicePack, lCpu, lMem, lUsuario, lNombre, lCargo, lConsejeria, lCorreo, lTelefono, lUltConex;
	private String urlOcs;
	private Luz chkEnLinea;
	//private Font fontKey;
	private String ip;
	private JLabel lOcsUri = new JLabel("OCS");
	private JLabel lLdapUri = new JLabel("LDAP");
	private JLabel lEnviarUri = new JLabel("Redactar");
	private JLabel lLlamarUri = new JLabel("Llamar*");
	

	
	public PanelInfo2()
	{
		super();
		build();
	}
	
	
	private void build()
	{
		JLabel _lHostname, _lIp, _lMarca, _lModelo, _lSistOp, _lServicePack, _lCpu, _lMem,
			_lUsuario, _lNombre, _lCargo, _lConsejeria, _lTelefono, _lCorreo, _lUltConex, _lEnLinea;
		
		_lHostname = crearLabelEtiqueta("Equipo:");
		_lIp = crearLabelEtiqueta("IP/Máscara:");
		_lMarca = crearLabelEtiqueta("Marca:");
		_lModelo = crearLabelEtiqueta("Modelo:");
		_lSistOp = crearLabelEtiqueta("Sist. Operativo:");
		_lServicePack = crearLabelEtiqueta("Service Pack:");
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
		lServicePack = crearLabelDato();
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
		
		
		JPanel k = crearPanelConEnlace(lHostname, lOcsUri);
		JPanel m = crearPanelConEnlace(lUsuario, lLdapUri);
		JPanel q = crearPanelConEnlace(lCorreo, lEnviarUri);
		JPanel r = crearPanelConEnlace(lTelefono, lLlamarUri);
				
		JPanel p1 = crearPanelColumna(_lHostname, k, _lMarca, lMarca, _lSistOp, lSistOp, _lCpu, lCpu,
				_lUsuario, m,  _lCargo, lCargo, _lTelefono, r, _lUltConex, lUltConex);
		
		JPanel p2 = crearPanelColumna(_lIp, lIp, _lModelo, lModelo, _lServicePack, lServicePack, _lMem, lMem, 
				_lNombre, lNombre, _lConsejeria, lConsejeria, _lCorreo, q, _lEnLinea, chkEnLinea);
	
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
		lServicePack.setText(" ");
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
		lOcsUri.setVisible(false);
		lLdapUri.setVisible(false);
		this.lEnviarUri.setVisible(false);
		this.lLlamarUri.setVisible(false);
	}
	
	
	
	public void setInfoEquipo(RegistroOCS registro)
	{
		this.ip = registro.ip.trim();
		
		lHostname.setText(registro.computerName + TEXT_GAP);
		lIp.setText("<html><b>" + registro.ip + "</b>&nbsp;&nbsp;&nbsp;&nbsp;" + registro.mascara + "</html>");
		lMarca.setText(registro.marca);
		lModelo.setText(registro.modelo);
		lSistOp.setText(registro.sistOp);
		lServicePack.setText(registro.versionSO);
		lCpu.setText(registro.cpu);
		lMem.setText(registro.memRam);
		lUsuario.setText(registro.usuario + TEXT_GAP);
		lUltConex.setText(registro.lastLogin);
		urlOcs = registro.urlOcs;
		
		setInfoUsuario(registro.datosUsuario);
		this.lOcsUri.setVisible(true);
	}
	
	
	
	public void setInfoUsuario(UsuarioAD usu)
	{
		if (usu != null) {
			this.lNombre.setText(usu.getNombre());
			this.lLdapUri.setVisible(true);
			this.lCargo.setText(usu.getCargoBueno());
			this.lConsejeria.setText(usu.getConsejeria());
			this.lCorreo.setText(usu.getCorreo() + TEXT_GAP);
			this.lTelefono.setText(usu.getTelefono() + TEXT_GAP);
			this.lOcsUri.setVisible(true);
			this.lLdapUri.setVisible(true);
			this.lEnviarUri.setVisible(true);
			this.lLlamarUri.setVisible(true);
		}
		else {
			this.lNombre.setText(" ");
			this.lCargo.setText(" ");
			this.lConsejeria.setText(" ");
			this.lCorreo.setText(" ");
			this.lTelefono.setText(" ");
			this.lOcsUri.setVisible(false);
			this.lLdapUri.setVisible(false);
			this.lEnviarUri.setVisible(false);
			this.lLlamarUri.setVisible(false);
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
		if (e.getClickCount() == 2 && source != lOcsUri && source != lLdapUri && source != lEnviarUri && source != lLlamarUri) {
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
		else if (e.getClickCount() == 1) {
			if (e.getSource() == lOcsUri) openOcs();
			else if (e.getSource() == lLdapUri) openLdap();
			else if (e.getSource() == lEnviarUri) openCorreo();
			else if (e.getSource() == lLlamarUri) openLlamada();
		}
	}
	
	
	
	private void openOcs() {
		try {
			Desktop d = getDesktop();
			String url = "http://ocs.jclm.es/ocsreports/" + urlOcs; 
			URI uri = new URI(url);
			d.browse(uri);
		}
		catch (Exception ex) {}
//		if (Desktop.isDesktopSupported()) {
//			Desktop desktop = Desktop.getDesktop();
//
//			if (desktop.isSupported(Desktop.Action.BROWSE)) {
//				String url = "http://ocs.jclm.es/ocsreports/" + urlOcs; 
//				try {
//					URI uri = new URI(url);
//					desktop.browse(uri);
//				} catch (URISyntaxException | IOException ex) {}
//			}
//		}
	}

	

	private void openLdap() {
		try {
			Desktop d = getDesktop();
			String url = "https://cru.jccm.es/usuarios/gestusu.php?uid=" + lUsuario.getText().trim();
			URI uri = new URI(url);
			d.browse(uri);
		}
		catch (Exception ex) {}
//		if (Desktop.isDesktopSupported()) {
//			Desktop desktop = Desktop.getDesktop();
//
//			if (desktop.isSupported(Desktop.Action.BROWSE)) {
//				String url = "https://cru.jccm.es/usuarios/gestusu.php?uid=" + lUsuario.getText().trim();
//				try {
//					URI uri = new URI(url);
//					desktop.browse(uri);
//				} catch (URISyntaxException | IOException ex) {}
//			}
//		}
	}

	

	private void openCorreo() {
		try {
			Desktop d = getDesktop();
			String url = "mailto:" + lCorreo.getText().trim(); 
			URI uri = new URI(url);
			d.browse(uri);
		}
		catch (Exception ex) {}
//		if (Desktop.isDesktopSupported()) {
//			Desktop desktop = Desktop.getDesktop();
//
//			if (desktop.isSupported(Desktop.Action.BROWSE)) {
//				String url = "mailto:" + lCorreo.getText().trim(); 
//				try {
//					URI uri = new URI(url);
//					desktop.browse(uri);
//				} catch (URISyntaxException | IOException ex) {}
//			}
//		}
	}

	

	private void openLlamada() {
		try {
			Desktop d = getDesktop();
			String tel = lTelefono.getText().trim().replace(".", "");
			String url = "tel:" + tel;
			URI uri = new URI(url);
			d.browse(uri);
		}
		catch (Exception ex) {}
//		if (Desktop.isDesktopSupported()) {
//			Desktop desktop = Desktop.getDesktop();
//
//			if (desktop.isSupported(Desktop.Action.BROWSE)) {
//				String tel = lTelefono.getText().trim().replace(".", "");
//				String url = "tel:" + tel;
//				try {
//					URI uri = new URI(url);
//					desktop.browse(uri);
//				} catch (URISyntaxException | IOException ex) {}
//			}
//		}
	}
	
	
	private Desktop getDesktop() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) return desktop;
		}
		return null;
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
