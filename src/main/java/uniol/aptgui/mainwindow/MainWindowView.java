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

import uniol.aptgui.View;
import uniol.aptgui.internalwindow.InternalWindowView;
import uniol.aptgui.mainwindow.menu.MenuView;
import uniol.aptgui.mainwindow.toolbar.ToolbarView;

public interface MainWindowView extends View<MainWindowPresenter> {

	void setVisible(boolean visible);

	void close();

	void setTitle(String title);

	void addInternalWindow(InternalWindowView windowView);

	void removeInternalWindow(InternalWindowView windowView);

	void setToolbar(ToolbarView toolbarView);

	void setMenu(MenuView menuView);

	/**
	 * Arranges the internal windows in a cascade.
	 */
	void cascadeInternalWindows();

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
