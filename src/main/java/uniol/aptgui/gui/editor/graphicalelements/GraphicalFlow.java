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

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GraphicalFlow extends GraphicalEdge {

	protected GraphicalNode source;
	protected GraphicalNode target;
	protected long multiplicity;

	public GraphicalFlow(GraphicalNode source, GraphicalNode target, long multiplicity) {
		this.source = source;
		this.target = target;
		this.multiplicity = multiplicity;
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

	public long getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(long multiplicity) {
		this.multiplicity = multiplicity;
	}

	@Override
	public void draw(Graphics2D graphics) {
		List<Point> drawPath = new ArrayList<>();

		Point first, last;
		if (path.isEmpty()) {
			first = target.getCenter();
			last = source.getCenter();
		} else {
			first = drawPath.get(0);
			last = drawPath.get(drawPath.size() - 1);
		}

		drawPath.add(source.getBoundaryIntersection(first));
		drawPath.addAll(path);
		drawPath.add(target.getBoundaryIntersection(last));

		drawPathWithArrowhead(graphics, drawPath);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
