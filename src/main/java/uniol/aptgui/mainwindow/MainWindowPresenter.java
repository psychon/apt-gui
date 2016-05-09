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

	void show();

	void close();

	/**
	 * Creates a new WindowId for the given document.
	 *
	 * @param document
	 *                a PnDocument or a TsDocument
	 * @return a new WindowId
	 */
	WindowId createDocumentWindowId(Document<?> document);

	void createDocumentEditorWindow(WindowId id, Document<?> document);

	void showWindow(WindowId id);

	void removeWindow(WindowId id);

	void focus(WindowId id);

	void showModuleBrowser();

	void openModuleWindow(Module module);

	/**
	 * Returns the title of the window with the given id.
	 *
	 * @param id
	 *                window id
	 * @return window title
	 */
	String getWindowTitle(WindowId id);

	/// VIEW EVENTS ///

	void onCloseButtonClicked();

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
