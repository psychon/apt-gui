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

package uniol.aptgui.mainwindow.menu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.apt.module.Module;
import uniol.aptgui.Application;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.JMenuBarView;
import uniol.aptgui.swing.actions.CascadeWindowsAction;
import uniol.aptgui.swing.actions.DeleteElementsAction;
import uniol.aptgui.swing.actions.ExitAction;
import uniol.aptgui.swing.actions.ExportAction;
import uniol.aptgui.swing.actions.ImportAction;
import uniol.aptgui.swing.actions.ModuleAction;
import uniol.aptgui.swing.actions.ModuleBrowserAction;
import uniol.aptgui.swing.actions.NewPetriNetAction;
import uniol.aptgui.swing.actions.NewTransitionSystemAction;
import uniol.aptgui.swing.actions.OpenAction;
import uniol.aptgui.swing.actions.RandomLayoutAction;
import uniol.aptgui.swing.actions.RedoAction;
import uniol.aptgui.swing.actions.RenameDocumentAction;
import uniol.aptgui.swing.actions.SaveAction;
import uniol.aptgui.swing.actions.SaveAllAction;
import uniol.aptgui.swing.actions.SaveAsAction;
import uniol.aptgui.swing.actions.SetColorAction;
import uniol.aptgui.swing.actions.SetInitialStateAction;
import uniol.aptgui.swing.actions.SetLabelAction;
import uniol.aptgui.swing.actions.SetTokensAction;
import uniol.aptgui.swing.actions.ShowWindowAction;
import uniol.aptgui.swing.actions.UndoAction;

@SuppressWarnings("serial")
public class MenuViewImpl extends JMenuBarView<MenuPresenter> implements MenuView {

	private final JMenu fileMenu;
	private final JMenuItem newPn;
	private final JMenuItem newTs;
	private final JMenuItem open;
	private final JMenuItem save;
	private final JMenuItem saveAs;
	private final JMenuItem saveAll;
	private final JMenuItem import_;
	private final JMenuItem export;
	private final JMenuItem exit;

	private final JMenu editMenu;
	private final JMenuItem undo;
	private final JMenuItem redo;
	private final JMenuItem setColor;
	private final JMenuItem setLabel;
	private final JMenuItem setTokens;
	private final JMenuItem setInitialState;
	private final JMenuItem delete;

	private final JMenu documentMenu;
	private final JMenuItem renameDocument;
	private final JMenuItem layoutRandom;

	private final JMenu moduleMenu;
	private final JMenuItem moduleBrowser;
	private final JMenuItem recentlyUsedHeader;
	private final List<JMenuItem> recentlyUsedModulesMenuItems;

	private final JMenu windowMenu;
	private final JMenuItem cascadeEditorWindows;
	private final JMenuItem openWindowsHeader;
	private final List<JMenuItem> openWindowsMenuItems;

	@Inject
	public MenuViewImpl(Injector injector) {
		fileMenu = new JMenu("File");
		newPn = new JMenuItem(injector.getInstance(NewPetriNetAction.class));
		newTs = new JMenuItem(injector.getInstance(NewTransitionSystemAction.class));
		open = new JMenuItem(injector.getInstance(OpenAction.class));
		save = new JMenuItem(injector.getInstance(SaveAction.class));
		saveAs = new JMenuItem(injector.getInstance(SaveAsAction.class));
		saveAll = new JMenuItem(injector.getInstance(SaveAllAction.class));
		import_ = new JMenuItem(injector.getInstance(ImportAction.class));
		export = new JMenuItem(injector.getInstance(ExportAction.class));
		exit = new JMenuItem(injector.getInstance(ExitAction.class));

		editMenu = new JMenu("Edit");
		undo = new JMenuItem(injector.getInstance(UndoAction.class));
		redo = new JMenuItem(injector.getInstance(RedoAction.class));
		setColor = new JMenuItem(injector.getInstance(SetColorAction.class));
		setLabel = new JMenuItem(injector.getInstance(SetLabelAction.class));
		setTokens = new JMenuItem(injector.getInstance(SetTokensAction.class));
		setInitialState = new JMenuItem(injector.getInstance(SetInitialStateAction.class));
		delete = new JMenuItem(injector.getInstance(DeleteElementsAction.class));

		documentMenu = new JMenu("Document");
		renameDocument = new JMenuItem(injector.getInstance(RenameDocumentAction.class));
		layoutRandom = new JMenuItem(injector.getInstance(RandomLayoutAction.class));

		moduleMenu = new JMenu("Modules");
		moduleBrowser = new JMenuItem(injector.getInstance(ModuleBrowserAction.class));
		recentlyUsedHeader = new JMenuItem("Recently Used");
		recentlyUsedHeader.setEnabled(false);
		recentlyUsedModulesMenuItems = new ArrayList<>();

		windowMenu = new JMenu("Windows");
		cascadeEditorWindows = new JMenuItem(injector.getInstance(CascadeWindowsAction.class));
		openWindowsHeader = new JMenuItem("Currently Opened Windows");
		openWindowsHeader.setEnabled(false);
		openWindowsMenuItems = new ArrayList<>();

		setupFileMenu();
		setupEditMenu();
		setupDocumentMenu();
		setupModuleMenu();
		setupWindowMenu();
	}

	private void setupDocumentMenu() {
		add(documentMenu);

		documentMenu.add(renameDocument);
		documentMenu.add(layoutRandom);
	}

	private void setupFileMenu() {
		add(fileMenu);

		fileMenu.add(newPn);
		fileMenu.add(newTs);
		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(saveAll);
		fileMenu.addSeparator();
		fileMenu.add(import_);
		fileMenu.add(export);
		fileMenu.addSeparator();
		fileMenu.add(exit);
	}

	private void setupEditMenu() {
		add(editMenu);

		editMenu.add(undo);
		editMenu.add(redo);
		editMenu.addSeparator();
		editMenu.add(setColor);
		editMenu.add(setLabel);
		editMenu.add(setTokens);
		editMenu.add(setInitialState);
		editMenu.add(delete);
	}

	private void setupModuleMenu() {
		add(moduleMenu);

		moduleMenu.add(moduleBrowser);
		moduleMenu.addSeparator();
		moduleMenu.add(recentlyUsedHeader);
	}

	private void setupWindowMenu() {
		add(windowMenu);

		windowMenu.add(cascadeEditorWindows);
		windowMenu.addSeparator();
		windowMenu.add(openWindowsHeader);
	}

	@Override
	public void setRecentlyUsedModule(Application app, List<Module> recentlyUsedModules) {
		// Remove old menu entries.
		for (JMenuItem item : recentlyUsedModulesMenuItems) {
			moduleMenu.remove(item);
		}
		recentlyUsedModulesMenuItems.clear();
		// Set new menu entries.
		for (Module module : recentlyUsedModules) {
			ModuleAction action = new ModuleAction(app, module);
			JMenuItem item = new JMenuItem(action);
			recentlyUsedModulesMenuItems.add(item);
			moduleMenu.add(item);
		}
	}

	@Override
	public void setWindows(Application app, List<WindowId> windows) {
		// Remove old menu entries.
		for (JMenuItem item : openWindowsMenuItems) {
			windowMenu.remove(item);
		}
		openWindowsMenuItems.clear();
		// Set new menu entries.
		for (WindowId windowId : windows) {
			ShowWindowAction action = new ShowWindowAction(app, windowId);
			JMenuItem item = new JMenuItem(action);
			openWindowsMenuItems.add(item);
			windowMenu.add(item);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
