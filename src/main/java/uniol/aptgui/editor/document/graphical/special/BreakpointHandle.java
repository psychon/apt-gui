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

package uniol.aptgui.editor.document.graphical.special;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import uniol.aptgui.editor.document.RenderingOptions;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;

public class BreakpointHandle extends GraphicalNode {

	public BreakpointHandle() {
		setColor(Color.BLUE);
		setVisible(false);
	}

	@Override
	public Point getBoundaryIntersection(Point point) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean coversPoint(Point point) {
		return false;
	}

	@Override
	protected void drawShape(Graphics2D graphics, RenderingOptions renderingOptions) {
		drawSquare(graphics, center, 5);
	}

	@Override
	protected void drawId(Graphics2D graphics, RenderingOptions renderingOptions) {
	}

	@Override
	protected void drawSelectionMarkers(Graphics2D graphics, RenderingOptions renderingOptions) {
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
