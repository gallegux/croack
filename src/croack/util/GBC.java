package croack.util;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.StringTokenizer;


public class GBC extends GridBagConstraints 
{
	
	public final String GRID = "grid";
	public final String GRID_X = "gridx";
	public final String GRID_Y = "gridy";
	public final String GRID_WIDTH = "gridwidth";
	public final String GRID_HEIGHT = "gridheight";
	public final String WEIGHT = "weight";
	public final String WEIGHT_X = "weightx";
	public final String WEIGHT_Y = "weighty";
	public final String ANCHOR = "anchor";
	public final String FILL = "fill";
	public final String INSETS = "insets";
	public final String INSETS_TOP = "insets.top";
	public final String INSETS_LEFT = "insets.left";
	public final String INSETS_BOTTOM = "insets.bottom";
	public final String INSETS_RIGHT = "insets.right";
	public final String IPAD = "ipad";
	public final String IPAD_X = "ipadx";
	public final String IPAD_Y = "ipady";
	
	
	
	public GBC()
	{
		super();
	}
	
	
	public GBC(String constraints)
	{
		super();
		
		set(constraints);
	}
	
	
	
	public GBC set(String constraints)
	{
		procesar(constraints);
		return this;
	}
	
	
	
	public GBC setGrid(int _gridx, int _gridy)
	{
		gridx = _gridx;
		gridy = _gridy;
		
		return this;
	}
	
	
	public GBC setWeight(int _wx, int _wy)
	{
		weightx = _wx;
		weighty = _wy;
		
		return this;
	}
	
	
	private void procesar(String constraints)
	{
		StringTokenizer st = new StringTokenizer(constraints);
		String token = null;
		
		while (st.hasMoreTokens()) {
			procesarToken(st.nextToken());
		}
	}
	
	
	private void procesarToken(String token)
	{
		try {
			StringTokenizer st = new StringTokenizer(token, "=");
			String propiedad = st.nextToken().toLowerCase();
			String valor = st.nextToken().toUpperCase();
			procesar(propiedad, valor);
		}
		catch (Exception e) {
			System.err.println("Error con el token " + token);
		}
		
	}
	
	
	private void procesar(String propiedad, String valor)
	{
		try {
			if (GRID.equals(propiedad)) {
				String[] valores = getValores(valor, 2);
				gridx = Integer.parseInt(valores[0]);
				gridy = Integer.parseInt(valores[1]);
			}
			else if (GRID_X.equals(propiedad)) {
				gridx = Integer.parseInt(valor);
			}
			else if (GRID_Y.equals(propiedad)) {
				gridy = Integer.parseInt(valor);
			}
			else if (GRID_WIDTH.equals(propiedad)) {
				gridwidth = Integer.parseInt(valor);
			}
			else if (GRID_HEIGHT.equals(propiedad)) {
				gridheight = Integer.parseInt(valor);
			}
			else if (WEIGHT.equals(propiedad)) {
				String[] valores = getValores(valor, 2);
				weightx = Double.parseDouble(valores[0]);
				weighty = Double.parseDouble(valores[1]);
			}
			else if (WEIGHT_X.equals(propiedad)) {
				weightx = Double.parseDouble(valor);
			}
			else if (WEIGHT_Y.equals(propiedad)) {
				weighty = Double.parseDouble(valor);
			}
			else if (ANCHOR.equals(propiedad)) {
				anchor = getValor(valor); //Integer.parseInt(valor);
			}
			else if (FILL.equals(propiedad)) {
				fill = getValor(valor);
			}
			else if (INSETS.equals(propiedad)) {
				String[] valores = getValores(valor, 4);
				int top = Integer.parseInt(valores[0]);
				int left = Integer.parseInt(valores[1]);
				int bottom = Integer.parseInt(valores[2]);
				int right = Integer.parseInt(valores[3]);
				insets = new Insets(top, left, bottom, right);
			}
			else if (INSETS_TOP.equals(propiedad)) {
				insets = new Insets(Integer.parseInt(valor), insets.left, insets.bottom, insets.right);
			}
			else if (INSETS_LEFT.equals(propiedad)) {
				insets = new Insets(insets.top, Integer.parseInt(valor), insets.bottom, insets.right);
			}
			else if (INSETS_BOTTOM.equals(propiedad)) {
				insets = new Insets(insets.top, insets.left, Integer.parseInt(valor), insets.right);
			}
			else if (INSETS_RIGHT.equals(propiedad)) {
				insets = new Insets(insets.top, insets.left, insets.bottom, Integer.parseInt(valor));
			}
			else if (IPAD.equals(propiedad)) {
				String[] valores = getValores(valor, 2);
				ipadx = Integer.parseInt(valores[0]);
				ipady = Integer.parseInt(valores[1]);
			}
			else if (IPAD_X.equals(propiedad)) {
				ipadx = Integer.parseInt(valor);
			}
			else if (IPAD_Y.equals(propiedad)) {
				ipady = Integer.parseInt(valor);
			}
		}
		catch (Exception e) {
			System.err.println("Error al procesar (" + propiedad + "," + valor + ")");
		}
	}
	
	
	
