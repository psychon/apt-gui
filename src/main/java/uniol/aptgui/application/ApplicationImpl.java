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
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.gui.mainwindow.MainWindowPresenter;
import uniol.aptgui.gui.mainwindow.WindowId;

public class ApplicationImpl implements Application {

	private final MainWindowPresenter mainWindow;
	private final Map<WindowId, PetriNet> petriNets;
	private final Map<WindowId, TransitionSystem> transitionSystems;

	private WindowId activeWindow;

	@Inject
	public ApplicationImpl(MainWindowPresenter mainWindow) {
		this.mainWindow = mainWindow;
		this.petriNets = new HashMap<>();
		this.transitionSystems = new HashMap<>();
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

	@Override
	public void onInternalWindowActivated(WindowId id) {
		activeWindow = id;
		if (petriNets.containsKey(id)) {
			mainWindow.showPnToolbar();
		} else if (transitionSystems.containsKey(id)) {
			mainWindow.showTsToolbar();
		}
	}

	@Override
	public WindowId getActiveWindow() {
		return activeWindow;
	}

	@Override
	public void newTransitionSystem() {
		// TODO open dialog (using MainWindow) and ask for a name
		TransitionSystem ts = new TransitionSystem();
		State s0 = ts.createState();
		State s1 = ts.createState();
		ts.setInitialState(s0);
		ts.createArc(s0, s1, "a");
		ts.createArc(s1, s0, "b");
		WindowId id = mainWindow.createWindow(ts);
		transitionSystems.put(id, ts);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
