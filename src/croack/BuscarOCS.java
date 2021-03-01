package croack;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import croack.ed.RegistroOCS;
import croack.noto.HiloAntiTimeout;
import croack.noto.IAntiTimeOut;
import croack.util.Codigo;
import croack.util.StringExtractor;





/**
 * "CR"  "OCS_Cr!"
 */
public class BuscarOCS 
implements IAntiTimeOut
{
	
	public final static String[]  IGNORAR_COLUMNA = {"Account info: TAG", "CPU (MHz)", "Select", "Delete"};

	private HiloAntiTimeout hiloAntiTimeout;
	private String ocsUser, ocsPwd;
	private DefaultHttpClient client = new DefaultHttpClient();
	private List<String[]> camposRaros;
	private List<String> nombresColumnas = null;
	private List<Boolean> ignorarColumnas = null;
	
	
	
	private BuscarOCS()  {}
	
	
	
	public BuscarOCS(String ocsUser, String ocsPwd) {
		this.ocsUser = ocsUser;
		this.ocsPwd = ocsPwd;
	}
	
	

	
    public static void consumir(HttpResponse r) throws IOException
    {
    	EntityUtils.consume( r.getEntity() );
    }
    
    
    
    public static byte[] contenido(HttpResponse e) throws IOException
    {
    	int tam = -1, bajado = 0, n=0;
    	
    	HttpEntity entity = e.getEntity();
    	byte[] minibuffer = new byte[(int) entity.getContentLength()];
    	InputStream is = e.getEntity().getContent();
    	
   		while (bajado < tam) {
   			n = is.read(minibuffer, bajado, tam-bajado);
   			bajado += n;
   			//log(n, bajado);
    	}
    	
   		return minibuffer;
    }
    
    
    
    public static void grabarContenido(HttpResponse response, String fichero) throws IOException
    {
    	InputStream is = response.getEntity().getContent();
    	FileOutputStream fos = new FileOutputStream(fichero);
    	byte[] buffer = new byte[16000];
    	int leido = 0;
    	
    	while ( (leido = is.read(buffer)) != -1 ) {
    		fos.write(buffer,  0,  leido);
    	}
    	fos.close();
    	is.close();
    }
    
    
    
    public static void grabarHtml(String html, String fichero) throws IOException
    {
    	FileOutputStream fos = new FileOutputStream(fichero);
    	
    	fos.write(html.getBytes());
    	
    	fos.close();
    }
    
    

    
    public static String contenidoTexto(HttpResponse response) {
    	return content(response.getEntity());
    }
    
    
    public static String content(HttpEntity e)  {
    	try {
	        InputStream is = e.getContent();
	        byte[] buffer = new byte[1024];
	        StringBuilder sb = new StringBuilder();

	        try {
		        while (true) {
		        	int n = is.read(buffer);
		        	//System.out.print(new String(buffer,0,n));
		        	sb.append(new String(buffer,0,n));
		        }
	        }
	        catch (Exception e0) {}
	        
	        return sb.toString();
    	}
        catch (Exception ee){
        	return "";
        }
    }
    
    
    
    public static void printHeaders(HttpResponse e)
    {
    	Header[] hh = e.getAllHeaders();
    	Codigo.log("---------- RESPONSE HEADERS ----------");
    	for (Header h: hh) {
    		Codigo.log(h.getName()+": "+h.getValue());
    	}
    	Codigo.log("---------- -------- ------- ----------");
    }
 
	
    
    
	public static void cabecerasComunes(HttpRequestBase r)
	{
		r.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		r.addHeader("Accept-Language", "es-ES,es;q=0.8");
		r.addHeader("Host", "ocs.jclm.es");
		//r.addHeader("Connection", "close");
		r.addHeader("Upgrade-Insecure-Requests", "1");
		r.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
	}
	
	
	
	public static void rellenarFormulario(HttpPost r, String...params) throws UnsupportedEncodingException
	{
		List <NameValuePair> lista = new ArrayList<NameValuePair>();
		int i = 0;
		String n, v;
		
		try {
			while (true) {
				n = params[i++];
				v = params[i++];
				lista.add( new BasicNameValuePair(n,v) );
			}
		}
		catch (Exception e) {}		
		//r.addHeader("Content-Type", "application/x-www-form-urlencoded");
		r.setEntity(new UrlEncodedFormEntity(lista, HTTP.UTF_8));
	}
	
	
	
	public static void rellenarFormulario(HttpPost r, List<String[]> camposCSFR, String...params) throws UnsupportedEncodingException
	{
		List <NameValuePair> lista = new ArrayList<NameValuePair>();
		int i = 0;
		String n, v;
		
		try {
			while (true) {
				n = params[i++];
				v = params[i++];
				//log("\t", n, "=", v);
				lista.add( new BasicNameValuePair(n,v) );
			}
		}
		catch (Exception e) {}	
		
		if (camposCSFR != null)
			for (String[] x: camposCSFR) {
				//log("\t", x[0], "=", x[1]);
				lista.add( new BasicNameValuePair( x[0], x[1]) );
			}
		
		//r.addHeader("Content-Type", "application/x-www-form-urlencoded");
		r.setEntity(new UrlEncodedFormEntity(lista, HTTP.UTF_8));
	}
	
	
	
	public static String getHeader(HttpResponse e, String headerName)
	{
    	Header[] hh = e.getAllHeaders();
    	
    	for (Header h: hh) {
    		//System.out.println(h);
    		if ( h.getName().equals(headerName) ) {
    			return h.getValue();
    		}
    	}
    	return null;
	}
	
	
	
	public static String getFieldValue(String html, String formFieldName)
	{
		return StringExtractor.extract(html, "name='"+formFieldName+"' id='"+formFieldName+"' value='", "'");
	}
	
	

    
    public static void printCookies(CookieStore cs)
    {
    	Codigo.log("----- COOKIES -----");
    	
    	for (Cookie c: cs.getCookies()) {
    		Codigo.log(c.getName(), "=", c.getValue());
    	}
    }
    
    
    
    public static List<String[]> getCamposRaros(String html)
    {
    	String name = "", value = "";
    	StringExtractor se = new StringExtractor(html);
    	List<String[]> campos = new ArrayList<String[]>();
    	
    	//log("----- CAMPOS RAROS ----");
    	
    	while (name != null) {
    		name = se.extract("'CSRF_", "'");
    		if (name != null) {
	    		name = "CSRF_" + name;
	    		value = se.extract("value='", "'");
	    		//System.out.println("CSRF_"+ name+"="+ value);
	    		campos.add( new String[] {name, value} );
    		}
    	}
    	return campos;
    }
	
    
	
	
    public static String extraerNombreColumna(String contenidoTD)
    {
    	StringBuilder sb = new StringBuilder();
    	int nivel = 0;
    	char c;
    	
    	for (int i = 0; i < contenidoTD.length(); i++) {
    		c = contenidoTD.charAt(i);
    		if (c == '<') nivel++;
    		else if (c == '>') nivel--;
    		else if (nivel == 0) sb.append(c);
    	}
    	return sb.toString();
    }
    
    
    
    public static List<String> extraerNombresColumnas(String html)
    {
    	StringExtractor se = new StringExtractor(html);
    	ArrayList<String> columnas = new ArrayList<String>();
    	String fila;
    	
    	while ( (fila = se.extract("<th class='ta'>", "</th>")) != null )  {
    		String col = extraerNombreColumna(fila);
    		columnas.add(col);
    		//log("+columna:", col);
    	}
    	return columnas;
    }
    
    
    
	public static boolean ignorarColumna(String columnName) {
		for (String x: IGNORAR_COLUMNA) {
			if (x.equals(columnName)) return true;
		}
		return false;
	}
	
	   
	
    public static List<Boolean> getIgnorarColumnas(List<String> nombresColumnas)
    {
    	List<Boolean> lic = new ArrayList<Boolean>(nombresColumnas.size());
    	
    	for (String x: nombresColumnas) {
    		lic.add( ignorarColumna(x) );
    	}
    	return lic;
    }
    
    
    
    public boolean test() throws IOException
    {
		HttpGet httpget;
		HttpPost httppost;
		HttpResponse response;
		String etag, html;
		
		
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
	    client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);


		System.out.print("1");
		httpget = new HttpGet("http://ocs.jclm.es");
		cabecerasComunes(httpget);
		httpget.addHeader("Connection", "close");
		response = client.execute(httpget);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		etag = getHeader(response, "ETag");
		html = contenidoTexto(response);
		
		
		// obtener el formulario
		System.out.print("2");
		httpget = new HttpGet("http://ocs.jclm.es/ocsreports/");
		httpget.addHeader("Referer", "http://ocs.jclm.es/");
		response = client.execute(httpget);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		html = contenidoTexto(response);
		//grabarHtml(html, "c:/temp/ocs-02.html");
		camposRaros = getCamposRaros(html);
		//System.out.println(camposRaros);
		
		
		System.out.print("3");
		httppost = new HttpPost("http://ocs.jclm.es/ocsreports/");
		cabecerasComunes(httppost);
		httppost.addHeader("Origin", "http://ocs.jclm.es");
		httppost.addHeader("Referer", "http://ocs.jclm.es/ocsreports/");
		rellenarFormulario(httppost, camposRaros, "LOGIN", this.ocsUser, "PASSWD", this.ocsPwd, "Valid_CNX", "Send");
		response = client.execute(httppost);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		html = contenidoTexto(response);
		//grabarHtml(html, "c:/temp/ocs-03.html");
		//System.out.println("VALIDADOS: " + html.indexOf("Machines in DB"));
		System.out.println(".");
		
//		this.hiloAntiTimeout = new HiloAntiTimeout(this); 
//		this.hiloAntiTimeout.start();

		
		return html.indexOf("Machines in DB") != -1;
    }
    
    
    
    public void conectar() throws IOException
    {
		HttpGet httpget;
		HttpPost httppost;
		HttpResponse response;
		String etag, html;

		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
	    client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);

	    Codigo.log(" Conectar-1 ");
		httpget = new HttpGet("http://ocs.jclm.es");
		cabecerasComunes(httpget);
		httpget.addHeader("Connection", "close");
		response = client.execute(httpget);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		etag = getHeader(response, "ETag");
		html = contenidoTexto(response);
		
		
		// obtener el formulario
		Codigo.log(" Conectar-2 ");
		httpget = new HttpGet("http://ocs.jclm.es/ocsreports/");
		httpget.addHeader("Referer", "http://ocs.jclm.es/");
		response = client.execute(httpget);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		html = contenidoTexto(response);
		//grabarHtml(html, "c:/temp/ocs-02.html");
		camposRaros = getCamposRaros(html);
		
		
		Codigo.log(" Conectar-3 ");
		httppost = new HttpPost("http://ocs.jclm.es/ocsreports/");
		cabecerasComunes(httppost);
		httppost.addHeader("Origin", "http://ocs.jclm.es");
		httppost.addHeader("Referer", "http://ocs.jclm.es/ocsreports/");
		rellenarFormulario(httppost, camposRaros, "LOGIN", this.ocsUser, "PASSWD", this.ocsPwd, "Valid_CNX", "Send");
		response = client.execute(httppost);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		html = contenidoTexto(response);
		//grabarHtml(html, "c:/temp/ocs-03.html");
		//log("VALIDADOS:", html.indexOf("inventoried") != -1, "\n");
		camposRaros = getCamposRaros(html);

		Codigo.log(" Conectar-4 ");

		
		// primera lista
		//log("4");
		httppost = new HttpPost("http://ocs.jclm.es/ocsreports/?function=visu_computers");
		cabecerasComunes(httppost);
		httppost.addHeader("Origin", "http://ocs.jclm.es");
		httppost.addHeader("Referer", "http://ocs.jclm.es/ocsreports/");
		rellenarFormulario(httppost, camposRaros, "RESET", "1", "LANG", "");
		response = client.execute(httppost);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		html = contenidoTexto(response);
		//grabarHtml(html, "c:/temp/ocs-04.html");
		camposRaros = getCamposRaros(html);

		Codigo.log(" Conectar-5. ");

		
		anadirCampo("Manufacturer");
		anadirCampo("Model");
		anadirCampo("Serial number");
		anadirCampo("Architecture");
		anadirCampo("IP address");
		//anadirCampo("Service pack");
		anadirCampo("OS Version");
		anadirCampo("Netmask");
		html = anadirCampo("CPU type");
		
		nombresColumnas = extraerNombresColumnas(html);
		ignorarColumnas = getIgnorarColumnas(nombresColumnas);
		//log(nombresColumnas);
		//log(ignorarColumnas);
		
		this.hiloAntiTimeout = new HiloAntiTimeout(this); 
		this.hiloAntiTimeout.start();
		
		Codigo.log("hil ato ocs=", this.hiloAntiTimeout);
    }
    
    
    
    public void desconectar()
    {
    	this.hiloAntiTimeout.parar();
    }
    
    
    
    private String anadirCampo(String campo)
    throws UnsupportedEncodingException, IOException
    {
		//log("añadir campo:", campo);
		HttpPost httppost = new HttpPost("http://ocs.jclm.es/ocsreports/?function=visu_computers");
		cabecerasComunes(httppost);
		httppost.addHeader("Origin", "http://ocs.jclm.es");
		httppost.addHeader("Referer", "http://ocs.jclm.es/ocsreports/?function=visu_computers");
		rellenarFormulario(httppost, camposRaros,
				"pcparpage", "20",
				"SHOW", "SHOW",
				"FILTRE", "",
				"FILTRE_VALUE", "",
				"RAZ_FILTRE", "",
				"restCollist_show_all", campo,
				"SUP_COL", "",
				"TABLE_NAME", "list_show_all",
				"RAZ", "",
				"page", "",
				"old_pcparpage", "20",
				"tri_list_show_all", "h.lastdate",
				"tri_fixe", "",
				"sens_list_show_all", "DESC",
				"SUP_PROF", "",
				"MODIF", "",
				"SELECT", "",
				"OTHER", "",
				"ACTIVE", "",
				"CONFIRM_CHECK", "",
				"OTHER_BIS", "",
				"OTHER_TER", "",
				"DEL_ALL", ""
				);
		HttpResponse response = client.execute(httppost);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		String html = contenidoTexto(response);
		//grabarHtml(html, "c:/temp/ocs-"+campo+".html");
		camposRaros = getCamposRaros(html);
		
		return html;
    }
    
    
    
