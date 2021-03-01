package croack;


import java.awt.Color;
import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class CTE 
{
	public static String APP_TITLE = "Croack  (v.{version}-β) - {username}";
	public static String VERSION = "21-02-23"; // anio-mes-dia
	
//	public final static String LOC_SHARED = "http://10.132.132.200/croack/";
//	public final static String IP_UPDATE = "10.132.132.200";
//	public final static int PORT_UPDATE = 9999;
	
//	public final static String F_BOTONERA = System.getenv("LOCALAPPDATA") + "/" + "crkbotonera.txt";
	public final static String F_CONFIG_CONSOLA = System.getenv("LOCALAPPDATA") + "/" + "crkcfgui.bin";
	public final static String F_CONFIG_CONEXIONES = System.getenv("LOCALAPPDATA") + "/" + "crkconns.bin";
	public final static String F_BAT_AUX = System.getenv().get("TEMP") + "/crkcmd.bat";
	public final static String F_BD_USUARIOS = System.getenv("LOCALAPPDATA") + "/" + "crkusrs.bin";
	
	public final static Font TABLA_HEADER_FONT = new Font("Arial", Font.BOLD, 12);
	public final static Color TABLA_HEADER_FONDO_COLOR = new Color(0,93,172);
	public final static Color TABLA_HEADER_TEXTO_COLOR = Color.WHITE;

	public final static Color TABLA_FONDO_COLOR = new Color(245,245,245);
	public final static Font TABLA_FONT = new Font("Tahoma", Font.PLAIN, 14);
	public final static Color TABLA_GRID_COLOR = new Color(192,192,192);
	public final static Color TABLA_FILA_SELECCIONADA_COLOR = Color.YELLOW;
	public final static Font INFO_LABEL_FONT = new Font("Tahoma", Font.PLAIN, 13);
	public final static Font INFO_VALUE_FONT = new Font("Tahoma", Font.PLAIN, 14);
	
	public final static Color FILTRO_OCS_COLOR = new Color(0,92,0);
	public final static Color FILTRO_LDAP_COLOR = new Color(92,0,0);
	
	public final static int SEPARACION_ELEMENTOS = 7;
	
	public final static long DIAS_CADUCIDAD_DATOS = 31;
	
	
	static
	{
		UIDefaults defaults = UIManager.getDefaults( );
		
		defaults.put("TableHeader.background", TABLA_HEADER_FONDO_COLOR);
		defaults.put("TableHeader.foreground", TABLA_HEADER_TEXTO_COLOR);
		defaults.put("TableHeader.font", TABLA_HEADER_FONT );
		defaults.put("TableHeader.cellBorder", new javax.swing.border.MatteBorder (0,0,1,1, Color.LIGHT_GRAY));
		
		defaults.put("Table.gridColor", TABLA_GRID_COLOR);
		defaults.put("Table.focusCellHighlightBorder", new EmptyBorder(0, 10, 0, 0));
		defaults.put("Table.cellNoFocusBorder", new EmptyBorder(0, 10, 0, 0));
		defaults.put("Table.selectionBackground", CTE.TABLA_FILA_SELECCIONADA_COLOR);
		defaults.put("Table.focusSelectedCellHighlightBorder", null);
		defaults.put("Table.font", TABLA_FONT);

		defaults.put("List.noFocusBorder", new EmptyBorder(2, 7, 2, 7));
		defaults.put("CheckBoxMenuItem.border", new EmptyBorder(2, 7, 2, 7));

		defaults.put("TextField.font", TABLA_FONT );
		
		defaults.put("Viewport.background", CTE.TABLA_FONDO_COLOR);
	}
	
	
	
}
