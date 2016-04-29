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
import uniol.aptgui.swing.actions.SetMultiplicityAction;
import uniol.aptgui.swing.actions.SetTokensAction;

@SuppressWarnings("serial")
public class EditorViewImpl extends JPanelView<EditorPresenter> implements EditorView {

	private final Injector injector;

	private JPopupMenu popupMenu;
	private JMenuItem setLabel;
	private JMenuItem setTokens;
	private JMenuItem setMultiplicity;

	@Inject
	public EditorViewImpl(Injector injector) {
		this.injector = injector;
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(400, 300));
		createPopupMenu();
	}

	private void createPopupMenu() {
		popupMenu = new JPopupMenu();
		setLabel = new JMenuItem(injector.getInstance(SetLabelAction.class));
		setTokens = new JMenuItem(injector.getInstance(SetTokensAction.class));
		setMultiplicity = new JMenuItem(injector.getInstance(SetMultiplicityAction.class));

		popupMenu.add(setLabel);
		popupMenu.add(setTokens);
		popupMenu.add(setMultiplicity);
		popupMenu.add(new JMenuItem("Another popup menu item"));
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
	public void showPopupMenu(int x, int y) {
		popupMenu.show(this, x, y);
	}

	@Override
	public void setMenuActionActive(MenuAction action, boolean active) {
		// TODO Auto-generated method stub
		switch(action) {
		case SET_LABEL:
			setLabel.setEnabled(active);
			break;
		case SET_TOKENS:
			setTokens.setEnabled(active);
			break;
		case SET_MULTIPLICITY:
			setMultiplicity.setEnabled(active);
			break;
		default:
			break;

		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
