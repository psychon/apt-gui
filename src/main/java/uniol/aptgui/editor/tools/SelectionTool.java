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

package uniol.aptgui.editor.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.Transform2D;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;

/**
 * Selection tool. Gives the user the ability to translate and scale the view,
 * highlight elements by hovering over them, select elements, move them and
 * modify edge paths.
 */
public class SelectionTool extends Tool {

	private static enum DragType {
		NONE, NODE, EDGE
	}

	/**
	 * Document this tool operates on.
	 */
	private final Document<?> document;

	/**
	 * Reference to the Document's transform object.
	 */
	private final Transform2D transform;

	private DragType dragType;
	private Object draggedElement;

	public SelectionTool(Document<?> document) {
		this.document = document;
		this.transform = document.getTransform();
		this.dragType = DragType.NONE;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		// User clicked LMB: Select element under cursor.
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		// User pressed LMB: Either move view or element under cursor.
		Point modelPosition = transform.applyInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);
		if (elem instanceof GraphicalNode) {
			dragType = DragType.NODE;
			draggedElement = elem;
		} else if (elem instanceof GraphicalEdge) {
			dragType = DragType.EDGE;
			draggedElement = getOrCreateBreakpoint(e.getPoint(), (GraphicalEdge) elem);
		} else {
			dragType = DragType.NONE;
			draggedElement = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point dragTarget = e.getPoint();

		switch (dragType) {
		case EDGE:
			translateBreakpoint(dragTarget);
			break;
		case NODE:
			translateNode(dragTarget);
			break;
		case NONE:
			break;
		}
	}

	/**
	 * Checks if a breakpoint exists near the given cursor position on the
	 * given edge. If it exists, it is returned. Otherwise a new breakpoint
	 * is added at the cursor position and returned.
	 *
	 * @param cursor view coordinate cursor position
	 * @param edge edge at the cursor position
	 * @return new or existing breakpoint at cursor position
	 */
	private Point getOrCreateBreakpoint(Point cursor, GraphicalEdge edge) {
		Point modelPoint = transform.applyInverse(cursor);
		Point breakpoint = edge.getClosestBreakpoint(modelPoint);
		// Either move existing breakpoint or create a new one.
		if (breakpoint != null) {
			return breakpoint;
		} else {
			edge.addBreakpointToClosestSegment(modelPoint);
			return modelPoint;
		}
	}

	private void translateBreakpoint(Point dragTarget) {
		// TODO encapsulate in history command
		Point breakpoint = (Point) draggedElement;
		Point modelPoint = transform.applyInverse(dragTarget);
		breakpoint.setLocation(modelPoint);
		document.fireDocumentDirty();
	}

	private void translateNode(Point dragTarget) {
		// TODO encapsulate in history command
		GraphicalNode node = (GraphicalNode) draggedElement;
		Point modelTarget = transform.applyInverse(dragTarget);
		node.setCenter(modelTarget);
		document.fireDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