//    public List<RegistroOCS> buscarPorEquipo(String filtro, List<RegistroOCS> registros, JTable tabla)
//    throws IOException
//    {
//    	return buscar("h.name", filtro, registros, tabla);
//    }
    
    
    public List<RegistroOCS> buscarPorEquipo(String filtro)
    throws IOException
    {
    	return buscar("h.name", filtro);
    }
    
    
    
//    public List<RegistroOCS> buscarPorUsuario(String filtro, List<RegistroOCS> registros, JTable tabla)
//    throws IOException
//    {
//    	return buscar("h.userid", filtro, registros, tabla);
//    }
    
    
    public List<RegistroOCS> buscarPorUsuario(String filtro)
    throws IOException
    {
    	return buscar("h.userid", filtro);
    }
    
    
    
//    public List<RegistroOCS> buscarPorIP(String filtro, List<RegistroOCS> registros, JTable tabla)
//    throws IOException
//    {
//    	return buscar("h.ipaddr", filtro, registros, tabla);
//    }
    
    
    public List<RegistroOCS> buscarPorIP(String filtro)
    throws IOException
    {
    	return buscar("h.ipaddr", filtro);
    }
    
    
    public RegistroOCS getEquipoPorIP(String ip)
    throws IOException
    {
    	List<RegistroOCS> rr = buscar("h.ipaddr", ip);
    	RegistroOCS registroMasNuevo = null;
    	
		for (RegistroOCS s: rr) {
			if (s.ip.equals(ip)) {
				if (registroMasNuevo == null) registroMasNuevo = s;
				else if (s.lastLogin.compareTo(registroMasNuevo.lastLogin) < 0) registroMasNuevo = s;
			}
		}
		return registroMasNuevo;
    }
    
    
    
    public List<RegistroOCS> getDatosBusqueda(String html) 
    {
    	if (html.indexOf("NO RESULT") != -1) {
    		// no hay datos
    		//log("no result");
    		return new ArrayList<RegistroOCS>();
    	}
    	else {
    		StringExtractor se = StringExtractor.create(html, "<tbody class='ta'>", "</tbody>");	    	
	    	List<RegistroOCS> registros = new ArrayList<>();
	    	List<String> fila;
	    	boolean hayFilas = true;
	    	int columnas = ignorarColumnas.size();
	    	String dato;
	    	boolean ignorar;
	    	int filasVisibles = -1;

	    	while (hayFilas) {
	    		String tr = se.extract("<tr", "</tr>");
				fila = new ArrayList<>();

				// obtenemos la url de ocs del equipo
	    		//System.out.println(tr);
	    		String urlOcs = StringExtractor.extract(tr, "href='", "'");
	    		fila.add(urlOcs);
	    		//Codigo.log(urlOcs);
	    		StringExtractor se2 = new StringExtractor(tr);
	    		se2.advanceTo(">");
	    		
	    		if (se2.contains("<td")) {

    				for (int col = 0; col < columnas; col++) {
    					ignorar = ignorarColumnas.get(col);
    					dato = se2.extract("<td class='ta' >", "</td>");
    					if (!ignorar) {
		    				if (dato.charAt(0) == '<') 
		    					dato = StringExtractor.extract(dato, ">", "<").trim();
		    				if (dato.equals("&nbsp")) dato = "";
		    				fila.add(dato);
    					}
	    			}
	    			
		    		RegistroOCS registro = new RegistroOCS(fila);
		    		registros.add(registro);
	    		}
	    		else hayFilas = false;

	    	}
	    	//System.out.println("fin");
    		return registros;
    	}
    }
    
    /**
     * 
     * @param html la página que tiene los datos del ocs que hay que extraer
     * @param registros si la lista no es nula, se le añaden los registros que se obtengan
     * @param tabla si la tabla no es nula se le hace un repaint cuando se le añaden las primeras filas
     * @return la lista de los registros obtenidos. si el parametro registros no era nulo, se devuelve la instancia de ese parametro
     */
