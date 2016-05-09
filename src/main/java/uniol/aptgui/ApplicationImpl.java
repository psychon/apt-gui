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
import java.util.Set;

import javax.swing.JOptionPane;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.io.parser.ParseException;
import uniol.apt.io.renderer.RenderException;
import uniol.apt.module.Module;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.layout.RandomLayout;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.io.AptDocumentRenderer;
import uniol.aptgui.io.AptParser;
import uniol.aptgui.io.DocumentRenderer;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;

public class ApplicationImpl implements Application {

	private final MainWindowPresenter mainWindow;
	private final EventBus eventBus;
	private final History history;

	private final Map<WindowId, Document<?>> documents;

	private WindowId activeWindow;

	@Inject
	public ApplicationImpl(MainWindowPresenter mainWindow, EventBus eventBus, History history) {
		this.mainWindow = mainWindow;
		this.eventBus = eventBus;
		this.history = history;
		this.documents = new HashMap<>();

		eventBus.register(this);
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		activeWindow = e.getWindowId();
	}

	@Override
	public WindowId newPetriNet() {
		PetriNet pn = new PetriNet();
		Document<?> document = new PnDocument(pn);
		String name = mainWindow.showDocumentNameInputDialog("New Petri Net");
		if (name != null) {
			document.setName(name);
			return openDocument(document);
		}
		return null;
	}

	@Override
	public void closeWindow(WindowId id) {
		if (activeWindow == id) {
			activeWindow = null;
		}
		mainWindow.removeWindow(id);
		documents.remove(id);
		eventBus.post(new WindowClosedEvent(id));
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
	public WindowId newTransitionSystem() {
		TransitionSystem ts = new TransitionSystem();
		Document<?> document = new TsDocument(ts);
		String name = mainWindow.showDocumentNameInputDialog("New Transition System");
		if (name != null) {
			document.setName(name);
			return openDocument(document);
		}
		return null;
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
	public WindowId openFile(File file) {
		try {
			AptParser parser = new AptParser();
			return openDocument(parser.parse(file));
		} catch (ParseException|IOException e) {
			showException(e);
			return null;
		}
	}

	@Override
	public MainWindowPresenter getMainWindow() {
		return mainWindow;
	}

	@Override
	public WindowId openDocument(Document<?> document) {
		if (document.getName().isEmpty()) {
			document.setName("UNNAMED");
		}

		WindowId id = mainWindow.createDocumentWindowId(document);
		documents.put(id, document);

		mainWindow.createDocumentEditorWindow(id, document);
		mainWindow.showWindow(id);

		document.applyLayout(new RandomLayout());
		document.setVisible(true);

		mainWindow.focus(id);
		return id;
	}

	@Override
	public Document<?> getDocument(WindowId id) {
		return documents.get(id);
	}

	@Override
	public void saveToFile(Document<?> document) {
		assert document != null;
		assert document.getFile() != null;
		DocumentRenderer renderer = new AptDocumentRenderer();
		try {
			renderer.render(document, document.getFile());
		} catch (RenderException | IOException e) {
			showException(e);
		}
	}

	private void showException(Exception e) {
		JOptionPane.showMessageDialog((Component) mainWindow.getView(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}

	@Override
	public void openModule(Module module) {
		mainWindow.openModuleWindow(module);
	}

	@Override
	public void openModuleBrowser() {
		mainWindow.showModuleBrowser();
	}

	@Override
	public Set<WindowId> getDocumentWindows() {
		return documents.keySet();
	}

	@Override
	public Document<?> getActiveDocument() {
		return documents.get(activeWindow);
	}

	@Override
	public void focusWindow(WindowId id) {
		mainWindow.focus(id);
	}

	@Override
	public String getWindowTitle(WindowId id) {
		return mainWindow.getWindowTitle(id);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
