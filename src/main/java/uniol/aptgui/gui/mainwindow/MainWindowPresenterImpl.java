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

package uniol.aptgui.gui.mainwindow;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.gui.AbstractPresenter;
import uniol.aptgui.gui.editor.PnEditorPresenter;
import uniol.aptgui.gui.editor.layout.RandomLayout;
import uniol.aptgui.gui.internalwindow.InternalWindowPresenter;

public class MainWindowPresenterImpl extends AbstractPresenter<MainWindowPresenter, MainWindowView>
		implements MainWindowPresenter {

	private final Injector injector;
	private final Map<WindowId, InternalWindowPresenter> internalWindows;

	@Inject
	public MainWindowPresenterImpl(MainWindowView view, Injector injector) {
		super(view);
		this.injector = injector;
		this.internalWindows = new HashMap<>();
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
	public WindowId createWindow(PetriNet pn) {
		PnEditorPresenter editor = injector.getInstance(PnEditorPresenter.class);
		editor.setPetriNet(pn);

		WindowId id = getUnusedWindowId();
		InternalWindowPresenter window = injector.getInstance(InternalWindowPresenter.class);
		window.setWindowId(id);
		window.setContentPresenter(editor);
		internalWindows.put(id, window);
		showWindow(id);

		editor.applyLayout(new RandomLayout());
		return id;
	}

	@Override
	public WindowId createWindow(TransitionSystem ts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeWindow(WindowId id) {
		Component component = internalWindows.remove(id).getGraphicalComponent();
		getView().removeInternalWindow(component);
	}

	@Override
	public void onCloseButtonClicked() {
		close();
	}

	private void showWindow(WindowId id) {
		Component component = internalWindows.get(id).getGraphicalComponent();
		getView().addInternalWindow(component);
	}

	private WindowId getUnusedWindowId() {
		return new WindowId();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