//    public List<RegistroOCS> getDatosBusqueda(String html, List<RegistroOCS> registros, JTable tabla)
//    {
//    	if (html.indexOf("NO RESULT") != -1) {
//    		// no hay datos
//    		//log("no result");
//    		return new ArrayList<RegistroOCS>();
//    	}
//    	else {
//	    	StringExtractor se = StringExtractor.create(html, "<tbody class='ta'>", "</tbody>");	    	
//	    	List<RegistroOCS> filas;
//	    	List<String> fila;
//	    	boolean hayFilas = true;
//	    	int columnas = ignorarColumnas.size();
//	    	String dato;
//	    	boolean ignorar;
//	    	int filasVisibles = -1;
//
//	    	if (tabla != null) {
//	    		filasVisibles = tabla.getHeight() / tabla.getRowHeight() + 1;
//	    	}
//	    	//System.out.println("tenemos el html");
//	    	filas = (registros != null) ? registros : new ArrayList<RegistroOCS>();
//	    	
//	    	while (hayFilas) {
//	    		String tr = se.extract("<tr", "</tr>");
//				fila = new ArrayList<>();
//
//				// obtenemos la url de ocs del equipo
//	    		//System.out.println(tr);
//	    		String urlOcs = StringExtractor.extract(tr, "href='", "'");
//	    		fila.add(urlOcs);
//	    		//Codigo.log(urlOcs);
//	    		StringExtractor se2 = new StringExtractor(tr);
//	    		se2.advanceTo(">");
//	    		
//	    		if (se2.contains("<td")) {
//
//    				for (int col = 0; col < columnas; col++) {
//    					ignorar = ignorarColumnas.get(col);
//    					dato = se2.extract("<td class='ta' >", "</td>");
//    					if (!ignorar) {
//		    				if (dato.charAt(0) == '<') 
//		    					dato = StringExtractor.extract(dato, ">", "<").trim();
//		    				if (dato.equals("&nbsp")) dato = "";
//		    				fila.add(dato);
//    					}
//	    			}
//	    			
//		    		RegistroOCS registro = new RegistroOCS(fila);
//		    		filas.add(registro);
//		    		
//		    		if (tabla != null && filasVisibles >= filas.size()) {
//		    			//tabla.repaint();
//		    			tabla.revalidate();
//		    		}
//	    		}
//	    		else hayFilas = false;
//	    	}
//
//	    	//System.out.println("fin");
//	    	return filas;
//    	}
//    }
    
    
    
    private synchronized List<RegistroOCS> buscar(String nombreFiltro, String valorFiltro)
    		throws UnsupportedEncodingException, IOException
    {
    	//Codigo.log("buscarOCS hilo=" + this.hiloAntiTimeout);
		this.hiloAntiTimeout.updateUltimoAcceso();
		
    	HttpPost httppost = new HttpPost("http://ocs.jclm.es/ocsreports/?function=visu_computers");
		cabecerasComunes(httppost);
		httppost.addHeader("Origin", "http://ocs.jclm.es");
		httppost.addHeader("Referer", "http://ocs.jclm.es/ocsreports/?function=visu_computers");
		rellenarFormulario(httppost, camposRaros,
				"pcparpage", "100000",
				"SHOW", "SHOW",
				"FILTRE", nombreFiltro,
				"FILTRE_VALUE", valorFiltro.trim(),
				"RAZ_FILTRE", "",
				"SUB_FILTRE", "Filter",
				"restCollist_show_all", "",
				"SUP_COL", "",
				"TABLE_NAME", "list_show_all",
				"RAZ", "",
				"page", "0",
				"old_pcparpage", "100000",
				"tri_list_show_all", "h.lastdate",
				"tri_fixe", "",
				"sens_list_show_all", "DESC",
				"SUP_PROF", "",
				"MODIF", "",
				"SELECT", "",
				"OTHER", "",
				"ACTIVE", "",
				"CONFIRM_CHECK", "",
				"OTHER_BIS", "",
				"OTHER_TER", "",
				"DEL_ALL", ""
				);
		HttpResponse response = client.execute(httppost);
		//printHeaders(response);
		//printCookies(client.getCookieStore());
		String html = contenidoTexto(response);
		//grabarHtml(html, "c:/temp/s-busqueda.html");
		this.camposRaros = getCamposRaros(html);
		
		List<RegistroOCS> rr = getDatosBusqueda(html);
		//log("registros busqueda", registros.size());
		this.hiloAntiTimeout.updateUltimoAcceso();
		
		return rr;
    }
    
    
    
