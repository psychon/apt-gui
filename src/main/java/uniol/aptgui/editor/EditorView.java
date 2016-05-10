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

import java.awt.Cursor;
import java.awt.event.MouseAdapter;

import uniol.aptgui.View;

public interface EditorView extends View<EditorPresenter> {

	public int getCanvasWidth();

	public int getCanvasHeight();

	public void setCursor(Cursor cursor);

	public void addMouseEventListener(MouseAdapter mouseEventListener);

	public void repaint();

	public void showPopupMenu(int x, int y);

	/**
	 * Shows a dialog box that prompts the user to input a label for a new
	 * arc.
	 *
	 * @return the label or null if the process was cancelled
	 */
	public String showArcLabelInputDialog();

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
