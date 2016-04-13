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

package uniol.aptgui.editor.features;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.Transform2D;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.special.BreakpointHandle;
import uniol.aptgui.editor.tools.Tool;

public class HoverFeature extends Tool {

	/**
	 * Document reference the HoverFeature operates on.
	 */
	private final Document<?> document;

	/**
	 * Reference to the Document's transform object.
	 */
	private final Transform2D transform;

	/**
	 * Graphical representation for edge corners.
	 */
	private final BreakpointHandle breakpointHandle;

	/**
	 * Currently highlighted element.
	 */
	private GraphicalElement hoverElem;

	public HoverFeature(Document<?> document) {
		this.document = document;
		this.transform = document.getTransform();
		this.breakpointHandle = new BreakpointHandle();
		this.hoverElem = null;
	}

	@Override
	public void onActivated() {
		document.add(breakpointHandle);
	}

	@Override
	public void onDeactivated() {
		document.remove(breakpointHandle);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setHoverEffects(e.getPoint());
	}

	/**
	 * Sets highlights and displays handles for breakpoints.
	 *
	 * @param cursor
	 *                current mouse position
	 */
	private void setHoverEffects(Point cursor) {
		Point modelPosition = transform.applyInverse(cursor);
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);

		// Display breakpoint handle if necessary.
		if (elem instanceof GraphicalEdge) {
			GraphicalEdge edge = (GraphicalEdge) elem;
			Point breakpoint = edge.getClosestBreakpoint(modelPosition);
			if (breakpoint != null) {
				breakpointHandle.setCenter(breakpoint);
				breakpointHandle.setVisible(true);
			}
		} else {
			breakpointHandle.setVisible(false);
		}

		// Reset previous highlight.
		if (hoverElem != null) {
			hoverElem.setHighlighted(false);
		}

		// Update highlighted element.
		hoverElem = elem;

		// Set new highlight.
		if (hoverElem != null) {
			hoverElem.setHighlighted(true);
		}

		document.fireDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
