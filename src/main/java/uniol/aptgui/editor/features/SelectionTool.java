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
import java.util.HashSet;
import java.util.Set;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.Transform2D;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.editor.features.base.Feature;

/**
 * Selection tool. Gives the user the ability to translate and scale the view,
 * highlight elements by hovering over them, select elements, move them and
 * modify edge paths.
 */
public class SelectionTool extends Feature {

	private static enum DragType {
		NONE, NODE, EDGE, SELECTION
	}

	/**
	 * Document this tool operates on.
	 */
	private final Document<?> document;

	/**
	 * Reference to the Document's transform object.
	 */
	private final Transform2D transform;

	/**
	 * A set of selected elements.
	 */
	private final Set<GraphicalNode> selection;

	/**
	 * Differentiates on what element the drag began.
	 */
	private DragType dragType;

	/**
	 * The element that is dragged around.
	 */
	private Object draggedElement;

	/**
	 * Cursor position when a drag started.
	 */
	private Point dragSource;

	/**
	 * Creates a new SelectionTool that operates on the given document.
	 *
	 * @param document
	 */
	public SelectionTool(Document<?> document) {
		this.document = document;
		this.transform = document.getTransform();
		this.dragType = DragType.NONE;
		this.dragSource = null;
		this.selection = new HashSet<>();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		// User clicked LMB: Select element under cursor.
		Point modelPosition = transform.applyInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);
		if (elem instanceof GraphicalNode) {
			GraphicalNode node = (GraphicalNode) elem;
			if (e.isControlDown()) {
				// If CTRL was pressed, add to/remove from
				// selection.
				toggleSelection(node);
			} else {
				// Otherwise replace selection.
				clearSelection();
				addToSelection(node);
			}
			document.fireDocumentDirty();
		}
	}

	/**
	 * Toggles selection status on the element. If it was previously
	 * unselected, it will be selected afterwards and the other way around.
	 *
	 * @param elem
	 */
	private void toggleSelection(GraphicalNode elem) {
		if (elem.isSelected()) {
			removeFromSelection(elem);
		} else {
			addToSelection(elem);
		}
	}

	/**
	 * Adds the given element to the selection.
	 *
	 * @param elem
	 *                newly selected element
	 */
	private void addToSelection(GraphicalNode elem) {
		selection.add(elem);
		elem.setSelected(true);
	}

	/**
	 * Removes the given element from the selection.
	 *
	 * @param elem
	 *                the element to unselect
	 */
	private void removeFromSelection(GraphicalNode elem) {
		selection.add(elem);
		elem.setSelected(true);
	}

	/**
	 * Clears the current selection.
	 */
	private void clearSelection() {
		for (GraphicalNode ge : selection) {
			ge.setSelected(false);
		}
		selection.clear();
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
			if (selection.isEmpty()) {
				dragType = DragType.NODE;
				draggedElement = elem;
			} else if (selection.contains(elem)) {
				dragType = DragType.SELECTION;
			} else {
				dragType = DragType.NONE;
				draggedElement = null;
			}
		} else if (elem instanceof GraphicalEdge) {
			dragType = DragType.EDGE;
			draggedElement = getOrCreateBreakpoint(e.getPoint(), (GraphicalEdge) elem);
			clearSelection();
		} else {
			dragType = DragType.NONE;
			draggedElement = null;
			clearSelection();
		}

		dragSource = e.getPoint();
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
		case SELECTION:
			int dx = (int) ((dragTarget.x - dragSource.x) / transform.getScale());
			int dy = (int) ((dragTarget.y - dragSource.y) / transform.getScale());
			translateSelection(dx, dy);
			break;
		case NONE:
			break;
		}

		dragSource = dragTarget;
	}

	private void translateSelection(int dx, int dy) {
		// TODO encapsulate in history command
		for (GraphicalNode node : selection) {
			node.translate(dx, dy);
		}
		document.fireDocumentDirty();
	}

	/**
	 * Checks if a breakpoint exists near the given cursor position on the
	 * given edge. If it exists, it is returned. Otherwise a new breakpoint
	 * is added at the cursor position and returned.
	 *
	 * @param cursor
	 *                view coordinate cursor position
	 * @param edge
	 *                edge at the cursor position
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
