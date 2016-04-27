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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import uniol.apt.module.impl.ReturnValue;

@SuppressWarnings("serial")
public class ResultTableModel extends AbstractTableModel {

	private final String[] columnNames = { "Return value", "Value" };
	private final Object[][] values;

	public ResultTableModel(List<ReturnValue> returnValues, List<Object> filledValues) {
		values = new Object[returnValues.size()][columnNames.length];
		for (int i = 0; i < returnValues.size(); i++) {
			values[i][0] = returnValues.get(i).getName();
			values[i][1] = filledValues.get(i);
		}
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
