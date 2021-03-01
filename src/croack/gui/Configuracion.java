package croack.gui;



import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import croack.BuscarAD;
import croack.BuscarOCS;
import croack.CTE;
import croack.util.Codigo;
import croack.util.GBC;
import gallegux.gui.helpers.panel.PanelBuilder;



public class Configuracion extends JDialog 
implements ActionListener, WindowListener, FocusListener
{
				
	
	private JTextField tfUsrLdap = new JTextField(10);
	private JPasswordField tfPwdLdap = new JPasswordField(10);
	private JTextField tfUsrOcs = new JTextField(10);
	private JPasswordField tfPwdOcs = new JPasswordField(10);
	private JCheckBox chkGuardarDatos = new JCheckBox("Recordar");
	private JButton bContinuar = new JButton("Continuar");
	
	
	
	public Configuracion()
	{
		super();
		setModal(true);
		
		build();
	}
	
	
	
	private void build()
	{
		tfUsrLdap.addFocusListener(this);
		tfPwdLdap.addFocusListener(this);
		tfUsrOcs.addFocusListener(this);
		tfPwdOcs.addFocusListener(this);
		bContinuar.addActionListener(this);
		
		tfUsrLdap.setFont(tfUsrLdap.getFont().deriveFont(Font.BOLD));
		tfUsrOcs.setFont(tfUsrOcs.getFont().deriveFont(Font.BOLD));

		JLabel titulo = new JLabel("Configuración conexiones");
		Font f = titulo.getFont();
		f = f.deriveFont( (float) f.getSize() + 5 );
		titulo.setFont(f);

		try {
			Object[] ccCfg1 = {"etiUsr", new JLabel("Usuario"), "etiPwd", new JLabel("Contraseña"),
					"etiLdap", new JLabel("LDAP:"), "etiOcs", new JLabel("OCS:"),
					"txtUsrLdap", tfUsrLdap, "txtUsrOcs", tfUsrOcs,
					"txtPwdLdap", tfPwdLdap, "txtPwdOcs", tfPwdOcs};
			JPanel panCfg1 = PanelBuilder.buildPanelF("/croack/gui/paneles.properties", "cfg1", ccCfg1);
			
			Object[] ccCfg2 = {"titulo", titulo, "panCfg1", panCfg1, "chkGuardar", chkGuardarDatos, "btnContinuar", bContinuar};
			JPanel panCfg2 = PanelBuilder.buildPanelF("/croack/gui/paneles.properties", "cfg2", ccCfg2);
	
			setTitle(Version.getAppTitle());
			setResizable(false);
			setLayout(new BorderLayout());
			add(panCfg2, BorderLayout.CENTER);
			pack();
			add(new PanelImagen(getContentPane().getHeight()), BorderLayout.WEST);
			pack();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		setLocationRelativeTo(null);
		addWindowListener(this);
		
		bContinuar.requestFocus();
	}
	
	
	
	private void build1()
	{
		tfUsrLdap.addFocusListener(this);
		tfPwdLdap.addFocusListener(this);
		tfUsrOcs.addFocusListener(this);
		tfPwdOcs.addFocusListener(this);
		bContinuar.addActionListener(this);
		
		tfUsrLdap.setFont(tfUsrLdap.getFont().deriveFont(Font.BOLD));
		tfUsrOcs.setFont(tfUsrOcs.getFont().deriveFont(Font.BOLD));

		JLabel titulo = new JLabel("Configuración conexiones");
		Font f = titulo.getFont();
		f = f.deriveFont( (float) f.getSize() + 5 );
		titulo.setFont(f);
		
		JPanel pForm = new JPanel(new GridBagLayout());
		GBC gbc = new GBC();
		
		pForm.add(new JLabel("Usuario"), gbc.setGrid(1, 0));
		pForm.add(new JLabel("Contraseña"), gbc.setGrid(2, 0));
		
		pForm.add(new JLabel("LDAP:"), gbc.setGrid(0, 1));
		pForm.add(tfPwdLdap, gbc.setGrid(2, 1));
		
		pForm.add(new JLabel("OCS:"), gbc.setGrid(0, 2));
		pForm.add(tfUsrOcs, gbc.setGrid(1, 2));
		pForm.add(tfPwdOcs, gbc.setGrid(2, 2));
		
		pForm.add(tfUsrLdap, gbc.set("insets=13,13,13,13").setGrid(1, 1));

		JPanel pFormPpal = new JPanel(new GridBagLayout());
		gbc = new GBC();
		
		pFormPpal.add(titulo, gbc.setGrid(0, 0));
		pFormPpal.add(pForm, gbc.setGrid(0, 1).set("insets.top=17"));
		pFormPpal.add(chkGuardarDatos, gbc.setGrid(0, 2));
		pFormPpal.add(bContinuar, gbc.setGrid(0, 3));
		pFormPpal.setBorder(new EmptyBorder(15,25,15,25));
		
		setTitle(Version.getAppTitle());
		setResizable(false);
		setLayout(new BorderLayout());
		add(pFormPpal, BorderLayout.CENTER);
		pack();
		add(new PanelImagen(getContentPane().getHeight()), BorderLayout.WEST);
		pack();
		
		setLocationRelativeTo(null);
		addWindowListener(this);
		
		bContinuar.requestFocus();
	}
	
	
	
	public void init()
	{
		cargarConfiguracion();
	}

	
	
	public void clickContinuar()
	{
		this.bContinuar.doClick();
	}
	
	
	
	private void cargarConfiguracion()
	{
		try {
			FileInputStream fis = new FileInputStream( CTE.F_CONFIG_CONEXIONES );
			GZIPInputStream zis = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(zis);
			
			Boolean recordar = ois.readBoolean();
			
			if (recordar) {
				chkGuardarDatos.setSelected(true);
				tfUsrLdap.setText( ois.readObject().toString() );
				tfPwdLdap.setText( new String((char[]) ois.readObject()) );
				tfUsrOcs.setText( ois.readObject().toString() );
				tfPwdOcs.setText( new String((char[]) ois.readObject()) );
			}
			
			ois.close();
			zis.close();
			fis.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void grabarConfiguracion()
	{
		try {
			FileOutputStream fos = new FileOutputStream( CTE.F_CONFIG_CONEXIONES );
			GZIPOutputStream zos = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(zos);
			
			oos.writeBoolean(chkGuardarDatos.isSelected());
			
			if (chkGuardarDatos.isSelected()) {
				oos.writeObject(tfUsrLdap.getText());
				oos.writeObject(tfPwdLdap.getPassword());
				oos.writeObject(tfUsrOcs.getText());
				oos.writeObject(tfPwdOcs.getPassword());
			}
			
			oos.close();
			zos.close();
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public List<String> getCredenciales()
	{
		ArrayList<String> ss = new ArrayList<>();
		
		ss.add(tfUsrLdap.getText());
		ss.add(new String(tfPwdLdap.getPassword()));
		ss.add(tfUsrOcs.getText());
		ss.add(new String(tfPwdOcs.getPassword()));
	
		return ss;
	}
	
	

	// ActionListener
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		String msg = "";
		
		// comprobacion ocs
		try {
			//System.out.println( new String(tfPwdOcs.getPassword()) );
			BuscarOCS ocs = new BuscarOCS(tfUsrOcs.getText(), new String(tfPwdOcs.getPassword()));
			
			if (!ocs.test()) {
				msg = "El usuario y/o contraseña de OCS no son correctos.";
			}
			else { 
				Codigo.log("ocs ok"); 
			}
		}
		catch (IOException ex1) {
			msg = "No se ha podido conectar a OCS.";
		}
		
		// comprobacion ldap
		try {
			BuscarAD bad = new BuscarAD(tfUsrLdap.getText(), new String(tfPwdLdap.getPassword()));
			bad.test();
		}
		catch (CommunicationException ex2) {
			msg += "\nNo se ha podido conectar a LDAP";
			Codigo.log("x ldap");
		}
		catch (NamingException ex1) {
			msg += "\nEl usuario y/o contraseña de LDAP no son correctos.";
			Codigo.log("x ldap");
		}
		
		msg = msg.trim();
		Codigo.log(msg);

		if (msg.equals("")) {
			grabarConfiguracion();
			setVisible(false);
		}
		else {
			JOptionPane.showMessageDialog(this, msg, ":/", JOptionPane.ERROR_MESSAGE);
			Codigo.log("!!!");
		}
	}
	
	
	
	// WindowListener
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}

	// FocusListener
	@Override
	public void focusGained(FocusEvent fe) {
		JTextField tf = (JTextField) fe.getSource();
		tf.setSelectionStart(0);
		tf.setSelectionEnd( tf.getText().length() );
	}
	@Override
	public void focusLost(FocusEvent arg0) {}
	


	private class PanelImagen extends JPanel
	{

		public PanelImagen(int alto)
		{
			super();
			
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream( Codigo.loadFromClasspath("/croack/recursos/imgs.txt") );
				InputStreamReader isr = new InputStreamReader(bais);
				BufferedReader br = new BufferedReader(isr);
				List<String> lineas = new ArrayList<>();
				
				try {
					while (true) {
						String l = br.readLine();
						if (!"".equals(l.trim())) lineas.add(l);
					}
				}
				catch (IOException|NullPointerException ex) {}
				
				int n = new Random().nextInt(lineas.size());
				String fn = lineas.get(n);
				
				byte[] data = Codigo.loadFromClasspath("/croack/recursos/" + fn);
				
				ImageIcon ii = new ImageIcon(data);
				JLabel l = new JLabel(ii);
				setLayout(new BorderLayout());
				add(l, BorderLayout.CENTER);
			}
			catch (Exception e) {
				Codigo.log("no image");
			}
		}

		
//		private byte[] getImagenAlt()
//		{
//			String user = getUsuario();
//			String fecha = getFecha();
//			
//			String[] imgs = { fecha + user, fecha };
//			
//			for (int i = 0; i < imgs.length; i++) {
//				String img = String.format(CTE.LOC_SHARED, imgs[i]);
//				Codigo.log(img); 
//				byte[] data = Codigo.loadFromClasspath(img);
//				if (data != null) return Codigo.loadFromClasspath(img);
//			}
//			
//			return null;
//		}
		
		
		private String getFecha()
		{
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			return sdf.format(fecha);
		}
		
		private String getUsuario()
		{
			String usu = System.getenv("USERNAME").toLowerCase();
			
			int p = usu.indexOf(',');
			if (p > 0) {
				usu = usu.substring(0, p);
			}
			
			return usu;
		}
		
	}
	

	
}
