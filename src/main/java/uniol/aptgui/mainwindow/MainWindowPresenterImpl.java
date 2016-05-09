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

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.apt.module.Module;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.Presenter;
import uniol.aptgui.editor.EditorPresenter;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.DocumentListener;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.internalwindow.InternalWindowPresenter;
import uniol.aptgui.mainwindow.menu.MenuPresenter;
import uniol.aptgui.mainwindow.toolbar.ToolbarPresenter;
import uniol.aptgui.module.ModulePresenter;
import uniol.aptgui.modulebrowser.ModuleBrowserPresenter;

public class MainWindowPresenterImpl extends AbstractPresenter<MainWindowPresenter, MainWindowView>
		implements MainWindowPresenter {

	private static final String TITLE = "APT";

	private final Injector injector;
	private final Map<WindowId, InternalWindowPresenter> internalWindows;
	private final ToolbarPresenter toolbar;
	private final MenuPresenter menu;
	private final ModuleBrowserPresenter moduleBrowser;
	private final DocumentListener titleChangeListener = new DocumentListener() {
		@Override public void onSelectionChanged(Class<? extends GraphicalElement> commonBaseClass) {}
		@Override public void onDocumentDirty() {}
		@Override
		public void onDocumentChanged() {
			menu.setInternalWindows(internalWindows.keySet());
		}
	};

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

	@Override
	public void removeWindow(WindowId id) {
		InternalWindowPresenter window = internalWindows.remove(id);
		getView().removeInternalWindow(window.getView());
		titleChangeListener.onDocumentDirty();

		Document<?> document = injector.getInstance(Application.class).getDocument(id);
		if (document != null) {
			document.removeListener(titleChangeListener);
		}
	}

	@Override
	public void onCloseButtonClicked() {
		close();
	}

	@Override
	public void showWindow(WindowId id) {
		InternalWindowPresenter window = internalWindows.get(id);
		getView().addInternalWindow(window.getView());
		titleChangeListener.onDocumentDirty();
	}

	@Override
	public void focus(WindowId id) {
		InternalWindowPresenter window = internalWindows.get(id);
		window.focus();
	}

	@Override
	public void createDocumentEditorWindow(WindowId id, Document<?> document) {
		EditorPresenter editor = injector.getInstance(EditorPresenter.class);
		editor.setDocument(document);
		editor.setWindowId(id);

		createInternalWindow(id, editor);

		document.addListener(titleChangeListener);
	}

	@Override
	public void showModuleBrowser() {
		if (moduleBrowserWindowId == null) {
			moduleBrowserWindowId = new WindowId(WindowType.MODULE_BROWSER);
			InternalWindowPresenter iwp = createInternalWindow(moduleBrowserWindowId, moduleBrowser);
			iwp.setTitle("Module Browser");
			showWindow(moduleBrowserWindowId);
		}

		focus(moduleBrowserWindowId);
	}

	@Override
	public void openModuleWindow(Module module) {
		ModulePresenter modulePresenter = injector.getInstance(ModulePresenter.class);
		modulePresenter.setModule(module);

		WindowId id = new WindowId(WindowType.MODULE);
		InternalWindowPresenter iwp = createInternalWindow(id, modulePresenter);
		iwp.setTitle(module.getTitle());

		showWindow(id);
		focus(id);
	}


	@Override
	public WindowId createDocumentWindowId(Document<?> document) {
		if (document instanceof PnDocument) {
			return new WindowId(WindowType.PETRI_NET);
		}
		if (document instanceof TsDocument) {
			return new WindowId(WindowType.TRANSITION_SYSTEM);
		}
		assert false;
		return null;
	}

	/**
	 * Creates a new InternalWindowPresenter with the given Presenter as its
	 * content presenter.
	 *
	 * @param id
	 *                the WindowId
	 * @param contentPresenter
	 *                the content/child presenter
	 * @return a new InternalWindowPresenter
	 */
	private InternalWindowPresenter createInternalWindow(WindowId id, Presenter<?> contentPresenter) {
		InternalWindowPresenter window = injector.getInstance(InternalWindowPresenter.class);
		window.setWindowId(id);
		window.setContentPresenter(contentPresenter);
		internalWindows.put(id, window);
		return window;
	}

	@Override
	public String getWindowTitle(WindowId id) {
		return internalWindows.get(id).getDisplayedTitle();
	}

	@Override
	public String showDocumentNameInputDialog(String title) {
		return (String) JOptionPane.showInputDialog((Component) view, "Document Name: ", title,
				JOptionPane.QUESTION_MESSAGE, null, null, "");
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
