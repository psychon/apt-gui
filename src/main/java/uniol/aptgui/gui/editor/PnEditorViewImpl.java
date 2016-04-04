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

package uniol.aptgui.gui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;

import uniol.aptgui.gui.JPanelView;

@SuppressWarnings("serial")
public class PnEditorViewImpl extends JPanelView<PnEditorPresenter> implements PnEditorView {

	public PnEditorViewImpl() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(400, 300));
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
	public void addMouseAdapter(MouseAdapter mouseAdapter) {
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
	}
}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
