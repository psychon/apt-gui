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

package uniol.aptgui.gui.editor.layout;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.Node;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalElement;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalFlow;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalNode;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalPlace;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalTransition;

public class RandomLayout implements Layout {

	@Override
	public void applyTo(PetriNet pn, int width, int height) {
		Map<Node, GraphicalNode> nodeMap = new HashMap<>();
		for (Place p : pn.getPlaces()) {
			int x = randomInt(width);
			int y = randomInt(height);
			// TODO correct tokens
			GraphicalPlace elem = new GraphicalPlace(2);
			elem.setCenter(new Point(x, y));
			p.putExtension(GraphicalElement.EXTENSION_KEY, elem);
			nodeMap.put(p, elem);
		}
		for (Transition t : pn.getTransitions()) {
			int x = randomInt(width);
			int y = randomInt(height);
			GraphicalTransition elem = new GraphicalTransition(t.getId(), t.getLabel());
			elem.setCenter(new Point(x, y));
			t.putExtension(GraphicalElement.EXTENSION_KEY, elem);
			nodeMap.put(t, elem);
		}
		for (Flow f : pn.getEdges()) {
			GraphicalNode source = nodeMap.get(f.getSource());
			GraphicalNode target = nodeMap.get(f.getTarget());
			// TODO correct multiplicity
			GraphicalFlow elem = new GraphicalFlow(source, target, 1);
			f.putExtension(GraphicalElement.EXTENSION_KEY, elem);
		}
	}

	private static int randomInt(int max) {
		return (int) (Math.random() * (max + 1));
	}

	@Override
	public void applyTo(TransitionSystem ts, int width, int height) {
		// TODO Auto-generated method stub

	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
