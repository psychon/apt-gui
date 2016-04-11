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

package uniol.aptgui.editor.layout;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.Node;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;
import uniol.apt.adt.ts.Arc;
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.editor.document.GraphicalArc;
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.GraphicalFlow;
import uniol.aptgui.editor.document.GraphicalNode;
import uniol.aptgui.editor.document.GraphicalPlace;
import uniol.aptgui.editor.document.GraphicalState;
import uniol.aptgui.editor.document.GraphicalTransition;

public class RandomLayout implements Layout {

	@Override
	public void applyTo(PetriNet pn, int width, int height) {
		Map<Node, GraphicalNode> nodeMap = new HashMap<>();
		for (Place p : pn.getPlaces()) {
			int x = randomInt(width);
			int y = randomInt(height);
			// TODO correct tokens
			GraphicalPlace elem = new GraphicalPlace();
			elem.setCenter(new Point(x, y));
			p.putExtension(GraphicalElement.EXTENSION_KEY, elem);
			nodeMap.put(p, elem);
		}
		for (Transition t : pn.getTransitions()) {
			int x = randomInt(width);
			int y = randomInt(height);
			GraphicalTransition elem = new GraphicalTransition();
			elem.setCenter(new Point(x, y));
			t.putExtension(GraphicalElement.EXTENSION_KEY, elem);
			nodeMap.put(t, elem);
		}
		for (Flow f : pn.getEdges()) {
			GraphicalNode source = nodeMap.get(f.getSource());
			GraphicalNode target = nodeMap.get(f.getTarget());
			// TODO correct multiplicity
			GraphicalFlow elem = new GraphicalFlow(source, target);
			f.putExtension(GraphicalElement.EXTENSION_KEY, elem);
		}
	}

	private static int randomInt(int max) {
		return (int) (Math.random() * (max + 1));
	}

	@Override
	public void applyTo(TransitionSystem ts, int width, int height) {
		Map<State, GraphicalState> stateMap = new HashMap<>();
		for (State s : ts.getNodes()) {
			int x = randomInt(width);
			int y = randomInt(height);
			GraphicalState elem = new GraphicalState();
			elem.setCenter(new Point(x, y));
			s.putExtension(GraphicalElement.EXTENSION_KEY, elem);
			stateMap.put(s, elem);
		}
		for (Arc a : ts.getEdges()) {
			GraphicalState source = stateMap.get(a.getSource());
			GraphicalState target = stateMap.get(a.getTarget());
			GraphicalArc elem = new GraphicalArc(source, target);
			a.putExtension(GraphicalElement.EXTENSION_KEY, elem);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
