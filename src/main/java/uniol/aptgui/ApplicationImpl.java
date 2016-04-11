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

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.io.parser.ParseException;
import uniol.apt.io.parser.impl.AptPNParser;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.filechooser.OpenFileChooser;

public class ApplicationImpl implements Application {

	private final MainWindowPresenter mainWindow;
	private final EventBus eventBus;
	private final History history;

	private final Map<WindowId, Document> documents;

	private WindowId activeWindow;

	@Inject
	public ApplicationImpl(MainWindowPresenter mainWindow, EventBus eventBus, History history) {
		this.mainWindow = mainWindow;
		this.eventBus = eventBus;
		this.history = history;
		this.documents = new HashMap<>();

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

		openPetriNet(pn);
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
	public WindowId getActiveInternalWindow() {
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

		openTransitionSystem(ts);
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public History getHistory() {
		return history;
	}

	@Override
	public void openFile(File file) {
		// TODO correctly handle different types of APT-files
		AptPNParser parser = new AptPNParser();
		try {
			PetriNet pn = parser.parseFile(file);
			openPetriNet(pn);
		} catch (ParseException|IOException e) {
			JOptionPane.showMessageDialog((Component) mainWindow,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public MainWindowPresenter getMainWindow() {
		return mainWindow;
	}

	private void openTransitionSystem(TransitionSystem ts) {
		WindowId id = mainWindow.createWindow(ts);
		Document tsDoc = new TsDocument(ts);
		documents.put(id, tsDoc);
	}

	private void openPetriNet(PetriNet pn) {
		WindowId id = mainWindow.createWindow(pn);
		Document pnDoc = new PnDocument(pn);
		documents.put(id, pnDoc);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
