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

package uniol.aptgui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import uniol.aptgui.swing.JPanelView;

@SuppressWarnings("serial")
public class EditorViewImpl extends JPanelView<EditorPresenter>
		implements EditorView, MouseListener, MouseMotionListener, MouseWheelListener {

	private MouseEventListener mouseEventListener;

	public EditorViewImpl() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(400, 300));
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		getPresenter().onPaint((Graphics2D) g);
	}

	@Override
	public int getCanvasWidth() {
		return getWidth();
	}

	@Override
	public int getCanvasHeight() {
		return getHeight();
	}

	@Override
	public void setMouseEventListener(MouseEventListener mouseEventListener) {
		this.mouseEventListener = mouseEventListener;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (mouseEventListener != null) {
			mouseEventListener.mouseWheelMoved(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseEventListener != null) {
			mouseEventListener.mouseDragged(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mouseEventListener != null) {
			mouseEventListener.mouseMoved(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (mouseEventListener != null) {
			mouseEventListener.mouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (mouseEventListener != null) {
			mouseEventListener.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (mouseEventListener != null) {
			mouseEventListener.mouseReleased(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
