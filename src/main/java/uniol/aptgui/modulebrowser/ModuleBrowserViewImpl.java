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
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import uniol.apt.module.Module;
import uniol.aptgui.swing.JPanelView;

@SuppressWarnings("serial")
public class ModuleBrowserViewImpl extends JPanelView<ModuleBrowserPresenter> implements ModuleBrowserView {

	private final JLabel header;
	private final JTextField searchBox;

	private JList<Module> list;
	private DefaultListModel<Module> model;

	public ModuleBrowserViewImpl() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 400));

		header = new JLabel("Double-click a module to open it...");
		searchBox = new JTextField();

		add(header, BorderLayout.PAGE_START);
		add(searchBox, BorderLayout.PAGE_END);
	}

	@Override
	public void setModules(Collection<Module> modules) {
		model = new DefaultListModel<>();
		for (Module module : modules) {
			model.addElement(module);
		}

		list = new JList<>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Add double-click support to list.
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					onListDoubleClick();
				}
			}
		});

		// Replace list.
		add(new JScrollPane(list), BorderLayout.CENTER);
	}

	private void onListDoubleClick() {
		Module selection = list.getSelectedValue();
		if (selection != null) {
			getPresenter().onModuleRequestOpen(selection);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
