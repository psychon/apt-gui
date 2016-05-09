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

import java.io.File;
import java.util.Set;

import com.google.common.eventbus.EventBus;

import uniol.apt.module.Module;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;

public interface Application {

	public EventBus getEventBus();

	public History getHistory();

	public MainWindowPresenter getMainWindow();

	/**
	 * Shows the main window.
	 */
	public void show();

	/**
	 * Creates a new, empty Petri net editor window.
	 *
	 * @return the window id or null if the process was cancelled
	 */
	public WindowId newPetriNet();

	/**
	 * Creates a new, empty transition system editor window.
	 *
	 * @return the window id or null if the process was cancelled
	 */
	public WindowId newTransitionSystem();

	/**
	 * Closes the window with the given id.
	 *
	 * @param id
	 *                window id
	 */
	public void closeWindow(WindowId id);

	/**
	 * Focuses and brings the window with the given id to the front,
	 *
	 * @param id
	 *                window id
	 */
	public void focusWindow(WindowId id);

	/**
	 * Returns the title of the window with the given id.
	 *
	 * @param id
	 *                id of the window
	 * @return title of the window
	 */
	public String getWindowTitle(WindowId id);

	/**
	 * Returns the window id of the window that currently has the focus.
	 *
	 * @return window id or null if no window is active
	 */
	public WindowId getActiveInternalWindow();

	public Set<WindowId> getDocumentWindows();

	public Document<?> getDocument(WindowId id);

	/**
	 * Returns the focused editor's document object or null if no editor is
	 * focused.
	 *
	 * @return
	 */
	public Document<?> getActiveDocument();

	public WindowId openFile(File file);

	public void saveToFile(Document<?> document);

	public void openModule(Module module);

	public void openModuleBrowser();

	public WindowId openDocument(Document<?> document);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
