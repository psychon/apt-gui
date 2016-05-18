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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import uniol.aptgui.swing.JPanelView;
import uniol.aptgui.swing.moduletable.ModuleRowFilter;
import uniol.aptgui.swing.moduletable.ModuleTable;
import uniol.aptgui.swing.moduletable.ModuleTableModel;

@SuppressWarnings("serial")
public class ModuleBrowserViewImpl extends JPanelView<ModuleBrowserPresenter> implements ModuleBrowserView {

	private final JLabel categoryFilterText;
	private final JComboBox<String> categoryFilter;
	private final JLabel searchBoxText;
	private final JTextField searchBox;
	private final JLabel explanatoryText;
	private final ModuleTable moduleTable;

	private final ModuleRowFilter moduleRowFilter;
	private TableRowSorter<ModuleTableModel> rowSorter;

	public ModuleBrowserViewImpl() {
		moduleRowFilter = new ModuleRowFilter();

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(300, 400));

		categoryFilterText = new JLabel("Category filter");
		categoryFilterText.setAlignmentX(Component.LEFT_ALIGNMENT);

		categoryFilter = new JComboBox<>();
		categoryFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
		categoryFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filter();
			}
		});

		searchBoxText = new JLabel("Name filter");
		searchBoxText.setAlignmentX(Component.LEFT_ALIGNMENT);

		searchBox = new JTextField();
		searchBox.setAlignmentX(Component.LEFT_ALIGNMENT);
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

		explanatoryText = new JLabel("Double-click a module to open it...");
		explanatoryText.setAlignmentX(Component.LEFT_ALIGNMENT);

		moduleTable = new ModuleTable();
		moduleTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					onListDoubleClick();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(moduleTable);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(categoryFilterText);
		add(Box.createVerticalStrut(2));
		add(categoryFilter);
		add(Box.createVerticalStrut(5));
		add(searchBoxText);
		add(Box.createVerticalStrut(2));
		add(searchBox);
		add(Box.createVerticalStrut(5));
		add(explanatoryText);
		add(Box.createVerticalStrut(2));
		add(scrollPane);
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
		String selection = (String) categoryFilter.getSelectedItem();
		moduleRowFilter.setCategoryFilter(selection);
		moduleRowFilter.setNameFilter(searchBox.getText());
		rowSorter.setRowFilter(moduleRowFilter);
	}

	@Override
	public void setModuleTableModel(ModuleTableModel tableModel) {
		rowSorter = new TableRowSorter<>(tableModel);
		moduleTable.setModel(tableModel);
		moduleTable.setRowSorter(rowSorter);
		moduleTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		moduleTable.getColumnModel().getColumn(1).setPreferredWidth(30);
		moduleTable.getColumnModel().getColumn(2).setPreferredWidth(200);
	}

	@Override
	public void setCategoryFilters(List<String> categoryStrings) {
		categoryFilter.removeAllItems();
		for (String cat : categoryStrings) {
			categoryFilter.addItem(cat);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
