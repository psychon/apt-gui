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

package uniol.aptgui.gui.editor.graphicalelements;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public abstract class GraphicalEdge extends GraphicalElement {

	protected List<Point> path;

	public GraphicalEdge() {
		this.path = new ArrayList<>();
	}

	public List<Point> getPath() {
		return path;
	}

	public void setPath(List<Point> path) {
		this.path = path;
	}

	protected static void drawPathWithArrowhead(Graphics2D graphics, List<Point> path) {
		assert path.size() >= 2;
		drawPath(graphics, path);
		drawArrowhead(
			graphics,
			path.get(path.size() - 2),
			path.get(path.size() - 1)
		);
	}

	protected static void drawPath(Graphics2D graphics, List<Point> path) {
		if (path.isEmpty()) {
			return;
		}
		Point previous = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			Point current = path.get(i);
			graphics.drawLine(previous.x, previous.y, current.x, current.y);
			previous = current;
		}
	}

	protected static void drawArrowhead(Graphics2D graphics, Point source, Point target) {
		int x = target.x;
		int y = target.y;
		int i1 = 12;
		int i2 = 6;
		double aDir = Math.atan2(source.x - target.x, source.y - target.y);

		Polygon arrowhead = new Polygon();
		arrowhead.addPoint(x, y);
		arrowhead.addPoint(x + xCor(i1, aDir + 0.5), y + yCor(i1, aDir + 0.5));
		arrowhead.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
		arrowhead.addPoint(x + xCor(i1, aDir - 0.5), y + yCor(i1, aDir - 0.5));
		arrowhead.addPoint(x, y);

		// make the arrowhead solid
		graphics.setStroke(new BasicStroke(1f));
		graphics.drawPolygon(arrowhead);
		graphics.fillPolygon(arrowhead);
	}

	private static int yCor(int len, double dir) {
		return (int) (len * Math.cos(dir));
	}

	private static int xCor(int len, double dir) {
		return (int) (len * Math.sin(dir));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
