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

package uniol.aptgui.application;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import uniol.aptgui.application.events.ToolboxEventRouter;
import uniol.aptgui.gui.editor.EditorView;
import uniol.aptgui.gui.editor.EditorViewImpl;
import uniol.aptgui.gui.editor.PnEditorPresenter;
import uniol.aptgui.gui.editor.PnEditorPresenterImpl;
import uniol.aptgui.gui.editor.TsEditorPresenter;
import uniol.aptgui.gui.editor.TsEditorPresenterImpl;
import uniol.aptgui.gui.internalwindow.InternalWindowPresenter;
import uniol.aptgui.gui.internalwindow.InternalWindowPresenterImpl;
import uniol.aptgui.gui.internalwindow.InternalWindowView;
import uniol.aptgui.gui.internalwindow.InternalWindowViewImpl;
import uniol.aptgui.gui.mainwindow.MainWindowPresenter;
import uniol.aptgui.gui.mainwindow.MainWindowPresenterImpl;
import uniol.aptgui.gui.mainwindow.MainWindowView;
import uniol.aptgui.gui.mainwindow.MainWindowViewImpl;
import uniol.aptgui.gui.mainwindow.toolbar.ToolbarPresenter;
import uniol.aptgui.gui.mainwindow.toolbar.ToolbarPresenterImpl;
import uniol.aptgui.gui.mainwindow.toolbar.ToolbarView;
import uniol.aptgui.gui.mainwindow.toolbar.ToolbarViewImpl;

public class DependenyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Application.class).to(ApplicationImpl.class).in(Singleton.class);
		bind(MainWindowPresenter.class).to(MainWindowPresenterImpl.class);
		bind(MainWindowView.class).to(MainWindowViewImpl.class);
		bind(InternalWindowPresenter.class).to(InternalWindowPresenterImpl.class);
		bind(InternalWindowView.class).to(InternalWindowViewImpl.class);
		bind(PnEditorPresenter.class).to(PnEditorPresenterImpl.class);
		bind(TsEditorPresenter.class).to(TsEditorPresenterImpl.class);
		bind(EditorView.class).to(EditorViewImpl.class);
		bind(ToolbarPresenter.class).to(ToolbarPresenterImpl.class);
		bind(ToolbarView.class).to(ToolbarViewImpl.class);
		bind(ToolboxEventRouter.class).in(Singleton.class);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