	private String[] getValores(String str_valores, int int_valores)
	{
		String[] arr_valores = new String[int_valores];
		
		StringTokenizer st = new StringTokenizer(str_valores, ",");
		
		for (int i = 0; i < int_valores; arr_valores[i++] = st.nextToken());
		
		return arr_valores;
	}
	
	
	
	private int getValor(String valor) throws Exception
	{
		if ("ABOVE_BASELINE".equals(valor)) {
			return GridBagConstraints.ABOVE_BASELINE;
		}
		else if ("ABOVE_BASELINE_LEADING".equals(valor)) {
			return GridBagConstraints.ABOVE_BASELINE_LEADING;
		} 
		else if ("ABOVE_BASELINE_TRAILING".equals(valor)) {
			return GridBagConstraints.ABOVE_BASELINE_TRAILING;
		} 
		else if ("BASELINE".equals(valor)) {
			return GridBagConstraints.BASELINE;
		} 
		else if ("BASELINE_LEADING".equals(valor)) {
			return GridBagConstraints.BASELINE_LEADING;
		} 
		else if ("BASELINE_TRAILING".equals(valor)) {
			return GridBagConstraints.BASELINE_TRAILING;
		} 
		else if ("BELOW_BASELINE".equals(valor)) {
			return GridBagConstraints.BELOW_BASELINE;
		} 
		else if ("BELOW_BASELINE_LEADING".equals(valor)) {
			return GridBagConstraints.BELOW_BASELINE_LEADING;
		} 
		else if ("BELOW_BASELINE_TRAILING".equals(valor)) {
			return GridBagConstraints.BELOW_BASELINE_TRAILING;
		} 
		else if ("BOTH".equals(valor)) {
			return GridBagConstraints.BOTH;
		} 
		else if ("CENTER".equals(valor)) {
			return GridBagConstraints.CENTER;
		} 
		else if ("EAST".equals(valor)) {
			return GridBagConstraints.EAST;
		} 
		else if ("FIRST_LINE_END".equals(valor)) {
			return GridBagConstraints.FIRST_LINE_END;
		} 
		else if ("FIRST_LINE_START".equals(valor)) {
			return GridBagConstraints.FIRST_LINE_START;
		} 
		else if ("HORIZONTAL".equals(valor)) {
			return GridBagConstraints.HORIZONTAL;
		} 
		else if ("LAST_LINE_END".equals(valor)) {
			return GridBagConstraints.LAST_LINE_END;
		} 
		else if ("".equals(valor)) {
			return GridBagConstraints.LAST_LINE_START;
		} 
		else if ("LINE_END".equals(valor)) {
			return GridBagConstraints.LINE_END;
		} 
		else if ("LINE_START".equals(valor)) {
			return GridBagConstraints.LINE_START;
		} 
		else if ("NONE".equals(valor)) {
			return GridBagConstraints.NONE;
		} 
		else if ("NORTH".equals(valor)) {
			return GridBagConstraints.NORTH;
		} 
		else if ("NORTHEAST".equals(valor)) {
			return GridBagConstraints.NORTHEAST;
		} 
		else if ("NORTHWEST".equals(valor)) {
			return GridBagConstraints.NORTHWEST;
		} 
		else if ("PAGE_END".equals(valor)) {
			return GridBagConstraints.PAGE_END;
		} 
		else if ("PAGE_START".equals(valor)) {
			return GridBagConstraints.PAGE_START;
		} 
		else if ("RELATIVE".equals(valor)) {
			return GridBagConstraints.RELATIVE;
		} 
		else if ("REMAINDER".equals(valor)) {
			return GridBagConstraints.REMAINDER;
		} 
		else if ("SOUTH".equals(valor)) {
			return GridBagConstraints.SOUTH;
		} 
		else if ("SOUTHEAST".equals(valor)) {
			return GridBagConstraints.SOUTHEAST;
		} 
		else if ("SOUTHWEST".equals(valor)) {
			return GridBagConstraints.SOUTHWEST;
		} 
		else if ("VERTICAL".equals(valor)) {
			return GridBagConstraints.VERTICAL;
		} 
		else if ("WEST ".equals(valor)) {
			return GridBagConstraints.WEST ;
		} 
		else {
			throw new Exception("Invalid value: " + valor);
		} 
	}
	

}