//    private synchronized List<RegistroOCS> buscar(String nombreFiltro, String valorFiltro, List<RegistroOCS> registros, JTable tabla)
//    		throws UnsupportedEncodingException, IOException
//    {
//    	//Codigo.log("buscarOCS hilo=" + this.hiloAntiTimeout);
//		this.hiloAntiTimeout.updateUltimoAcceso();
//		
//    	HttpPost httppost = new HttpPost("http://ocs.jclm.es/ocsreports/?function=visu_computers");
//		cabecerasComunes(httppost);
//		httppost.addHeader("Origin", "http://ocs.jclm.es");
//		httppost.addHeader("Referer", "http://ocs.jclm.es/ocsreports/?function=visu_computers");
//		rellenarFormulario(httppost, camposRaros,
//				"pcparpage", "100000",
//				"SHOW", "SHOW",
//				"FILTRE", nombreFiltro,
//				"FILTRE_VALUE", valorFiltro.trim(),
//				"RAZ_FILTRE", "",
//				"SUB_FILTRE", "Filter",
//				"restCollist_show_all", "",
//				"SUP_COL", "",
//				"TABLE_NAME", "list_show_all",
//				"RAZ", "",
//				"page", "0",
//				"old_pcparpage", "100000",
//				"tri_list_show_all", "h.lastdate",
//				"tri_fixe", "",
//				"sens_list_show_all", "DESC",
//				"SUP_PROF", "",
//				"MODIF", "",
//				"SELECT", "",
//				"OTHER", "",
//				"ACTIVE", "",
//				"CONFIRM_CHECK", "",
//				"OTHER_BIS", "",
//				"OTHER_TER", "",
//				"DEL_ALL", ""
//				);
//		HttpResponse response = client.execute(httppost);
//		//printHeaders(response);
//		//printCookies(client.getCookieStore());
//		String html = contenidoTexto(response);
//		//grabarHtml(html, "c:/temp/s-busqueda.html");
//		this.camposRaros = getCamposRaros(html);
//		
//		List<RegistroOCS> rr = getDatosBusqueda(html, registros, tabla);
//		//log("registros busqueda", registros.size());
//		this.hiloAntiTimeout.updateUltimoAcceso();
//		
//		return rr;
//    }
    
    
    
    
    private void busquedaNeutra()
    {
    	try {
    		//log("busqueda neutra");
    		//buscarPorIP("0.0.0.0", null, null);
    		buscarPorIP("0.0.0.0");
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    



	@Override
	public void antiTimeOutMethod() 
	{
		Codigo.log("antiTO BuscarOCS");
		busquedaNeutra();		
	}

	

}
