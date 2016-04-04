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

public class GraphicalFlow extends GraphicalEdge {

	private GraphicalNode source;
	private GraphicalNode target;
	private long multiplicity;

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

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
