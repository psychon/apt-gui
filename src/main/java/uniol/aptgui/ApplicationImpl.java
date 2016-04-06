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

package uniol.aptgui;

import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.commands.History;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;

public class ApplicationImpl implements Application {

	private final MainWindowPresenter mainWindow;
	private final EventBus eventBus;
	private final History history;

	private final Map<WindowId, PetriNet> petriNets;
	private final Map<WindowId, TransitionSystem> transitionSystems;

	private WindowId activeWindow;

	@Inject
	public ApplicationImpl(MainWindowPresenter mainWindow, EventBus eventBus, History history) {
		this.mainWindow = mainWindow;
		this.eventBus = eventBus;
		this.history = history;
		this.petriNets = new HashMap<>();
		this.transitionSystems = new HashMap<>();

		this.eventBus.register(this);
	}

	@Subscribe
	public void onWindowFocusEvent(WindowFocusGainedEvent e) {
		activeWindow = e.getWindowId();
	}

	@Override
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

	@Override
	public void closeWindow(WindowId id) {
		mainWindow.removeWindow(id);
	}

	@Override
	public void show() {
		mainWindow.show();
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

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public History getHistory() {
		return history;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
