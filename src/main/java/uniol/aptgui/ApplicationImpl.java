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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.io.parser.ParseException;
import uniol.apt.module.Module;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.RenderingOptions;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.layout.RandomLayout;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.io.AptDocumentRenderer;
import uniol.aptgui.io.AptParser;
import uniol.aptgui.io.DocumentRenderer;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.actions.SaveAction;

public class ApplicationImpl implements Application {

	private final MainWindowPresenter mainWindow;
	private final EventBus eventBus;
	private final History history;
	private final RenderingOptions renderingOptions;

	private final Map<WindowId, Document<?>> documents;

	private WindowId activeWindow;

	@Inject
	public ApplicationImpl(MainWindowPresenter mainWindow, EventBus eventBus, History history, RenderingOptions renderingOptions) {
		this.mainWindow = mainWindow;
		this.eventBus = eventBus;
		this.history = history;
		this.documents = new HashMap<>();
		this.renderingOptions = renderingOptions;

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
		String name = mainWindow.showDocumentNameInputDialog("New Petri Net", "unnamed");
		if (name != null) {
			document.setName(name);
			return openDocument(document);
		}
		return null;
	}

	@Override
	public boolean closeWindow(WindowId id) {
		if (id.getType().isEditorWindow()) {
			Document<?> doc = getDocument(id);
			if (doc.hasUnsavedChanges() && askSaveDocument(id, getDocument(id))) {
				return false;
			}
		}

		if (activeWindow == id) {
			activeWindow = null;
		}
		documents.remove(id);
		mainWindow.removeWindow(id);
		return true;
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
		String name = mainWindow.showDocumentNameInputDialog("New Transition System", "unnamed");
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
			mainWindow.showException("Open Error", e);
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
			document.setName("unnamed");
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
			document.fireDocumentChanged(false);
		} catch (Exception e) {
			mainWindow.showException("Save Error", e);
		}
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
		return Collections.unmodifiableSet(documents.keySet());
	}

	@Override
	public Collection<Document<?>> getDocuments() {
		return Collections.unmodifiableCollection(documents.values());
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

	/**
	 * Asks the user if he wants to save the given document and opens the
	 * save dialog if he does. Returns if the cancel button was clicked.
	 *
	 * @param id
	 *                window id of the document
	 * @param document
	 *                document in question
	 * @return true, if the user choose cancel
	 */
	private boolean askSaveDocument(WindowId id, Document<?> document) {
		Component parentComponent = (Component) getMainWindow().getView();
		int res = JOptionPane.showOptionDialog(parentComponent,
				"Do you want to save your changes to '" + getWindowTitle(id) + "'?",
				"Save changes?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
		if (res == JOptionPane.CANCEL_OPTION) {
			return true;
		}
		if (res == JOptionPane.YES_OPTION) {
			focusWindow(id);
			new SaveAction(this, eventBus).actionPerformed(null);
		}
		return false;
	}

	@Override
	public void close() {
		mainWindow.close();
	}

	@Override
	public void exportSvg(Document<?> document, File exportFile) {
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		org.w3c.dom.Document xmlDoc = domImpl.createDocument(svgNS, "svg", null);
		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(xmlDoc);
		svgGenerator.setFont(Font.decode("Arial"));
		// Draw document with SVG generator.
		document.draw(svgGenerator, renderingOptions);

		// Save to file.
		try {
			svgGenerator.stream(exportFile.getAbsolutePath());
		} catch (SVGGraphics2DIOException e) {
			mainWindow.showException("Export Error", e);
		}
	}

	@Override
	public void exportPng(Document<?> document, File exportFile) {
		BufferedImage bufferedImage = new BufferedImage(
				document.getWidth(), document.getHeight(),
				BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D imageGraphics = (Graphics2D) bufferedImage.getGraphics();
		imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		document.draw(imageGraphics, renderingOptions);
		try {
			ImageIO.write(bufferedImage, "png", exportFile);
		} catch (IOException e) {
			mainWindow.showException("Export Error", e);
		}
	}

	@Override
	public RenderingOptions getRenderingOptions() {
		return renderingOptions;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
