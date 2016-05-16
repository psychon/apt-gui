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

import uniol.aptgui.commands.History;
import uniol.aptgui.commands.TranslateElementsCommand;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.Transform2D;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.features.base.Feature;

/**
 * The selection tool gives the user the ability to select elements, move them
 * and modify edge paths.
 */
public class SelectionTool extends Feature {

	private static enum DragType {
		NONE, SELECTION, BREAKPOINT
	}

	/**
	 * History reference.
	 */
	private final History history;

	/**
	 * Document this tool operates on.
	 */
	private final Document<?> document;

	/**
	 * Reference to the Document's transform object.
	 */
	private final Transform2D transform;

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
	 * Command object that will be submitted to the history when the
	 * translation process finishes.
	 */
	private TranslateElementsCommand translateCommand;

	/**
	 * Creates a new SelectionTool that operates on the given document.
	 *
	 * @param document
	 */
	public SelectionTool(Document<?> document, History history) {
		this.document = document;
		this.transform = document.getTransform();
		this.history = history;
		this.dragType = DragType.NONE;
		this.dragSource = null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Only listen for LMB clicks.
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		dragSource = e.getPoint();

		Point modelPosition = transform.applyInverse(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPosition);

		if (elem != null) {
			if (elem instanceof GraphicalEdge) {
				GraphicalEdge edge = (GraphicalEdge) elem;
				Point bp = edge.getClosestBreakpoint(modelPosition);
				// If the click hit a breakpoint, drag it.
				if (bp != null) {
					draggedElement = bp;
					dragType = DragType.BREAKPOINT;
					return;
				}
			}

			if (e.isControlDown()) {
				// If CTRL was pressed, add to/remove from
				// selection.
				document.toggleSelection(elem);
			} else if (!document.getSelection().contains(elem)) {
				// If the clicked element did not belong to the
				// selection, replace the selection.
				document.clearSelection();
				document.addToSelection(elem);
			}

			document.fireSelectionChanged();
			document.fireDocumentDirty();

			// Set drag type to selection.
			dragType = DragType.SELECTION;
			translateCommand = new TranslateElementsCommand(document, document.getSelection());
		} else {
			dragType = DragType.NONE;
			document.clearSelection();
			document.fireSelectionChanged();
			document.fireDocumentDirty();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragType == DragType.SELECTION && !translateCommand.isIdentity()) {
			translateCommand.unapplyTranslation();
			history.execute(translateCommand);
		}
		dragType = DragType.NONE;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point dragTarget = e.getPoint();

		switch (dragType) {
		case BREAKPOINT:
			translateBreakpoint(dragTarget);
			break;
		case SELECTION:
			int dx = (int) ((dragTarget.x - dragSource.x) / transform.getScale());
			int dy = (int) ((dragTarget.y - dragSource.y) / transform.getScale());
			translateCommand.unapplyTranslation();
			translateCommand.translate(dx, dy);
			translateCommand.applyTranslation();
			break;
		case NONE:
			break;
		}

		dragSource = dragTarget;
	}

	private void translateBreakpoint(Point dragTarget) {
		// TODO encapsulate in history command
		Point breakpoint = (Point) draggedElement;
		Point modelPoint = transform.applyInverse(dragTarget);
		breakpoint.setLocation(modelPoint);
		document.fireDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
