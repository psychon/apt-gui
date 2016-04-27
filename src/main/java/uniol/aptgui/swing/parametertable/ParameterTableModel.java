/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2016 Jonas Prellberg
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package uniol.aptgui.swing.parametertable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.analysis.language.Word;
import uniol.apt.module.impl.Parameter;

@SuppressWarnings("serial")
public class ParameterTableModel extends AbstractTableModel {

	private final String defaultString = new String();
	private final Word defaultWord = new Word();
	private final WindowReferencePn defaultWindowReferencePn = new WindowReferencePn(null);
	private final WindowReferenceTs defaultWindowReferenceTs = new WindowReferenceTs(null);

	private final List<Parameter> parameters;
	private final String[] columnNames = { "Parameter", "Value" };
	private final Object[][] values;

	public ParameterTableModel(List<Parameter> parameters) {
		this.parameters = parameters;
		values = new Object[parameters.size()][columnNames.length];
		for (int i = 0; i < parameters.size(); i++) {
			values[i][0] = parameters.get(i).getName();
			values[i][1] = getDefaultInstance(parameters.get(i).getKlass());
		}
	}

	private Object getDefaultInstance(Class<?> clazz) {
		if (clazz.equals(String.class)) {
			return defaultString;
		}
		if (clazz.equals(Word.class)) {
			return defaultWord;
		}
		if (clazz.equals(PetriNet.class)) {
			return defaultWindowReferencePn;
		}
		if (clazz.equals(TransitionSystem.class)) {
			return defaultWindowReferenceTs;
		}
		return null;
	}

	public boolean isParameterSet(int rowIndex) {
		return values[rowIndex][1] != getDefaultInstance(parameters.get(rowIndex).getKlass());
	}

	public Object getParameterValueAt(int rowIndex) {
		if (parameters.get(rowIndex).getKlass().equals(PetriNet.class)) {
			WindowReferencePn pnRef = (WindowReferencePn) values[rowIndex][1];
			return pnRef.getPetriNet();
		} else if (parameters.get(rowIndex).getKlass().equals(TransitionSystem.class)) {
			WindowReferenceTs tsRef = (WindowReferenceTs) values[rowIndex][1];
			return tsRef.getTransitionSystem();
		} else {
			return values[rowIndex][1];
		}
	}

	public Object[] getParameterValues() {
		List<Object> parameterValues = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
			if (isParameterSet(i)) {
				parameterValues.add(getParameterValueAt(i));
			}
		}
		return parameterValues.toArray();
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		values[rowIndex][columnIndex] = aValue;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		return values.length;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return values[rowIndex][columnIndex];
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
