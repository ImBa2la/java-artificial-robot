/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.styskin.ca.functions.SaveLoadParameters;

public class LambdaTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -3293948872303745175L;
		
	Map<String, Double> lambda;
	List<String> keys;
	Runnable callback;
	
	public LambdaTableModel(Runnable callback) {
		this.callback = callback;
	}
	
	private SaveLoadParameters operator;
	
	public void setSaveLoadParameters(SaveLoadParameters operator) {
		this.operator = operator; 
		lambda = new HashMap<String, Double>();
		this.operator.saveParameters(lambda);
		keys = new ArrayList<String>(lambda.keySet());		
	}
	

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return lambda.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= 0 && rowIndex < keys.size()) {
			switch(columnIndex) {
			case 0:
				return keys.get(rowIndex);
			case 1:
				return lambda.get(keys.get(rowIndex));
			}
		}		
		return null;
	}

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	lambda.put(keys.get(rowIndex), (Double) aValue);
    	callback.run();
    }

    public void save() {
    	operator.loadParameters(lambda);
    }
    
    
    public String getColumnName(int column) {
		switch(column) {
		case 0:
			return "��������";
		case 1:
			return "��������";
		}
		return "";
    }
	
    public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
		case 0:
			return String.class;
		case 1:
			return Double.class;
		}
		return Object.class;    	
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return columnIndex == 1;
    }    

  
}
