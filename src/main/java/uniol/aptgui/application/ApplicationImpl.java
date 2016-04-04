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

package uniol.aptgui.application;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.aptgui.gui.mainwindow.MainWindowPresenter;
import uniol.aptgui.gui.mainwindow.WindowId;

public class ApplicationImpl implements Application {

	private final MainWindowPresenter mainWindow;
	private final Map<WindowId, PetriNet> petriNets;

	@Inject
	public ApplicationImpl(MainWindowPresenter mainWindow) {
		this.mainWindow = mainWindow;
		this.petriNets = new HashMap<>();
	}

	public void newPetriNet() {
		// TODO: open dialog (using MainWindow) and ask for a name
		PetriNet pn = new PetriNet();
		pn.createPlace("p0");
		pn.createPlace("p1");
		pn.createTransition("t0");
		pn.createFlow("p0", "t0", 1);
		pn.createFlow("t0", "p1", 5);
		WindowId id = mainWindow.createWindow(pn);
		petriNets.put(id, pn);
	}

	public void closeWindow(WindowId id) {
		mainWindow.removeWindow(id);
	}

	@Override
	public void show() {
		mainWindow.show();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
