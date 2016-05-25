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

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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
import uniol.aptgui.events.DocumentSelectionChangedEvent;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowOpenedEvent;
import uniol.aptgui.internalwindow.InternalWindowListener;
import uniol.aptgui.internalwindow.InternalWindowPresenter;
import uniol.aptgui.mainwindow.menu.MenuPresenter;
import uniol.aptgui.mainwindow.toolbar.ToolbarPresenter;
import uniol.aptgui.module.ModulePresenter;
import uniol.aptgui.modulebrowser.ModuleBrowserPresenter;

public class MainWindowPresenterImpl extends AbstractPresenter<MainWindowPresenter, MainWindowView>
		implements MainWindowPresenter {

	/**
	 * Main application title.
	 */
	private static final String TITLE = "APT";

	/**
	 * Reference to the global application instance.
	 */
	private final Application application;

	/**
	 * Reference to the global event bus instance.
	 */
	private final EventBus eventBus;

	/**
	 * Reference to the injector to create new InternalWindowPresenters.
	 */
	private final Injector injector;

	/**
	 * Map that contains all windows by id.
	 */
	private final Map<WindowId, InternalWindowPresenter> internalWindows;

	/**
	 * Toolbar presenter.
	 */
	@SuppressWarnings("unused")
	private final ToolbarPresenter toolbar;

	/**
	 * Main menu presenter.
	 */
	private final MenuPresenter menu;

	/**
	 * Module browser window id.
	 */
	private WindowId moduleBrowserWindowId;

	/**
	 * Listener for Document events. It updates the menu so that an
	 * up-to-date window list can be shown and also broadcasts selection
	 * change events so that actions can be enabled or disabled correctly.
	 */
	private final DocumentListener documentListener = new DocumentListener() {
		@Override public void onSelectionChanged(Document<?> source) {
			eventBus.post(new DocumentSelectionChangedEvent(source));
		}
		@Override public void onDocumentDirty() {}
		@Override
		public void onDocumentChanged() {
			updateWindowMenu();
		}
	};

	/**
	 * Listener for window events that updates the document size when the
	 * window size changes.
	 */
	private final InternalWindowListener windowListener = new InternalWindowListener() {
		@Override
		public void windowResized(WindowId id, int width, int height) {
			Document<?> document = application.getDocument(id);
			assert document != null;
			document.setWidth(width);
			document.setHeight(height);
		}
	};

	@Inject
	public MainWindowPresenterImpl(
		MainWindowView view,
		Application application,
		EventBus eventBus,
		Injector injector,
		ToolbarPresenter toolbar,
		MenuPresenter menu
	) {
		super(view);
		this.application = application;
		this.eventBus = eventBus;
		this.injector = injector;
		this.internalWindows = new HashMap<>();
		this.toolbar = toolbar;
		this.menu = menu;

		view.setToolbar(toolbar.getView());
		view.setMenu(menu.getView());
		view.setTitle(TITLE);

		eventBus.register(this);
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		view.setTitle(getWindowTitle(e.getWindowId()) + " - " + TITLE);
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		if (internalWindows.isEmpty()) {
			view.setTitle(TITLE);
		}
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
		view.removeInternalWindow(window.getView());
		updateWindowMenu();

		Document<?> document = application.getDocument(id);
		if (document != null) {
			document.removeListener(documentListener);
			window.removeWindowListener(windowListener);
		}

		eventBus.post(new WindowClosedEvent(id));
	}

	@Override
	public void onCloseButtonClicked() {
		close();
	}

	@Override
	public void showWindow(WindowId id) {
		InternalWindowPresenter window = internalWindows.get(id);
		view.addInternalWindow(window.getView());
		updateWindowMenu();

		eventBus.post(new WindowOpenedEvent(id));
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

		InternalWindowPresenter iwp = createInternalWindow(id, editor);
		iwp.addWindowListener(windowListener);

		document.addListener(documentListener);
	}

	@Override
	public void showModuleBrowser() {
		if (moduleBrowserWindowId == null) {
			moduleBrowserWindowId = new WindowId(WindowType.MODULE_BROWSER);
			ModuleBrowserPresenter moduleBrowser = injector.getInstance(ModuleBrowserPresenter.class);
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

	@Override
	public String getWindowTitle(WindowId id) {
		return internalWindows.get(id).getDisplayedTitle();
	}

	@Override
	public String showDocumentNameInputDialog(String title, String defaultValue) {
		return (String) JOptionPane.showInputDialog((Component) view, "Document name:", title,
				JOptionPane.QUESTION_MESSAGE, null, null, defaultValue);
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

	/**
	 * Updates the Menu presenter with the current set of windows.
	 */
	private void updateWindowMenu() {
		menu.setInternalWindows(internalWindows.keySet());
	}

	@Override
	public void cascadeWindows() {
		view.cascadeInternalWindows();
	}

	@Override
	public void showException(String title, Exception exception) {
		JOptionPane.showMessageDialog((Component) view, exception.getMessage(), title,
				JOptionPane.ERROR_MESSAGE);
		exception.printStackTrace();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
