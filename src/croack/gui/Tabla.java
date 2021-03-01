package croack.gui;


import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import croack.ed.IP;
import croack.ed.RegistroOCS;
import croack.util.Codigo;



public class Tabla extends JTable
{
	
	private final static int[] MINIMUM_WIDTHS = {165, 130, 130, 100}; 
	private final static int[] MAXIMUM_WIDTHS = {165, 130, 130, 1000}; 
	private final static String[] COLUMNAS = {"Hostname", "IP", "Usuario", "Nombre"}; 

	public MiTableModel modelo;
	private TableRowSorter sorter = null; 
	
	
	public Tabla()
	{
		super();
		
		this.modelo = new MiTableModel();
		this.setModel(this.modelo);
		
		getTableHeader().setReorderingAllowed(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setRowHeight( getRowHeight() + 8);
		
		getTableHeader().setPreferredSize(new Dimension(getTableHeader().getWidth(), getRowHeight()));
		
		setColumnsWidth();
		
		
		//TableRowSorter sorter = new TableRowSorter(this.modelo);
		//this.setRowSorter(sorter);
	}
	
	
	private void setColumnsWidth()
	{
		TableColumnModel tcm = getColumnModel();
		
		for (int i = 0; i < MINIMUM_WIDTHS.length; i++) {
			tcm.getColumn(i).setMinWidth(MINIMUM_WIDTHS[i]);
			tcm.getColumn(i).setMaxWidth(MAXIMUM_WIDTHS[i]);
			//tcm.getColumn(i).setWidth(widths[i]);
		}
	}

	
	
	public int[] getColumnsWidth()
	{
		int int_columnas = getColumnCount();
		TableColumnModel tcm = getColumnModel();
		int[] anchos = new int[int_columnas];
		
		for (int i = 0; i < int_columnas; i++) {
			anchos[i] = tcm.getColumn(i).getWidth();
		}
		
		return anchos;
	}

	
	
	public List<RegistroOCS> getRegistros()
	{
		return this.modelo.registros;
	}
	
	
	
	public void clear()
	{
		this.modelo.registros.clear();
		
		this.modelo = new MiTableModel();
		this.setModel(this.modelo);
		setColumnsWidth();
		
		this.sorter = new TableRowSorter(this.modelo);
		this.setRowSorter(sorter);
	}
	
	
	public void addRegistro(RegistroOCS r) {
		this.modelo.registros.add(r);
	}
	
	
	public void addRegistros(List<RegistroOCS> reg)
	{
		this.modelo.registros.addAll(reg);
	}
	
	
	
	public RegistroOCS getRegistro(int i)
	{
		int x = sorter.convertRowIndexToModel(i);
		return this.modelo.registros.get(x);
	}
	
	
	
	
	class MiTableModel implements TableModel 
	{
		public List<RegistroOCS> registros = new ArrayList<>();
		
		
		@Override
		public void addTableModelListener(TableModelListener arg0) {}

		@Override
		public Class<?> getColumnClass(int col) {
			if (col == 1)
				// col IP
				return IP.class;
			else
				return String.class;
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public String getColumnName(int n) {
			return COLUMNAS[n];
		}

		@Override
		public int getRowCount() {
			try {
				return registros.size();
			}
			catch (NullPointerException e) {
				return 0;
			}
		}

		
		@Override
		public Object getValueAt(int fila, int columna) {
			try {
				RegistroOCS r = registros.get(fila);
				
				switch (columna) {
				case 0:	return r.computerName;
				case 1: return r.getIP();
				case 2: return r.usuario;
				case 3: return r.nombreUsuario;
				default: return null;
				}
			}
			catch (Exception e) {
				return null;
			}
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}

		@Override
		public void removeTableModelListener(TableModelListener arg0) {}

		@Override
		public void setValueAt(Object arg0, int arg1, int arg2) {}
	}
		

}
