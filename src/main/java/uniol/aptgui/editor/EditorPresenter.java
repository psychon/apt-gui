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

package uniol.aptgui.editor;

import java.awt.Graphics2D;

import uniol.aptgui.Presenter;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.layout.Layout;
import uniol.aptgui.mainwindow.WindowId;

public interface EditorPresenter extends Presenter<EditorView> {

	/// ACTIONS ///

	public void setWindowId(WindowId window);

	public void setDocument(Document document);

	public void applyLayout(Layout layout);

	/**
	 * Translates the view by dx on the x-axis and dy on the y-axis. Scale
	 * is always applied after any translation.
	 *
	 * @param dx
	 *                x translation
	 * @param dy
	 *                y translation
	 */
	public void translateView(int dx, int dy);

	/**
	 * Scales the view. Scale is always applied after any translation.
	 *
	 * @param scale
	 *                scaling factor
	 */
	public void scaleView(double scale);

	/// VIEW EVENTS ///

	public void onPaint(Graphics2D graphics);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120