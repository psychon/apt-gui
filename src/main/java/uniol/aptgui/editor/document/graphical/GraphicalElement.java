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

package uniol.aptgui.editor.document.graphical;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class GraphicalElement {

	private static final Color HIGHLIGHT_COLOR = Color.BLUE;
	private static final Color HIGHLIGHT_COLOR_ERROR = Color.RED;
	private static final Color HIGHLIGHT_COLOR_SUCCESS = Color.GREEN;
	public static final String EXTENSION_KEY = "uniol.ape.gui.editor.graphicalelements.GraphicalElement";

	protected Color color;
	protected boolean highlighted;
	protected boolean highlightedError;
	protected boolean highlightedSuccess;
	protected boolean visible;

	public GraphicalElement() {
		this.color = Color.BLACK;
		this.visible = true;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public boolean isHighlightedError() {
		return highlightedError;
	}

	public void setHighlightedError(boolean highlightedError) {
		this.highlightedError = highlightedError;
	}

	public boolean isHighlightedSuccess() {
		return highlightedSuccess;
	}

	public void setHighlightedSuccess(boolean highlightedSuccess) {
		this.highlightedSuccess = highlightedSuccess;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}

		if (highlightedError) {
			graphics.setColor(HIGHLIGHT_COLOR_ERROR);
		} else if (highlightedSuccess) {
			graphics.setColor(HIGHLIGHT_COLOR_SUCCESS);
		} else if (highlighted) {
			graphics.setColor(HIGHLIGHT_COLOR);
		} else {
			graphics.setColor(color);
		}
	}

	public abstract boolean coversPoint(Point point);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
