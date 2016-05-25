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

package uniol.aptgui.internalwindow;

import uniol.aptgui.View;
import uniol.aptgui.mainwindow.WindowType;

public interface InternalWindowView extends View<InternalWindowPresenter> {

	/**
	 * Sets the content view that will be shown inside this window.
	 *
	 * @param view
	 *                content/child view
	 */
	void setContent(View<?> view);

	/**
	 * Sets the window title.
	 *
	 * @param title
	 *                new window title
	 */
	void setTitle(String title);

	/**
	 * Focuses and brings the window to the front of the containing desktop
	 * pane.
	 */
	void focus();

	/**
	 * Sets the amount of space around the content view.
	 *
	 * @param padding
	 *                padding in pixels
	 */
	void setPadding(int padding);

	/**
	 * Sets the icon of the internal window according to the window type.
	 *
	 * @param windowType
	 *                type of the window
	 */
	void setIcon(WindowType windowType);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
