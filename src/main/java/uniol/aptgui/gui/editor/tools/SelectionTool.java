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
import java.awt.event.MouseWheelEvent;

import uniol.aptgui.gui.editor.EditorView;
import uniol.aptgui.gui.editor.graphicalelements.Document;

/**
 * Selection tool. Gives the user the ability to translate and scale the view,
 * highlight elements by hovering over them, select elements, move them and
 * modify edge paths.
 */
public class SelectionTool extends Tool {

	private static final double SCALE_FACTOR = 1.1;

	private final Document document;

	private boolean dragging;
	private Point dragSource;

	public SelectionTool(Document document, EditorView view) {
		super(view);
		this.document = document;
		this.dragging = false;
		this.dragSource = null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && !dragging) {
			dragging = true;
			dragSource = e.getPoint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			Point dragTarget = e.getPoint();
			translateView(dragTarget.x - dragSource.x, dragTarget.y - dragSource.y);
			dragSource = dragTarget;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragging) {
			dragging = false;
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

	private void translateView(int dx, int dy) {
		document.translateView(dx, dy);
		view.repaint();
	}

	private void scaleView(double scale) {
		document.scaleView(scale);
		view.repaint();
	}

	@Override
	public void draw(Graphics2D graphics) {
		// no need for drawing
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
