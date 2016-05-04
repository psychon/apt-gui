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

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

import uniol.aptgui.editor.document.graphical.GraphicalElement;

import static java.lang.Math.*;

public abstract class GraphicalNode extends GraphicalElement {

	protected String id;
	protected Point center;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public void translate(int dx, int dy) {
		center.x += dx;
		center.y += dy;
	}

	@Override
	public boolean coversPoint(Point point) {
		return center != null;
	}

	@Override
	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}
		super.draw(graphics);

		drawShape(graphics);
		if (id != null) {
			drawId(graphics);
		}
		if (selected) {
			drawSelectionMarkers(graphics);
		}
	}

	protected abstract void drawShape(Graphics2D graphics);

	protected abstract void drawId(Graphics2D graphics);

	protected abstract void drawSelectionMarkers(Graphics2D graphics);

	/**
	 * Returns the intersection point of this GraphicalNode's boundary with
	 * a line from the given point to this GraphicalNode's center.
	 *
	 * @param point
	 * @return
	 */
	public abstract Point getBoundaryIntersection(Point point);

	protected static Point getCircleBoundaryIntersection(Point center, int radius, Point test) {
		double deltaCenterTestX = (center.x - test.x);
		double deltaCenterTestY = (center.y - test.y);
		double alpha = atan(deltaCenterTestY / deltaCenterTestX);
		double deltaX = cos(alpha) * radius;
		double deltaY = sin(alpha) * radius;

		if (deltaCenterTestX >= 0) {
			deltaX = -deltaX;
			deltaY = -deltaY;
		}

		int x = (int) (center.x + deltaX);
		int y = (int) (center.y + deltaY);
		return new Point(x, y);
	}

	protected static Point getSquareBoundaryIntersection(Point center, int radius, Point test) {
		// TODO: this is the same code as for the circle intersection, replace with correct method
		double deltaCenterTestX = (center.x - test.x);
		double deltaCenterTestY = (center.y - test.y);
		double alpha = atan(deltaCenterTestY / deltaCenterTestX);
		double deltaX = cos(alpha) * radius;
		double deltaY = sin(alpha) * radius;

		if (deltaCenterTestX > 0) {
			deltaX = -deltaX;
			deltaY = -deltaY;
		}

		int x = (int) (center.x + deltaX);
		int y = (int) (center.y + deltaY);
		return new Point(x, y);
	}

	protected static void drawCircle(Graphics2D graphics, Point center, int radius) {
		graphics.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
	}

	protected static void drawSquare(Graphics2D graphics, Point center, int radius) {
		graphics.drawRect(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
	}

	protected static void drawSelectionMarkers(Graphics2D graphics, Point center, int radius) {
		int len = 5;

		Point topLeft = new Point(center.x - radius, center.y - radius);
		Point topRight = new Point(center.x + radius, center.y - radius);
		Point bottomLeft = new Point(center.x - radius, center.y + radius);
		Point bottomRight = new Point(center.x + radius, center.y + radius);

		graphics.drawLine(topLeft.x, topLeft.y, topLeft.x + len, topLeft.y);
		graphics.drawLine(topLeft.x, topLeft.y, topLeft.x, topLeft.y + len);

		graphics.drawLine(topRight.x, topRight.y, topRight.x - len, topRight.y);
		graphics.drawLine(topRight.x, topRight.y, topRight.x, topRight.y + len);

		graphics.drawLine(bottomLeft.x, bottomLeft.y, bottomLeft.x + len, bottomLeft.y);
		graphics.drawLine(bottomLeft.x, bottomLeft.y, bottomLeft.x, bottomLeft.y - len);

		graphics.drawLine(bottomRight.x, bottomRight.y, bottomRight.x - len, bottomRight.y);
		graphics.drawLine(bottomRight.x, bottomRight.y, bottomRight.x, bottomRight.y - len);
	}

	protected static void drawCenteredString(Graphics2D graphics, Point center, String string) {
		FontMetrics metrics = graphics.getFontMetrics();
		int xOffset = metrics.stringWidth(string) / 2;
		int yOffset = metrics.getAscent() / 2;
		graphics.drawString(string, center.x - xOffset, center.y + yOffset);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
