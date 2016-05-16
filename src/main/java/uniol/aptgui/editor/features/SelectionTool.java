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
import uniol.aptgui.commands.TranslateBreakpointCommand;
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
	 * Cursor position when a drag started.
	 */
	private Point dragSource;

	/**
	 * Command object that will be submitted to the history when an element
	 * translation finishes.
	 */
	private TranslateElementsCommand translateElementsCommand;

	/**
	 * Command object that will be submitted to the history when a
	 * breakpoint translation finishes.
	 */
	private TranslateBreakpointCommand translateBreakpointCommand;

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
				int breakpointIndex = edge.getClosestBreakpointIndex(modelPosition);
				// If the click hit a breakpoint, drag it.
				if (breakpointIndex != -1) {
					dragType = DragType.BREAKPOINT;
					translateBreakpointCommand = new TranslateBreakpointCommand(document, edge, breakpointIndex);
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

			document.setLastSelectionPosition(modelPosition);
			document.fireSelectionChanged();
			document.fireDocumentDirty();

			// Set drag type to selection.
			dragType = DragType.SELECTION;
			translateElementsCommand = new TranslateElementsCommand(document, document.getSelection());
		} else {
			dragType = DragType.NONE;
			document.clearSelection();
			document.fireSelectionChanged();
			document.fireDocumentDirty();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragType == DragType.BREAKPOINT && !translateBreakpointCommand.isIdentity()) {
			translateBreakpointCommand.unapplyTranslation();
			history.execute(translateBreakpointCommand);
		}
		if (dragType == DragType.SELECTION && !translateElementsCommand.isIdentity()) {
			translateElementsCommand.unapplyTranslation();
			history.execute(translateElementsCommand);
		}

		dragType = DragType.NONE;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point dragTarget = e.getPoint();
		int dx = (int) ((dragTarget.x - dragSource.x) / transform.getScale());
		int dy = (int) ((dragTarget.y - dragSource.y) / transform.getScale());

		switch (dragType) {
		case BREAKPOINT:
			translateBreakpointCommand.unapplyTranslation();
			translateBreakpointCommand.translate(dx, dy);
			translateBreakpointCommand.applyTranslation();
			break;
		case SELECTION:
			if (!GraphicalEdge.class.isAssignableFrom(document.getSelectionCommonBaseClass())) {
				// Only translate elements if the selection is not only edges.
				translateElementsCommand.unapplyTranslation();
				translateElementsCommand.translate(dx, dy);
				translateElementsCommand.applyTranslation();
			}
			break;
		default:
			break;
		}

		dragSource = dragTarget;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
