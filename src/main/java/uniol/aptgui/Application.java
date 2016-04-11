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

import com.google.common.eventbus.EventBus;

import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;

public interface Application {

	// ACTIONS

	public void show();

	public void newPetriNet();

	public void newTransitionSystem();

	public void closeWindow(WindowId id);

	public WindowId getActiveInternalWindow();

	public Document getDocument(WindowId id);

	public EventBus getEventBus();

	public History getHistory();

	public void openFile(File file);

	public MainWindowPresenter getMainWindow();

	// getActiveSelectedElement(s)

}


// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
