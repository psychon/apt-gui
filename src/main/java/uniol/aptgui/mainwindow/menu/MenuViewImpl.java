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

package uniol.aptgui.mainwindow.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.aptgui.swing.JMenuBarView;
import uniol.aptgui.swing.actions.ExitAction;
import uniol.aptgui.swing.actions.NewPetriNetAction;
import uniol.aptgui.swing.actions.NewTransitionSystemAction;
import uniol.aptgui.swing.actions.OpenAction;
import uniol.aptgui.swing.actions.SaveAction;
import uniol.aptgui.swing.actions.SaveAllAction;
import uniol.aptgui.swing.actions.SaveAsAction;

@SuppressWarnings("serial")
public class MenuViewImpl extends JMenuBarView<MenuPresenter> implements MenuView {

	private final JMenu fileMenu;

	private final JMenuItem newPn;
	private final JMenuItem newTs;
	private final JMenuItem open;
	private final JMenuItem save;
	private final JMenuItem saveAs;
	private final JMenuItem saveAll;
	private final JMenuItem exit;

	@Inject
	public MenuViewImpl(Injector injector) {
		fileMenu = new JMenu("File");
		add(fileMenu);

		newPn = new JMenuItem(injector.getInstance(NewPetriNetAction.class));
		newTs = new JMenuItem(injector.getInstance(NewTransitionSystemAction.class));
		open = new JMenuItem(injector.getInstance(OpenAction.class));
		save = new JMenuItem(injector.getInstance(SaveAction.class));
		saveAs = new JMenuItem(injector.getInstance(SaveAsAction.class));
		saveAll = new JMenuItem(injector.getInstance(SaveAllAction.class));
		exit = new JMenuItem(injector.getInstance(ExitAction.class));

		fileMenu.add(newPn);
		fileMenu.add(newTs);
		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(saveAll);
		fileMenu.addSeparator();
		fileMenu.add(exit);
	}

}


// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
