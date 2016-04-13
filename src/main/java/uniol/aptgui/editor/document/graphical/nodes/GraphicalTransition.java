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

package uniol.aptgui.editor.document.graphical.nodes;

import java.awt.Graphics2D;
import java.awt.Point;

public class GraphicalTransition extends GraphicalNode {

	private static final int RADIUS = 20;

	protected String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Point getBoundaryIntersection(Point point) {
		return center; // TODO
	}

	@Override
	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}
		super.draw(graphics);
		drawSquare(graphics, center, 20);
	}

	@Override
	public boolean coversPoint(Point point) {
		int minX = center.x - RADIUS;
		int maxX = center.x + RADIUS;
		int minY = center.y - RADIUS;
		int maxY = center.y + RADIUS;
		return minX <= point.x && point.x <= maxX && minY <= point.y && point.y <= maxY;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
