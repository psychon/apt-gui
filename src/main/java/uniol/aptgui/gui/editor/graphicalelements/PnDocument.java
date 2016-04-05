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

import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;
import uniol.aptgui.gui.editor.layout.Layout;

public class PnDocument extends Document {

	private final PetriNet petriNet;

	public PnDocument(PetriNet petriNet) {
		this.petriNet = petriNet;
	}

	public PetriNet getPetriNet() {
		return petriNet;
	}

	@Override
	public void draw(Graphics2D graphics) {
		for (Place place : petriNet.getPlaces()) {
			GraphicalPlace elem = getGraphicalExtension(place);
			elem.setTokens(place.getInitialToken().getValue());
			elem.draw(graphics);
		}
		for (Transition transition : petriNet.getTransitions()) {
			GraphicalTransition elem = getGraphicalExtension(transition);
			elem.setId(transition.getId());
			elem.setLabel(transition.getLabel());
			elem.draw(graphics);
		}
		for (Flow flow : petriNet.getEdges()) {
			GraphicalFlow elem = getGraphicalExtension(flow);
			GraphicalNode source = getGraphicalExtension(flow.getSource());
			GraphicalNode target = getGraphicalExtension(flow.getTarget());
			elem.setSource(source);
			elem.setTarget(target);
			elem.setMultiplicity(10); // TODO
			elem.draw(graphics);
		}
	}

	@Override
	public void applyLayout(Layout layout) {
		layout.applyTo(petriNet, width, height);
	}

	@Override
	public GraphicalElement getElementAt(Point point) {
		for (Place place : petriNet.getPlaces()) {
			GraphicalPlace elem = getGraphicalExtension(place);
			if (elem.containsPoint(point)) {
				return elem;
			}
		}
		for (Transition transition : petriNet.getTransitions()) {
			GraphicalTransition elem = getGraphicalExtension(transition);
			if (elem.containsPoint(point)) {
				return elem;
			}
		}
		for (Flow flow : petriNet.getEdges()) {
			GraphicalFlow elem = getGraphicalExtension(flow);
			if (elem.containsPoint(point)) {
				return elem;
			}
		}
		return null;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
