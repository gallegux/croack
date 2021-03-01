package croack;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.HasControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

import croack.ed.MemUsuariosAD;
import croack.ed.RegistroOCS;
import croack.ed.UsuarioAD;
import croack.noto.HiloAntiTimeout;
import croack.noto.IAntiTimeOut;
import croack.util.Codigo;



public class BuscarAD 
implements IAntiTimeOut
{

	private final int PAGE_SIZE = 500;
	private MemUsuariosAD memUsuarios = MemUsuariosAD.getInstance();
	private String ADuser, ADpwd;
	private LdapContext context;
	private HiloAntiTimeout hiloAntiTimeOut;
	
	
	
	private BuscarAD() {}
	

	
	public BuscarAD(String usr, String pwd)
	{
		this.ADuser = usr;
		this.ADpwd = pwd;
	}
	

	
	private LdapContext getConnection0()
			throws CommunicationException, NamingException
	{
//        if (domainName == null) {
//            try {
//                String fqdn = InetAddress.getLocalHost().getCanonicalHostName();
//                Codigo.log("fqdn = " + fqdn);
//                if (fqdn.split("\\.").length>1) domainName = fqdn.substring(fqdn.indexOf(".")+1);
//                Codigo.log("domainName = " + domainName);
//	        }
//	        catch(UnknownHostException e) {}
//	    }
	     
        String principalName = String.format("uid=%s,o=Junta de Castilla-La Mancha,c=es", this.ADuser);
        String ldapURL = "ldap://ldap.jccm.es:636";

	    Hashtable props = new Hashtable();
	    props.put(Context.SECURITY_PROTOCOL, "ssl");
        props.put(Context.SECURITY_PRINCIPAL, principalName);
        props.put(Context.SECURITY_CREDENTIALS, this.ADpwd);
	    props.put(Context.PROVIDER_URL, ldapURL);
	    props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	    
	    try {
	        return new InitialLdapContext(props, null);
	    }
	    catch (NamingException e) {
	    	Codigo.log("Excepcion capturada", e);
	    	e.printStackTrace();
	    	throw e;
	        //throw new NamingException("Failed to connect to " + domainName + ((serverName==null)? "" : " through " + serverName));
	    }
	}
	
	
	
	private void getConnection() throws NamingException {
		this.context = getConnection0();
		
		this.hiloAntiTimeOut = new HiloAntiTimeout(this);
		this.hiloAntiTimeOut.start();
	}

	
	
	public void conectar() throws NamingException {
		getConnection();
	}
	
	public void test() throws NamingException {
		getConnection0();
	}

	
	
	private synchronized UsuarioAD getUserFromLdap(String username) throws NamingException
	{
		//Codigo.log("getUserFromLdap()", username);
		UsuarioAD uad = null;
		//String[] ATRIBUTOS = {"uid", "cn",  "mail", "title", "telephoneNumber", "us-telephoneNumberPrefix", "us-consejeria", "us-direcciongeneral"};
		
        try {
        	this.context.setRequestControls(new Control[] { new PagedResultsControl(20, Control.NONCRITICAL) });
        }
        catch (IOException ioex) {
        	Codigo.log("BuscarAD", ioex);
        }
        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //sc.setReturningAttributes(ATRIBUTOS);
        String cadenaBusqueda = String.format("(&(%s=%s))", "uid", username);
        
        NamingEnumeration<SearchResult> answer = this.context.search( "o=Junta de Castilla-La Mancha,c=es", cadenaBusqueda, sc);
        this.hiloAntiTimeOut.updateUltimoAcceso();

        if (answer.hasMoreElements()) {
            Attributes attr = answer.nextElement().getAttributes();
            
//            NamingEnumeration ne = attr.getAll();
//            
//            while (ne.hasMore()) {
//            	Codigo.log( ne.nextElement() );
//            }
            
            Attribute user = attr.get("uid");
            if (user != null) {
            	uad = new UsuarioAD(attr);
            }
        }
        
		return uad;
	}

	
	
    public UsuarioAD getUser(String username) throws NamingException
    {
    	UsuarioAD uad = memUsuarios.get(username);

    	if (uad == null) {
    		uad = getUserFromLdap(username);
    		memUsuarios.add(uad);
    	}
    	else {
    		Codigo.log(username, "en cache");
    	}
    	
    	return uad;
    }

	
	
//    private List<UsuarioAD> buscarUsuarios(String campo, String patron) throws NamingException, IOException
//    {
//    	Codigo.log(campo, patron);
//    	ArrayList<UsuarioAD> users = new ArrayList<UsuarioAD>();
//    	String[] ATRIBUTOS = {"us-direccionip", "uid", "cn"};
//
//        try {
//    	    byte[] cookie = null;
//        	SearchControls sc = new SearchControls();
//    	    sc.setReturningObjFlag (true); 
//    	    sc.setReturningAttributes(ATRIBUTOS);
//    	    sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
//
//           	this.context.setRequestControls(new Control[] { new PagedResultsControl(PAGE_SIZE, Control.CRITICAL) });
//
//           	do {
//           		String cadenaBusqueda = String.format("(&(%s=*%s*)(l=Ciudad Real))", campo, patron);
//				NamingEnumeration<SearchResult> results = this.context.search("o=Junta de Castilla-La Mancha,c=es", cadenaBusqueda, sc);
//				this.hiloAntiTimeOut.updateUltimoAcceso();
//				
//				while (results != null && results.hasMore()) {
//					// Display an entry
//					SearchResult entry = (SearchResult) results.next();
//					System.out.println(entry);
//					UsuarioAD uad = new UsuarioAD(entry.getAttributes());
//					if (uad.getIP() != null) users.add(uad);
//					// Handle the entry's response controls (if any)
//					if (entry instanceof HasControls) {
//						// ((HasControls)entry).getControls();
//					}
//				}
//				
//				// Examine the paged results control response
//				Control[] controls = this.context.getResponseControls();
//				if (controls != null) {
//					for (int i = 0; i < controls.length; i++) {
//						if (controls[i] instanceof PagedResultsResponseControl) {
//							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
//							cookie = prrc.getCookie();
//						} else {
//							// Handle other response controls (if any)
//						}
//					}
//				}
//				
//				// Re-activate paged results
//				this.context.setRequestControls(new Control[] { new PagedResultsControl(PAGE_SIZE, cookie, Control.CRITICAL) });
//           	} while (cookie != null);
//	    }
//	    catch (SizeLimitExceededException ex) {
//	    	System.out.println("Excepcion controlada " + ex);
//	    }
//
//        return users;
//    }

	
	
    
	public void comprobarConexion()
	{
		try {
			MemUsuariosAD mem = MemUsuariosAD.getInstance();
			Codigo.log("mem", mem);
			String uid = mem.getUsuarioAzar();
			//Codigo.log("azar", uid);
			UsuarioAD uad = getUserFromLdap(uid);
			mem.add(uad);
			//Codigo.log("get user from ldap, ha funcionado");
		}
		catch (Exception ex0) {
			//Codigo.log("fallo azar");
			this.hiloAntiTimeOut.parar();
			this.hiloAntiTimeOut = null;
			
			try {
				getConnection();
			}
			catch (NamingException ne) {
				Codigo.log("No se puede reconectar");
			}
		}
	}
	

	
//    public List<UsuarioAD> buscarPorNombre(String patron) throws NamingException, IOException
//    {
//    	comprobarConexion();
//    	return buscarUsuarios("cn", patron);
//    }
    
    
    
//    public List<UsuarioAD> buscarPorCorreo(String patron) throws NamingException, IOException
//    {
//    	comprobarConexion();
//    	return buscarUsuarios("mail", patron);
//    }
    
    
    
    
    private List<RegistroOCS> buscarUsuarios(String campo, String patron) throws NamingException, IOException
    {
    	Codigo.log(campo, patron);
    	ArrayList<RegistroOCS> users = new ArrayList<RegistroOCS>();
    	String[] ATRIBUTOS = {"uid", "cn"};

        try {
    	    byte[] cookie = null;
        	SearchControls sc = new SearchControls();
    	    sc.setReturningObjFlag (true); 
    	    sc.setReturningAttributes(ATRIBUTOS);
    	    sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

           	this.context.setRequestControls(new Control[] { new PagedResultsControl(PAGE_SIZE, Control.CRITICAL) });

           	do {
           		String cadenaBusqueda = null;
           		if (patron.contains("*")) {
           			cadenaBusqueda = String.format("(&(%s=%s))", campo, patron);
           		}
           		else {
           			cadenaBusqueda = String.format("(&(%s=*%s*))", campo, patron);
           		}
           		
				NamingEnumeration<SearchResult> results = this.context.search("o=Junta de Castilla-La Mancha,c=es", cadenaBusqueda, sc);
				this.hiloAntiTimeOut.updateUltimoAcceso();
				
				while (results != null && results.hasMore()) {
					// Display an entry
					SearchResult entry = (SearchResult) results.next();
//					System.out.println(entry);
					UsuarioAD uad = new UsuarioAD(entry.getAttributes());
					//if (uad.getIP() != null) 
					users.add(uad.getRegistroOCS());
					
					// Handle the entry's response controls (if any)
//					if (entry instanceof HasControls) {
//						// ((HasControls)entry).getControls();
//					}
				}
				
				// Examine the paged results control response
				Control[] controls = this.context.getResponseControls();
				if (controls != null) {
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof PagedResultsResponseControl) {
							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
							cookie = prrc.getCookie();
						} else {
							// Handle other response controls (if any)
						}
					}
				}
				
				// Re-activate paged results
				this.context.setRequestControls(new Control[] { new PagedResultsControl(PAGE_SIZE, cookie, Control.CRITICAL) });
           	} while (cookie != null);
	    }
	    catch (SizeLimitExceededException ex) {
	    	System.out.println("Excepcion controlada " + ex);
	    }

        return users;
    }

	
	
    
    public List<RegistroOCS> buscarPorNombreUsuario(String patron) throws NamingException, IOException
    {
    	comprobarConexion();
    	return buscarUsuarios("cn-ascii", patron);
    }
    
    
    
    public List<RegistroOCS> buscarPorCorreoUsuario(String patron) throws NamingException, IOException
    {
    	comprobarConexion();
    	return buscarUsuarios("mail", patron);
    }
    
    
    
    
	@Override
	public void antiTimeOutMethod() 
	{
		Codigo.log("antiTO BuscarUsuarioAD");
		comprobarConexion();
	}

	
	
//	public static void main(String...arg) {
//		
//		try {
//			BuscarAD bad = new BuscarAD("ffgr33", "Fujitsu20");
//			
//			bad.getConnection();
//			
//			UsuarioAD uad = bad.getUserFromLdap("ffgr33");
//			System.out.println(uad);
//			
//			uad = bad.getUserFromLdap("vvec01");
//			System.out.println(uad);
//			
////			List<Usuario> lu = bad.buscarUsuarios("cn", "Fr*");
////			int nusus = lu.size();
////			for (Usuario u: lu) {
////				System.out.println(u);
////			}
////			System.out.println(nusus);
//		}
//		catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		
//		System.out.println("FIN");
//	}
	
	
	
}
