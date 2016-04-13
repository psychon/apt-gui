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

package uniol.aptgui.editor.document.graphical.edges;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;

public abstract class GraphicalEdge extends GraphicalElement {

	private static final double SELECTION_DISTANCE = 10;
	protected GraphicalNode source;
	protected GraphicalNode target;
	protected List<Point> breakpoints;
	protected String label;

	public GraphicalEdge(GraphicalNode source, GraphicalNode target) {
		assert source != null;
		assert target != null;
		this.source = source;
		this.target = target;
		this.breakpoints = new ArrayList<>();
	}

	public GraphicalNode getSource() {
		return source;
	}

	public void setSource(GraphicalNode source) {
		this.source = source;
	}

	public GraphicalNode getTarget() {
		return target;
	}

	public void setTarget(GraphicalNode target) {
		this.target = target;
	}

	public List<Point> getBreakpoints() {
		return breakpoints;
	}

	public void setBreakpoints(List<Point> breakpoints) {
		this.breakpoints = breakpoints;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}
		super.draw(graphics);
		drawPathWithArrowhead(graphics, getPath());
	}

	@Override
	public boolean coversPoint(Point point) {
		return getSegmentIndexAt(point) != -1;
	}

	/**
	 * Returns the closest breakpoint to the given point that is within the
	 * selection distance. Attention: A reference is returned, so any
	 * modifications will be mirrored by the GraphicalEdge.
	 *
	 * @param point
	 *                test position
	 * @return the closest breakpoint or null if no breakpoint is close
	 *         enough
	 */
	public Point getClosestBreakpoint(Point point) {
		for (Point p : breakpoints) {
			if (p.distance(point.x, point.y) < SELECTION_DISTANCE) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Add the given breakpoint as the last breakpoint.
	 *
	 * @param breakpoint
	 *                the new breakpoint in model coordinates
	 */
	public void addBreakpoint(Point breakpoint) {
		breakpoints.add(breakpoint);
	}

	/**
	 * Add the given breakpoint to the closest path segment or don't add it
	 * at all if there is no segment close enough.
	 *
	 * @param breakpoint
	 *                the new breakpoint
	 */
	public void addBreakpointToClosestSegment(Point breakpoint) {
		int pathIndex = getSegmentIndexAt(breakpoint);
		if (pathIndex != -1) {
			// SourceNode position is also part of the path, so
			// subtract 1.
			int insertionIndex = pathIndex - 1;
			breakpoints.add(insertionIndex, breakpoint);
		}
	}

	/**
	 * Returns a zero-based index that identifies the segment closest to the
	 * given point. The segment ends at path[segmentId].
	 *
	 * @param point
	 * @return the segment index or -1 if the point is not near a segment
	 */
	protected int getSegmentIndexAt(Point point) {
		List<Point> path = getPath();
		Point start = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			Point end = path.get(i);
			Line2D line = new Line2D.Float(start.x, start.y, end.x, end.y);
			if (line.ptSegDist(point.x, point.y) < SELECTION_DISTANCE) {
				return i;
			}
			start = end;
		}
		return -1;
	}

	protected List<Point> getPath() {
		List<Point> path = new ArrayList<>();

		// Find first and last breakpoints or valid substitutes for boundary intersection computations.
		Point first, last;
		if (breakpoints.isEmpty()) {
			first = target.getCenter();
			last = source.getCenter();
		} else {
			first = breakpoints.get(0);
			last = breakpoints.get(breakpoints.size() - 1);
		}

		// Collect path Points.
		path.add(source.getBoundaryIntersection(first));
		path.addAll(breakpoints);
		path.add(target.getBoundaryIntersection(last));

		assert path.size() >= 2;
		return path;
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
