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
import java.awt.event.MouseAdapter;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.aptgui.swing.JPanelView;
import uniol.aptgui.swing.actions.SetLabelAction;

@SuppressWarnings("serial")
public class EditorViewImpl extends JPanelView<EditorPresenter> implements EditorView {

	private final Injector injector;
	private final JPopupMenu popupMenuForNodes;

	@Inject
	public EditorViewImpl(Injector injector) {
		this.injector = injector;
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(400, 300));
		popupMenuForNodes = createPopupMenuForNodes();
	}

	private JPopupMenu createPopupMenuForNodes() {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem(injector.getInstance(SetLabelAction.class));
		popup.add(menuItem);
		menuItem = new JMenuItem("Another popup menu item");
		popup.add(menuItem);
		return popup;
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
	public void addMouseEventListener(MouseAdapter mouseEventListener) {
		addMouseListener(mouseEventListener);
		addMouseMotionListener(mouseEventListener);
		addMouseWheelListener(mouseEventListener);
	}

	@Override
	public void showPopupMenuForNodes(int x, int y) {
		popupMenuForNodes.show(this, x, y);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
