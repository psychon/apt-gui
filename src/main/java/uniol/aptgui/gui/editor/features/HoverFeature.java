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

package uniol.aptgui.gui.editor.features;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.gui.editor.EditorView;
import uniol.aptgui.gui.editor.graphicalelements.BreakpointHandle;
import uniol.aptgui.gui.editor.graphicalelements.Document;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalEdge;
import uniol.aptgui.gui.editor.graphicalelements.GraphicalElement;

public class HoverFeature extends Feature {

	private final Document document;
	private GraphicalElement hoverElem;
	private BreakpointHandle breakpointHandle;

	public HoverFeature(Document document, EditorView view) {
		super(view);
		this.document = document;
		this.hoverElem = null;
		this.breakpointHandle = new BreakpointHandle();
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
		Point modelPosition = document.transformViewToModel(cursor);
		GraphicalElement elem = document.getElementAt(modelPosition);

		// Display breakpoint handle if necessary.
		if (elem instanceof GraphicalEdge) {
			GraphicalEdge edge = (GraphicalEdge) elem;
			Point breakpoint = edge.getClosestBreakpoint(modelPosition);
			if (breakpoint != null) {
				// Transform back to view position since the Feature draw method doesn't take the Document transform into account.
				Point viewPosition = document.transformModelToView(breakpoint);
				breakpointHandle.setCenter(viewPosition);
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

		view.repaint();
	}

	@Override
	public void draw(Graphics2D graphics) {
		breakpointHandle.draw(graphics);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
