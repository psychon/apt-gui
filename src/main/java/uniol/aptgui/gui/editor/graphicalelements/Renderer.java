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
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import uniol.apt.adt.extension.IExtensible;
import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;

public class Renderer {

	private Graphics2D renderTarget;

	public void setRenderTarget(Graphics2D renderTarget) {
		this.renderTarget = renderTarget;
		this.renderTarget.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
	}

	public void render(PetriNet pn) {
		for (Place place : pn.getPlaces()) {
			GraphicalPlace elem = getGraphicalExtension(place);
			elem.setTokens(place.getInitialToken().getValue());
			drawPlace(elem);
		}
		for (Transition transition : pn.getTransitions()) {
			GraphicalTransition elem = getGraphicalExtension(transition);
			elem.setId(transition.getId());
			elem.setLabel(transition.getLabel());
			drawTransition(elem);
		}
		for (Flow flow : pn.getEdges()) {
			GraphicalFlow elem = getGraphicalExtension(flow);
			GraphicalNode source = getGraphicalExtension(flow.getSource());
			GraphicalNode target = getGraphicalExtension(flow.getTarget());
			elem.setSource(source);
			elem.setTarget(target);
			elem.setMultiplicity(10); // TODO
			drawFlow(elem);
		}
	}

	private void drawFlow(GraphicalFlow elem) {
		List<Point> path = new ArrayList<>();

		Point first, last;
		if (elem.getPath().isEmpty()) {
			first = elem.getTarget().getCenter();
			last = elem.getSource().getCenter();
		} else {
			first = path.get(0);
			last = path.get(path.size() - 1);
		}

		path.add(getNodeIntersection(elem.getSource(), first));
		path.addAll(elem.getPath());
		path.add(getNodeIntersection(elem.getTarget(), last));

		drawPathWithArrowhead(path);
	}

	private void drawPathWithArrowhead(List<Point> path) {
		assert path.size() >= 2;
		drawPath(path);
		drawArrowhead(
			path.get(path.size() - 2),
			path.get(path.size() - 1)
		);
	}

	private void drawTransition(GraphicalTransition elem) {
		drawSquare(elem.getCenter(), 20);
		// TODO draw labels
	}

	private void drawPlace(GraphicalPlace elem) {
		drawCircle(elem.getCenter(), 20);
		// TODO draw tokens
	}

	private void drawCircle(Point center, int radius) {
		renderTarget.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
	}

	private void drawSquare(Point center, int radius) {
		renderTarget.drawRect(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
	}

	/**
	 * Returns the intersection point of the GraphicalNode's boundary with a
	 * line from the given point to the GraphicalNode's center.
	 *
	 * @param point
	 * @return
	 */
	public Point getNodeIntersection(GraphicalNode node, Point point) {
		if (node instanceof GraphicalPlace) {
			return node.getCenter(); // TODO
		} else if (node instanceof GraphicalTransition) {
			return node.getCenter(); // TODO
		} else {
			throw new AssertionError("Node not yet handled.");
		}
	}

	private void drawPath(List<Point> path) {
		if (path.isEmpty()) {
			return;
		}
		Point previous = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			Point current = path.get(i);
			renderTarget.drawLine(previous.x, previous.y, current.x, current.y);
			previous = current;
		}
	}

	private void drawArrowhead(Point source, Point target) {
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
		renderTarget.setStroke(new BasicStroke(1f));
		renderTarget.drawPolygon(arrowhead);
		renderTarget.fillPolygon(arrowhead);
	}

	private static int yCor(int len, double dir) {
		return (int) (len * Math.cos(dir));
	}

	private static int xCor(int len, double dir) {
		return (int) (len * Math.sin(dir));
	}

	@SuppressWarnings("unchecked")
	private <T> T getGraphicalExtension(IExtensible obj) {
		return (T) obj.getExtension(GraphicalElement.EXTENSION_KEY);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
