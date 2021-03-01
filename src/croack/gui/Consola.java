package croack.gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import croack.BuscarAD;
import croack.BuscarOCS;
//import croack.BuscarUsuarioAD;
import croack.CTE;
import croack.ed.MemUsuariosAD;
import croack.ed.RegistroOCS;
import croack.ed.UsuarioAD;
import croack.util.Codigo;
import gallegux.gui.helpers.panel.PanelBuilder;




@SuppressWarnings("serial")
public class Consola extends JFrame 
implements ActionListener, MouseListener, FocusListener, KeyListener, WindowListener
{
	
	private final static String HOSTNAME = "Hostname";
	private final static String IP = "IP";
	private final static String UID_USUARIO = "UID usuario";
	private final static String NOMBRE_USUARIO = "Nombre usuario";
	private final static String CORREO_USUARIO = "Correo usuario";
	
	private final static int KEY_INTRO = 10;
	private final static int KEY_ARRIBA = 38;
	private final static int KEY_ABAJO = 40;
	private final static int KEY_ESPACIO = 32;
	
	private final static String[] FILTROS = {IP, HOSTNAME, UID_USUARIO, NOMBRE_USUARIO, CORREO_USUARIO};
	private final static String[] FILTROS_OCS = {UID_USUARIO, IP, HOSTNAME};
	private final static String[] FILTROS_LDA = {NOMBRE_USUARIO, CORREO_USUARIO};
	
	// patrones para reconocer
	private final static Pattern patronIP = Pattern.compile("^[0-9]*\\.[0-9]+\\.[[0-9]\\.]*$");
	private final static Pattern patronUID = Pattern.compile("^[a-z]{2,4}[0-9]{0,3}$");
	private final static Pattern patronMail = Pattern.compile("@");
	private final static Pattern patronHost = Pattern.compile("^(FJ|fj|EJ|ej|PJ|pj|J|j)?[0-9]{4,}$");
	
	private final static int[][] TECLAS_COMBO = {
			{49,73}, {50,72}, {51,85}, {52,78}, {53,67}, {8}
	};

	
	private JComboBox<String> cFiltro;
	private JTextField tfFiltro;
	private JButton bPegar;
	private JButton bFiltrar;
	private Tabla tabla;
	private JScrollPane scrollPane;
	private PanelInfo panelInfo;
	
	//
	private BuscarOCS busquedasOCS;
	private BuscarAD busquedasAD;
	private HiloActualizarNombresUsuarios hiloActualizarNombresUsuarios;
	private HiloGetInfoEquipos hiloGetInfoEquipos;
	private RegistroOCS registroSeleccionado;
	private boolean buscando = false;
	private boolean isAdmin = false;

	
	
	
	public Consola(String usrLdap, String pwdLdap, String usrOcs, String pwdOcs)
	{
		busquedasOCS = new BuscarOCS(usrOcs, pwdOcs);
		busquedasAD = new BuscarAD(usrLdap, pwdLdap);
	}
	
	

	public void init()
	throws IOException, NamingException
	{
		build();
		Codigo.log("Consola.init()");

		Thread t1 = new Thread() {
			public void run() {
				try {
					Codigo.log("iniciar HATO ocs");
					busquedasOCS.conectar();
					//log("conectados a ocs");
				}
				catch (Exception e) {
					Codigo.log(e);
				}
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				try {
					Codigo.log("iniciar HATO lda");
					busquedasAD.conectar();
					//log("conectados a ad");
				}
				catch (Exception e) {
					Codigo.log(e);
				}
			}
		};
		
		t1.start();
		t2.start();
		
		Codigo.log(busquedasOCS);
		Codigo.log(busquedasAD);
	}
	
	
	private void setIcon() {
		try {
			InputStream in = Consola.class.getResourceAsStream("/croack/recursos/app_icon.png");
			BufferedImage bi = ImageIO.read(in);
			ImageIcon image = new ImageIcon(bi); 
			setIconImage(image.getImage());
		}
		catch (Exception ex) {
			Codigo.log(ex);
		}
	}
	

	private void build() throws IOException
	{
		setIcon();
		
		cFiltro = new JComboBox<>(FILTROS);
		cFiltro.setRenderer(new ComboRenderer());
		cFiltro.addActionListener(this);
		cFiltro.addKeyListener(this);
		
		tfFiltro = new JTextField();
		tfFiltro.addFocusListener(this);
		tfFiltro.addKeyListener(this);
		
		bPegar = new JButton("\uD83D\uDCCB");
		bPegar.setToolTipText("Pegar");
		bPegar.setMargin(new Insets(2, 5, 2, 5));
		bPegar.addActionListener(this);
		
		bFiltrar = new JButton("  Buscar  ");
		bFiltrar.addActionListener(this);
		
		
		tabla = new Tabla();
		tabla.addMouseListener(this);
		tabla.addKeyListener(this);
		scrollPane = new JScrollPane(tabla);
		//
		this.panelInfo = new PanelInfo();

		Properties propsBotones = getBotones();
		propsBotones.list(System.out);
		
//		String strBotones = propsBotones.getProperty("botones");
//		String[] arrStrBotones = strBotones.split(",");
		
		//JPanel panelOpciones = new JPanel(new GridLayout(1, arrStrBotones.length, CTE.SEPARACION_ELEMENTOS, CTE.SEPARACION_ELEMENTOS));
		//lstBotones = new ArrayList<>(arrStrBotones.length);
//		for (String idBoton: arrStrBotones) {
//			Boton boton = new Boton(idBoton, propsBotones);
//			boton.addActionListener(this);
//			lstBotones.add(boton);
//			panelOpciones.add(boton);
//		}
		Boton bAsistenciaRemota = new Boton("bAsistenciaRemota", propsBotones, this);
		Boton bEscritorioRemoto = new Boton("bEscritorioRemoto", propsBotones, this);
		Boton bAdministrarEquipo = new Boton("bAdministrarEquipo", propsBotones, this);
		Boton bPSEXEC = new Boton("bPSEXEC", propsBotones, this);
		Boton bDiscoC = new Boton("bDiscoC", propsBotones, this);
		Boton bLlamar = new Boton("bLlamar", propsBotones, this);
		Boton bEscribir = new Boton("bEscribir", propsBotones, this);
		Boton bOCS = new Boton("bOCS", propsBotones, this);
		Boton bLDAP = new Boton("bLDAP", propsBotones, this);
		Boton bCRUs = new Boton("bCRUs", propsBotones, this);
		Boton bListas = new Boton("bListas", propsBotones, this);
		Boton bApagar = new Boton("bApagar", propsBotones, this);
		Boton bReiniciar = new Boton("bReiniciar", propsBotones, this);
		Boton bCancelar = new Boton("bCancelar", propsBotones, this);
		
		JPanel panelOpciones = PanelBuilder.buildPanelF("/croack/gui/paneles.properties", "botonera", 
				"bAsistenciaRemota", bAsistenciaRemota,
				"bEscritorioRemoto", bEscritorioRemoto,
				"bAdministrarEquipo", bAdministrarEquipo,
				"bPSEXEC", bPSEXEC,
				"bDiscoC", bDiscoC,
				"bLlamar", bLlamar,
				"bEscribir", bEscribir,
				"bOCS", bOCS,
				"bLDAP", bLDAP,
				"bCRUs", bCRUs,
				"bListas", bListas,
				"bApagar", bApagar,
				"bReiniciar", bReiniciar,
				"bCancelar", bCancelar);

		JPanel panelFiltro = PanelBuilder.buildPanelF("/croack/gui/paneles.properties", "panelFiltro",
				"lblFiltro", new JLabel("Filtro:"), "cmbFiltro", cFiltro, "tfFiltro", tfFiltro, "btnPegar", bPegar, "btnFiltrar", bFiltrar );
		
		JPanel panelEquipo = PanelBuilder.buildPanelF("/croack/gui/paneles.properties", "panelEquipo", 
				"panelInfo", this.panelInfo, "panelOpciones", panelOpciones);
		
		JPanel panelConsola = PanelBuilder.buildPanelF("/croack/gui/paneles.properties", "console", 
				"panelFiltro", panelFiltro, "scrollTabla", scrollPane, "panelEquipo", panelEquipo);
		
		setTitle(Version.getAppTitle());
		setContentPane(panelConsola);
		addWindowListener(this);
		cargarConfiguracion();
		
		//
		setMinimumSize( new Dimension(720, 500) );
		
		//
		
		String windowsUser = System.getProperty("user.name").toLowerCase();
		this.isAdmin = windowsUser.toLowerCase().endsWith(".admin");
	}

	
	
	private Properties getBotones() 
	{
		Properties propsBotones = new Properties();
		
		try {
			InputStream is = this.getClass().getResourceAsStream("/croack/botonera.cfg");
			propsBotones.load(is);
		}
		catch (Exception e) {}

		return propsBotones;
	}
	
	
	///////////////////// interfaz grafica /////////////////////////////////////////////////////////////
	
//	private Border crearBorde() {
//		return new EmptyBorder(CTE.SEPARACION_ELEMENTOS, CTE.SEPARACION_ELEMENTOS, CTE.SEPARACION_ELEMENTOS, CTE.SEPARACION_ELEMENTOS);
//	}
	
	
	////////////////////////// configuracion ///////////////////////////////////////////////////////////
	
	private void cargarConfiguracion()
	{
		try {
			FileInputStream fis = new FileInputStream( CTE.F_CONFIG_CONSOLA );
			ObjectInputStream ois = new ObjectInputStream(fis);
			Rectangle bounds = (Rectangle) ois.readObject();
			//log(bounds);
			setBounds(bounds);
			fis.close();
			ois.close();
		}
		catch (Exception e) {
			setBounds(new Rectangle(200, 200, 550, 450));
		}
	}
	
	
	
	
	private void grabarConfiguracion()
	{
		try {
			Rectangle bounds = getBounds();
			//log(bounds);
			FileOutputStream fos = new FileOutputStream( CTE.F_CONFIG_CONSOLA );
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(bounds);
			oos.close();
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void setBuscando(boolean b)
	{
		this.buscando = b;
		setCursor( (b) ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR );
		this.bFiltrar.setEnabled(!b);
	}

	
	private void filtrar()
	{
		try {
			hiloActualizarNombresUsuarios.parar();
			hiloActualizarNombresUsuarios = null;
		} catch (Exception e) {}
		
		try {
			hiloGetInfoEquipos.parar();
			hiloGetInfoEquipos = null;
		} catch (Exception e) {}
		
		this.panelInfo.limpiar();
		this.tabla.clearSelection();
		this.tabla.clear();
		this.tabla.repaint();
		
		String filtro = cFiltro.getSelectedItem().toString();
		String valorFiltro = tfFiltro.getText().trim();
		
		setBuscando(true);
		
		try {
			if (Codigo.estaEn(filtro, FILTROS_OCS)) {
				if (filtro.equals(HOSTNAME)) {
					List<RegistroOCS> registros = busquedasOCS.buscarPorEquipo(valorFiltro);
					rellenarTabla(registros);
				}
				else if (filtro.equals(IP)) {
					List<RegistroOCS> registros = busquedasOCS.buscarPorIP(valorFiltro);
					rellenarTabla(registros);
				}
				else if (filtro.equals(UID_USUARIO)) {
					List<RegistroOCS> registros = busquedasOCS.buscarPorUsuario(valorFiltro);
					rellenarTabla(registros);
				}
				if (this.tabla.getRegistros().size() > 0) {
					hiloActualizarNombresUsuarios = new HiloActualizarNombresUsuarios();
					hiloActualizarNombresUsuarios.start();
				}
				else {
					setBuscando(false);
					sinResultados();
				}
			}
			else {  // filtros ldap
				List<RegistroOCS> registros;
				if (filtro.equals(NOMBRE_USUARIO)) {
					registros = busquedasAD.buscarPorNombreUsuario(valorFiltro);
					rellenarTabla(registros);
				}
				else {
					registros = busquedasAD.buscarPorCorreoUsuario(valorFiltro);
					rellenarTabla(registros);
				}
				if (registros.size() > 0) {
					hiloGetInfoEquipos = new HiloGetInfoEquipos(registros);
					hiloGetInfoEquipos.start();
				}
				else {
					setBuscando(false);
					sinResultados();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		setCursor(Cursor.DEFAULT_CURSOR);
	}
	
	
	
	private void rellenarTabla(List<RegistroOCS> registros) {
		this.tabla.modelo.registros = registros;
		this.tabla.revalidate();
	}
	
	
	
	private void sinResultados() {
		this.tabla.revalidate();
		JOptionPane.showMessageDialog(this, "Búsqueda sin resultados", ":/", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	
	private void actualizarInformacion(RegistroOCS registro) {
		new HiloPing(registro.ip).start();
		
		this.panelInfo.setInfoEquipo(registro);
		
		if (registro.datosUsuario == null) new HiloActualizarInfoUsu(registro, registro.usuario).start();
				
		this.registroSeleccionado = registro;
	}
	
	
	
	//////////////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		
		if (o.getClass().equals(Boton.class)) {
			if (panelInfo.getIP() == null) {
				JOptionPane.showMessageDialog(this, "Seleccione un equipo", ":/", JOptionPane.ERROR_MESSAGE);
			}
			else {
				Boton b = (Boton) e.getSource();
				clickBoton(b);
			}
		}
		
		if (o == bFiltrar) {
			String valorFiltro = this.tfFiltro.getText().trim();
			if (valorFiltro.equals("")) {
				JOptionPane.showMessageDialog(this, "Escriba algún filtro", ":/", JOptionPane.ERROR_MESSAGE);
			}
			
//			if (valorFiltro.toLowerCase().equals("/coronavirus")) {
//				new LanzaCV().start();
//			}
			
			if (valorFiltro.startsWith("/")) {
				modificarMemUsuarios(valorFiltro);
			}
			else {
				filtrar();
			}				
		}
		else if (o == bPegar) {
			pegar();
		}
		else if (o == cFiltro) {
			System.out.println(e.getActionCommand());
			this.tfFiltro.requestFocus();
		}
	}
	
	
	class LanzaCV extends Thread 
	{		
		public void run() {
			try {
				Class c = Class.forName("coronavirus.Main");
				Object o = c.newInstance();
				Method m = c.getDeclaredMethod("run");
				m.invoke(o);			
			}
			catch (Exception ex) {
				Codigo.log("excepcion capturada:");
				ex.printStackTrace();
			}
		}	
	}
	
	
	
	private void modificarMemUsuarios(String comando)
	{
		MemUsuariosAD mem = MemUsuariosAD.getInstance();
		try {
			if (comando.equals("/clear")) {
				mem.clear();
			}
			else if (comando.startsWith("/del")) {
				String[] uids = comando.split(" ");
				
				for (int i = 1; i < uids.length; i++) mem.delete(uids[i]); 
			}
		}
		catch (Exception e) {
			Codigo.log(e);
		}
		this.tfFiltro.setText("");
	}
	
	
	
	private void pegar()
	{
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try {
            // Comprobamos que la información sea de tipo cadena, lo recuperamos y lo devolvemos.
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) t.getTransferData(DataFlavor.stringFlavor);

                tfFiltro.setText(text);
                
    			if (patronIP.matcher(text).find()) {
    				cFiltro.setSelectedIndex(0);
    			}
    			else if (patronMail.matcher(text).find()) {
    				cFiltro.setSelectedIndex(4);
    			}
    			else if (patronUID.matcher(text).find()) {
    				cFiltro.setSelectedIndex(2);
    			}
    			else if (patronHost.matcher(text).find()) {
    				cFiltro.setSelectedIndex(1);
    			}
    			else {
    				cFiltro.setSelectedIndex(3);
    			}

            }
        }
        catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	
	
	
	private void clickBoton(Boton boton)
	{
		
		{
			String chk = this.panelInfo.replaces( boton.getCheck() );
			if (chk.equals(""))		return;
		}
		
		
		if (boton.getPregunta() != null) {
			try {
				String pregunta = boton.getPregunta();
				pregunta = panelInfo.replaces(pregunta);
				
				int accion = JOptionPane.showConfirmDialog(this, pregunta, "¿?", JOptionPane.YES_NO_OPTION);
				//log(accion);
				if (accion == 0)	return;
			}
			catch (Exception e) {}
		}
		
		if (boton.getUri() != null) {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					Codigo.log("URI", boton.getUri());
					String sUri = panelInfo.replaces(boton.getUri());
					Codigo.log("URI", sUri);
					try {
						URI uri = new URI(sUri);
						desktop.browse(uri);
					} 
					catch (URISyntaxException | IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		else {	// no hay uri
			String comando = (isAdmin) ? boton.getCmdAdm() : boton.getCmd() ;
			Codigo.log("comando=", comando);

			comando = panelInfo.replaces(comando);
			Codigo.log("comando=", comando);
			
			if (boton.getBat()) {
				try {
					FileOutputStream fos = new FileOutputStream(CTE.F_BAT_AUX);
					fos.write( comando.getBytes() );
					fos.close();
					Runtime.getRuntime().exec(CTE.F_BAT_AUX);
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			else {
				try {
					String[] args = comando.split(" ");
					Codigo.log(comando.length(), args.length, args);
					for (String aa: args) Codigo.log(aa);
					Runtime.getRuntime().exec(args);
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		} // no hay uri
		
	}

	
	////////////////////////////////////////////////////////
	
	public void	windowActivated(WindowEvent e) {}
	public void	windowDeactivated(WindowEvent e) {}
	public void	windowDeiconified(WindowEvent e) {}
	public void	windowIconified(WindowEvent e) {}
	public void	windowOpened(WindowEvent e) {}
	public void	windowClosed(WindowEvent e) {}

	public void	windowClosing(WindowEvent e) {
		setVisible(false);
		busquedasOCS.desconectar();
		grabarConfiguracion();
		MemUsuariosAD.getInstance().cerrar();
		//busquedasAD.desconectar();
		//log("CLOSE");
		System.exit(0);
	}


	////////////////////////////////////////////////////////
	
	private void actualizarInfoEquipo()
	{
		int fila = tabla.getSelectedRow();
		RegistroOCS reg = this.tabla.getRegistro(fila);
		actualizarInformacion(reg);
	}
	
	public void	mouseClicked(MouseEvent e) {
		System.out.println("mouse clicked");
		if (e.getSource() == this.tabla)	actualizarInfoEquipo();
		if (e.getSource() == this.tfFiltro)	this.tfFiltro.requestFocus();
	}
	
	public void	mouseEntered(MouseEvent e) {}
	public void	mouseExited(MouseEvent e) {}
	public void	mousePressed(MouseEvent e) {}
	public void	mouseReleased(MouseEvent e) {}
	
	////////////////////////////////////////////////////////
	
	public void focusGained(FocusEvent fe) {
		this.tfFiltro.setSelectionStart(0);
		this.tfFiltro.setSelectionEnd( this.tfFiltro.getText().length() );
	}
	
	
	public void focusLost(FocusEvent fe) {}

	////////////////////////////////////////////////////////

	
	public void	keyPressed(KeyEvent e) {
		if (e.isAltDown()) {
			int tecla = e.getKeyCode();
			if (tecla == 8 /*DEL*/) {
				this.tfFiltro.setText("");
				return;
			}
			for (int opcionCombo = 0; opcionCombo < TECLAS_COMBO.length; opcionCombo++) {
				for (int nTecla = 0; nTecla < TECLAS_COMBO[opcionCombo].length; nTecla++) {
					if (TECLAS_COMBO[opcionCombo][nTecla] == tecla) {
						this.cFiltro.setSelectedIndex(opcionCombo);
						return;
					}
				}
			}
		}
	}
	
	
	public void	keyReleased(KeyEvent e) {
		int tecla = e.getExtendedKeyCode();
		Object og = e.getSource();
		
		if (og == this.tfFiltro && tecla == KEY_INTRO) this.bFiltrar.doClick();
		else if (og == this.tabla && (tecla == KEY_ARRIBA || tecla == KEY_ABAJO)) actualizarInfoEquipo();
		else if (og == this.cFiltro) {
			if (tecla == KEY_ESPACIO || tecla == KEY_INTRO) this.tfFiltro.requestFocus();
		}
	}
	
	public void	keyTyped(KeyEvent e) {}
	
	
	////////////////////////////////////////////////////////
	
	
	class HiloActualizarInfoUsu extends Thread
	{
		private RegistroOCS registro;
		private String uid;
		
		public HiloActualizarInfoUsu(RegistroOCS _reg, String _uid) 
		{
			super();
			
			this.registro = _reg;
			this.uid = _uid;
		}
		
		public void run() 
		{
			UsuarioAD usuAD = null;
			
			try {
				usuAD = busquedasAD.getUser(uid);
			}
			catch (NamingException | NullPointerException ne) {
				Codigo.log("Consola.HiloActualizarInfoUsu.run() -->", ne);
			}
			
			if (usuAD != null) {
				registro.datosUsuario = usuAD;
				panelInfo.setInfoUsuario(usuAD);
			}
		}
	}
	
	
	
	class HiloActualizarNombresUsuarios extends Thread
	{
		private boolean seguir = true;
		
		public HiloActualizarNombresUsuarios() 		{
			super();
		}
		
		public void parar() {
			this.seguir = false;
		}
		
		public void run()
		{
			int numFilas = tabla.getRowCount();
			int fila = 0;
			String uid = null, nombre = null;
			RegistroOCS reg;
			
			busquedasAD.comprobarConexion();
			
			while (fila < numFilas && this.seguir) {
				try {
					reg = tabla.getRegistro(fila);
					uid = reg.usuario;
					nombre = busquedasAD.getUser(uid).getNombre();
					reg.nombreUsuario = nombre;
					//tabla.repaint();
				}
				catch (NullPointerException ex) {
					Codigo.log("HiloActualizarNombresUsuarios.run()  NullPointerException  " + uid);
				}
				catch (NamingException ne) {
					Codigo.log("HiloActualizarNombresUsuarios.run()  NamingException  " + uid);
				}
				fila++;
			}

			setBuscando(false);
			tabla.repaint();
			tabla.revalidate();
			//tabla.scrollRectToVisible(new Rectangle(tabla.getCellRect(0, 0, true)));
		}
	}


	
	private class HiloGetInfoEquipos extends Thread
	{
		private boolean seguir = true;
		private List<RegistroOCS> registros;
		
		
		public HiloGetInfoEquipos(List<RegistroOCS> rr) {
			super();
			this.registros = rr;
		}
		
		public void parar() {
			this.seguir = false;
		}
		
		public void run()
		{
			try {
				HiloActualizarTabla hat = new HiloActualizarTabla();
				hat.start();
				setCursor(Cursor.WAIT_CURSOR);
				
				int numFila = 0;
				
				while (numFila < tabla.getRowCount()) {
					RegistroOCS registro = tabla.getRegistro(numFila);
					List<RegistroOCS> registrosUsuario = busquedasOCS.buscarPorUsuario(registro.usuario);
					
					if (registrosUsuario.size() == 0) {
						this.registros.remove(numFila);
					}
					else if (registrosUsuario.size() == 1) {
						registro.copiarDatosOCS(registrosUsuario.get(0));
						numFila++;
					}
					else {
						String regUsuUid = registrosUsuario.get(0).usuario;
						if (regUsuUid.equals(registro.usuario)) {
							registro.copiarDatosOCS(registrosUsuario.get(0));
						}
						numFila++;
						
						for (int i = 1; i < registrosUsuario.size(); ++i) {
							RegistroOCS ri = registrosUsuario.get(i);
							regUsuUid = ri.usuario;
							if (regUsuUid.equals(registro.usuario)) { 
								ri.nombreUsuario = registro.nombreUsuario;
							}
							registros.add(numFila, ri);
							numFila++;
						}
					}
				}
				
				hat.parar();
				setBuscando(false);
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	class HiloActualizarTabla extends Thread
	{
		private boolean seguir = true;
		
		public HiloActualizarTabla() {}
		
		public void parar() {
			this.seguir = false;
		}
		
		public void run() 
		{
			while (this.seguir) {
				try {
					sleep(1000);
				} 
				catch (Exception ex) {}
				
				tabla.repaint();
				tabla.revalidate();
			}
			//tabla.scrollRectToVisible(new Rectangle(tabla.getCellRect(0, 0, true)));
		}
	}

	
	class HiloPing extends Thread
	{
		String ip;
		
		public HiloPing(String ip) {
			this.ip = ip;
			panelInfo.setEstadoPing(null);
		}
		
		public void run() {
			try {
				if (this.ip != null) {
					char caracter;
					Boolean estado = null;
					String linea;
					StringBuilder sb = new StringBuilder();
					String[] parametros = new String[] {"ping", "-n", "5", "-w", "500", this.ip};
					Process proceso = Runtime.getRuntime().exec(parametros);
					InputStream is = proceso.getInputStream();
					
					while (estado == null) {
						caracter = (char) is.read();
						sb.append(caracter);
						
						if (caracter == '\n') {
							linea = sb.toString();
							sb = new StringBuilder();
							
							if (linea.indexOf("Respuesta desde") != -1) {
								estado = true;
							}
							else if (linea.indexOf("sticas de ping") != -1) {
								estado = false;
							}
						}
					}
					if (ip.equals(panelInfo.getIP())) {
						panelInfo.setEstadoPing(estado);
					}
					proceso.destroy();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
	class ComboRenderer implements ListCellRenderer 
	{
		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			renderer.setBorder(new EmptyBorder(2, 7, 2, 7));
			//renderer.setBackground(Color.WHITE);
			//if (!isSelected) renderer.setBackground(Color.WHITE);
			renderer.setForeground( getColorOpcion(value.toString()) );

			return renderer;
		}
	}
	
	
	
	private Color getColorOpcion(String opcion)
	{
		if (Codigo.estaEn(opcion, FILTROS_LDA)) return CTE.FILTRO_LDAP_COLOR;
		else return CTE.FILTRO_OCS_COLOR;
	}
	

	
// fin fichero	
}
