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

package uniol.aptgui.editor.tools.edge;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.Transform2D;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.editor.document.graphical.special.InvisibleNode;
import uniol.aptgui.editor.tools.Tool;

public abstract class CreateEdgeTool<T extends Document<?>, U extends GraphicalEdge> extends Tool {

	/**
	 * Document this tool operates on.
	 */
	protected final T document;

	/**
	 * Reference to the Document's transform object.
	 */
	protected final Transform2D transform;

	protected boolean creating;
	protected GraphicalNode graphicalSource;
	protected GraphicalNode graphicalTarget;
	protected U graphicalEdge;

	/**
	 * Saves element under cursor to apply hover effects.
	 */
	protected GraphicalElement hoverElement;

	public CreateEdgeTool(T document) {
		this.document = document;
		this.transform = document.getTransform();
		this.creating = false;
	}

	/**
	 * Creates a new instance of this tool's graphical edge type.
	 *
	 * @param source source node
	 * @param target target node
	 * @return
	 */
	protected abstract U createGraphicalEdge(GraphicalNode source, GraphicalNode target);

	/**
	 * Returns true if the edge being created can end in the given element.
	 * This may for example be dependent on the source and target type.
	 *
	 * @param target
	 *                the candidate target element
	 * @return true, if the element is a valid target for the edge
	 */
	protected abstract boolean isValidTarget(GraphicalElement target);

	/**
	 * Commits the creation of the given edge to the history.
	 *
	 * @param edge the edge to create
	 */
	protected abstract void commitEdgeCreation(U edge);

	@Override
	public void onDeactivated() {
		// Stop creation when the tool is deactivated.
		creating = false;

		// Hide GraphicalEdge.
		if (graphicalEdge != null) {
			document.remove(graphicalEdge);
			graphicalEdge = null;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			Point modelPosition = transform.applyInverse(e.getPoint());
			GraphicalElement elem = document.getGraphicalElementAt(modelPosition);
			System.out.println(elem);
			return;
		}
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		Point modelPosition = transform.applyInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);

		if (!creating && elem instanceof GraphicalNode) {
			// Clicked first node, therefore start creation.
			creating = true;
			graphicalSource = (GraphicalNode) elem;
			graphicalTarget = new InvisibleNode();
			graphicalTarget.setCenter(modelPosition);
			graphicalEdge = createGraphicalEdge(graphicalSource, graphicalTarget);
			document.add(graphicalEdge);
		}

		if (creating) {
			// Clicked second node. If possible, stop creation.
			if (elem != null && isValidTarget(elem)) {
				creating = false;
				graphicalTarget = (GraphicalNode) elem;
				graphicalEdge.setTarget(graphicalTarget);
				commitEdgeCreation(graphicalEdge);
				graphicalEdge = null;
				resetHoverEffects();
			}
			// Clicked anywhere else, therefore add breakpoint.
			if (elem == null) {
				graphicalEdge.addBreakpoint(modelPosition);
			}
		}

		document.fireDocumentDirty();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (creating) {
			Point modelPosition = transform.applyInverse(e.getPoint());
			// Move invisible target.
			graphicalTarget.setCenter(modelPosition);
			// Highlight elements based on validity.
			GraphicalElement elem = document.getGraphicalElementAt(modelPosition);
			setHoverEffects(elem);
			document.fireDocumentDirty();
		}
	}

	private void resetHoverEffects() {
		if (hoverElement != null) {
			hoverElement.setHighlightedSuccess(false);
			hoverElement.setHighlightedError(false);
		}
	}

	private void setHoverEffects(GraphicalElement currentHoverElem) {
		// Do nothing if the element didn't change.
		if (hoverElement == currentHoverElem) {
			return;
		}
		// Reset old hover effects.
		resetHoverEffects();
		// Update element.
		hoverElement = currentHoverElem;
		// Apply new hover effects.
		if (hoverElement != null) {
			if (isValidTarget(hoverElement)) {
				hoverElement.setHighlightedSuccess(true);
			} else {
				hoverElement.setHighlightedError(true);
			}

		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
