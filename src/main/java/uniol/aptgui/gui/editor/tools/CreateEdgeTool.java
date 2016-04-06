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

package uniol.aptgui.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.gui.editor.graphicalelements.Document;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalEdge;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalElement;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalNode;
import uniol.aptgui.gui.editor.graphicalelements.InvisibleNode;

public abstract class CreateEdgeTool extends BaseTool {

	protected final Document document;

	protected boolean creating;
	protected GraphicalNode source;
	protected GraphicalNode target;
	protected GraphicalEdge edge;

	/**
	 * Saves element under cursor to apply hover effects.
	 */
	protected GraphicalElement hoverElement;

	public CreateEdgeTool(Document document) {
		this.document = document;
		this.creating = false;
	}

	@Override
	public void onDeactivated() {
		creating = false;
		edge = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point modelPos = document.transformViewToModel(e.getPoint());
		GraphicalElement elem = document.getElementAt(modelPos);

		if (!creating && elem instanceof GraphicalNode) {
			// Clicked first node, therefore start creation.
			creating = true;
			source = (GraphicalNode) elem;
			target = new InvisibleNode();
			target.setCenter(modelPos);
			edge = createGraphicalEdge(source, target, modelPos);
		}

		if (creating) {
			// Clicked second node. If possible, stop creation.
			if (elem != null && isValidTarget(elem)) {
				creating = false;
				target = (GraphicalNode) elem;
				edge.setTarget(target);
				commitEdgeCreation(edge);
				edge = null;
				resetHoverEffects();
			}
			// Clicked anywhere else, therefore add breakpoint.
			if (elem == null) {
				edge.addBreakpoint(modelPos);
			}
		}

		document.fireDocumentDirty();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (creating) {
			Point modelPos = document.transformViewToModel(e.getPoint());
			// Move invisible target.
			target.setCenter(modelPos);
			// Highlight elements based on validity.
			GraphicalElement elem = document.getElementAt(modelPos);
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

	protected abstract GraphicalEdge createGraphicalEdge(GraphicalNode source, GraphicalNode target,
			Point modelTargetPosition);

	/**
	 * Returns true if the edge being created can end in the given element.
	 * This may for example be dependent on the source and target type.
	 *
	 * @param target the candidate target element
	 * @return true, if the element is a valid target for the edge
	 */
	protected abstract boolean isValidTarget(GraphicalElement target);

	protected abstract void commitEdgeCreation(GraphicalEdge edge);

	@Override
	public void draw(Graphics2D graphics) {
		if (edge != null) {
			edge.draw(graphics);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
