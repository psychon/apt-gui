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

package uniol.aptgui.swing.parametertable;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.mainwindow.WindowId;

public class WindowRef {

	private final WindowId windowId;

	private final Document<?> document;

	public WindowRef(WindowId windowId, Document<?> document) {
		this.windowId = windowId;
		this.document = document;
	}

	public WindowId getWindowId() {
		return windowId;
	}

	public Document<?> getDocument() {
		return document;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", document.getName(), windowId.toString());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
