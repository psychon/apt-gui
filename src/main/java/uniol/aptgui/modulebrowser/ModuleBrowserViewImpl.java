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

package uniol.aptgui.modulebrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import uniol.aptgui.swing.JPanelView;
import uniol.aptgui.swing.moduletable.ModuleTableModel;

@SuppressWarnings("serial")
public class ModuleBrowserViewImpl extends JPanelView<ModuleBrowserPresenter> implements ModuleBrowserView {

	private final JLabel header;
	private final JTextField searchBox;

	private JTable moduleTable;
	private TableRowSorter<ModuleTableModel> rowSorter;

	public ModuleBrowserViewImpl() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 400));

		header = new JLabel("Double-click a module to open it...");

		moduleTable = new JTable();
		moduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					onListDoubleClick();
				}
			}
		});

		searchBox = new JTextField();
		searchBox.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				filter();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				filter();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				filter();
			}
		});

		add(header, BorderLayout.PAGE_START);
		add(new JScrollPane(moduleTable), BorderLayout.CENTER);
		add(searchBox, BorderLayout.PAGE_END);
	}

	private void onListDoubleClick() {
		int viewRow = moduleTable.getSelectedRow();
		int modelRow = moduleTable.convertRowIndexToModel(viewRow);
		getPresenter().onModuleRequestOpen(modelRow);
	}

	/**
	 * Called when the textfield content changes to filter the module table.
	 */
	private void filter() {
		RowFilter<ModuleTableModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		try {
			rf = RowFilter.regexFilter(searchBox.getText(), 0);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		rowSorter.setRowFilter(rf);
	}

	@Override
	public void setModuleTableModel(ModuleTableModel tableModel) {
		rowSorter = new TableRowSorter<>(tableModel);
		moduleTable.setModel(tableModel);
		moduleTable.setRowSorter(rowSorter);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
