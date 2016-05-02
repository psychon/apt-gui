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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.apt.module.Module;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Presenter;
import uniol.aptgui.editor.EditorPresenter;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.internalwindow.InternalWindowPresenter;
import uniol.aptgui.mainwindow.menu.MenuPresenter;
import uniol.aptgui.mainwindow.toolbar.ToolbarPresenter;
import uniol.aptgui.module.ModulePresenter;
import uniol.aptgui.modulebrowser.ModuleBrowserPresenter;

public class MainWindowPresenterImpl extends AbstractPresenter<MainWindowPresenter, MainWindowView>
		implements MainWindowPresenter {

	private static final String TITLE = "APT";

	private final Logger logger = Logger.getLogger(MainWindowPresenterImpl.class.getName());
	private final Injector injector;
	private final Map<WindowId, InternalWindowPresenter> internalWindows;
	private final ToolbarPresenter toolbar;
	private final MenuPresenter menu;
	private final ModuleBrowserPresenter moduleBrowser;

	private WindowId moduleBrowserWindowId;

	@Inject
	public MainWindowPresenterImpl(MainWindowView view, Injector injector, ToolbarPresenter toolbar,
			MenuPresenter menu, ModuleBrowserPresenter moduleBrowser) {
		super(view);
		this.injector = injector;
		this.internalWindows = new HashMap<>();
		this.toolbar = toolbar;
		this.menu = menu;
		this.moduleBrowser = moduleBrowser;

		view.setToolbar(toolbar.getView());
		view.setMenu(menu.getView());
		view.setTitle(TITLE);
	}

	@Override
	public void show() {
		getView().setVisible(true);
	}

	@Override
	public void close() {
		getView().close();
	}

	private InternalWindowPresenter createInternalWindow(WindowId id, Presenter<?> contentPresenter) {
		InternalWindowPresenter window = injector.getInstance(InternalWindowPresenter.class);
		window.setWindowId(id);
		window.setContentPresenter(contentPresenter);
		internalWindows.put(id, window);
		return window;
	}

	@Override
	public void removeWindow(WindowId id) {
		InternalWindowPresenter window = internalWindows.remove(id);
		getView().removeInternalWindow(window.getView());
		menu.setInternalWindows(internalWindows.keySet());
	}

	@Override
	public void onCloseButtonClicked() {
		close();
	}

	@Override
	public void showWindow(WindowId id) {
		InternalWindowPresenter window = internalWindows.get(id);
		getView().addInternalWindow(window.getView());
		menu.setInternalWindows(internalWindows.keySet());
	}

	@Override
	public void focus(WindowId id) {
		InternalWindowPresenter window = internalWindows.get(id);
		window.focus();
	}

	@Override
	public WindowId createWindow(Document<?> document) {
		WindowId id = null;
		EditorPresenter editor = injector.getInstance(EditorPresenter.class);
		editor.setDocument(document);

		if (document instanceof PnDocument) {
			id = new WindowId(WindowType.PETRI_NET);
		} else if (document instanceof TsDocument) {
			id = new WindowId(WindowType.TRANSITION_SYSTEM);
		} else {
			logger.log(Level.SEVERE, "Unknown document type.");
		}

		id.setTitle(document.getTitle());
		editor.setWindowId(id);
		createInternalWindow(id, editor);

		return id;
	}

	@Override
	public void showModuleBrowser() {
		if (moduleBrowserWindowId == null) {
			moduleBrowserWindowId = new WindowId(WindowType.MODULE_BROWSER);
			moduleBrowserWindowId.setTitle("Module Browser");
			createInternalWindow(moduleBrowserWindowId, moduleBrowser);
			showWindow(moduleBrowserWindowId);
		}

		focus(moduleBrowserWindowId);
	}

	@Override
	public void openModuleWindow(Module module) {
		ModulePresenter modulePresenter = injector.getInstance(ModulePresenter.class);
		modulePresenter.setModule(module);

		WindowId id = new WindowId(WindowType.MODULE);
		id.setTitle(module.getTitle());
		createInternalWindow(id, modulePresenter);

		showWindow(id);
		focus(id);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
