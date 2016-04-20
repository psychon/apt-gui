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

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class PropertyTable extends JTable {

	private Class<?> editingClass;

	public PropertyTable(WindowReferenceProvider pnRefProvider, WindowReferenceProvider tsRefProvider) {
		setDefaultRenderer(WindowReference.class, new WindowReferenceRenderer());
		setDefaultEditor(WindowReferencePn.class, new WindowReferenceEditor(pnRefProvider));
		setDefaultEditor(WindowReferenceTs.class, new WindowReferenceEditor(tsRefProvider));
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (modelColumn == 1) {
			Object obj = getModel().getValueAt(row, modelColumn);
			Class<?> cellClass = Object.class;
			if (obj != null) {
				cellClass = obj.getClass();
			}
			return getDefaultRenderer(cellClass);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		editingClass = null;
		int modelColumn = convertColumnIndexToModel(column);
		if (modelColumn == 1) {
			Object obj = getModel().getValueAt(row, modelColumn);
			editingClass = Object.class;
			if (obj != null) {
				editingClass = obj.getClass();
			}
			return getDefaultEditor(editingClass);
		} else {
			return super.getCellEditor(row, column);
		}
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return editingClass != null ? editingClass : super.getColumnClass(column);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
