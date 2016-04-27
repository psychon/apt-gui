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

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.module.Module;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.mainwindow.MainWindowPresenter;
import uniol.aptgui.mainwindow.WindowId;

public interface Application {

	public EventBus getEventBus();

	public History getHistory();

	public MainWindowPresenter getMainWindow();

	public void show();

	public WindowId newPetriNet();

	public WindowId newTransitionSystem();

	public void closeWindow(WindowId id);

	public WindowId getActiveInternalWindow();

	public Set<WindowId> getInteralWindows();

	public Document<?> getDocument(WindowId id);

	public WindowId openFile(File file);

	public void saveToFile(Document<?> document);

	public void openModule(Module module);

	public void openModuleBrowser();

	public WindowId openPetriNet(PetriNet pn);

	public WindowId openTransitionSystem(TransitionSystem ts);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
