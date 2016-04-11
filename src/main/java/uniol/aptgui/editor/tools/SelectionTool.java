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
import java.awt.event.MouseWheelEvent;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.GraphicalEdge;
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.GraphicalNode;

/**
 * Selection tool. Gives the user the ability to translate and scale the view,
 * highlight elements by hovering over them, select elements, move them and
 * modify edge paths.
 */
public class SelectionTool extends Tool {

	private static enum DragType {
		NONE, VIEWPORT, NODE, EDGE
	}

	private static final double SCALE_FACTOR = 1.1;

	private final Document document;

	private DragType dragType;
	private Object draggedElement;
	private Point dragSource;

	public SelectionTool(Document document) {
		this.document = document;
		this.dragType = DragType.NONE;
		this.dragSource = null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			GraphicalElement elem = document.getGraphialElementAtViewCoordinates(e.getPoint());
			if (elem instanceof GraphicalNode) {
				dragType = DragType.NODE;
				draggedElement = elem;
			} else if (elem instanceof GraphicalEdge) {
				dragType = DragType.EDGE;
				draggedElement = getOrCreateBreakpoint(e.getPoint(), (GraphicalEdge) elem);
			} else {
				dragType = DragType.VIEWPORT;
				draggedElement = null;
			}
			dragSource = e.getPoint();
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
		case VIEWPORT:
			int dx = dragTarget.x - dragSource.x;
			int dy = dragTarget.y - dragSource.y;
			translateView(dx, dy);
			break;
		}

		dragSource = dragTarget;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragType == DragType.VIEWPORT) {
			dragType = DragType.NONE;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			scaleView(-1 / SCALE_FACTOR * e.getWheelRotation());
		} else {
			scaleView(SCALE_FACTOR * e.getWheelRotation());
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
		Point modelPoint = document.transformViewToModel(cursor);
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
		Point breakpoint = (Point) draggedElement;
		Point modelPoint = document.transformViewToModel(dragTarget);
		breakpoint.setLocation(modelPoint);
		document.fireDocumentDirty();
	}

	private void translateNode(Point dragTarget) {
		// TODO encapsulate in history command
		GraphicalNode node = (GraphicalNode) draggedElement;
		Point modelTarget = document.transformViewToModel(dragTarget);
		node.setCenter(modelTarget);
		document.fireDocumentDirty();
	}

	private void translateView(int dx, int dy) {
		document.translateView(dx, dy);
		document.fireDocumentDirty();
	}

	private void scaleView(double scale) {
		document.scaleView(scale);
		document.fireDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
