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

package uniol.aptgui.mainwindow;

import uniol.apt.module.Module;
import uniol.aptgui.Presenter;
import uniol.aptgui.editor.document.Document;

public interface MainWindowPresenter extends Presenter<MainWindowView> {

	/// ACTIONS ///

	/**
	 * Shows the main window.
	 */
	void show();

	/**
	 * Closes the main window.
	 */
	void close();

	/**
	 * Creates a new WindowId for the given document.
	 *
	 * @param document
	 *                a PnDocument or a TsDocument
	 * @return a new WindowId
	 */
	WindowId createDocumentWindowId(Document<?> document);

	/**
	 * Creates a new internal window with a document editor as the content
	 * presenter.
	 *
	 * @param id
	 *                window id to use
	 * @param document
	 *                document to edit
	 */
	void createDocumentEditorWindow(WindowId id, Document<?> document);

	/**
	 * Adds the window identified by the given id to the desktop pane.
	 *
	 * @param id
	 *                window id
	 */
	void showWindow(WindowId id);

	/**
	 * Removes the window identified by the given id from the desktop pane.
	 *
	 * @param id
	 *                window id
	 */
	void removeWindow(WindowId id);

	/**
	 * Focuses and brings the window identified by the given id to the front
	 * of the desktop pane.
	 *
	 * @param id
	 *                window id
	 */
	void focus(WindowId id);

	/**
	 * Opens and brings the module browser window to front.
	 */
	void showModuleBrowser();

	/**
	 * Opens a window that allows to run the given module.
	 *
	 * @param module
	 *                module whose settings should be displayed in the
	 *                window
	 */
	void openModuleWindow(Module module);

	/**
	 * Returns the title of the window with the given id.
	 *
	 * @param id
	 *                window id
	 * @return window title
	 */
	String getWindowTitle(WindowId id);

	/**
	 * Shows a dialog that asks for the name of a new document.
	 *
	 * @param title
	 *                the dialog's title
	 * @return the user input or null if the dialog was cancelled
	 */
	String showDocumentNameInputDialog(String title);

	/**
	 * Cascades the internal windows.
	 */
	void cascadeWindows();

	/// VIEW EVENTS ///

	void onCloseButtonClicked();

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
