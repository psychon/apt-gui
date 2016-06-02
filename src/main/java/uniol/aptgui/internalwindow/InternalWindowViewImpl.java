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

package uniol.aptgui.internalwindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import uniol.aptgui.View;
import uniol.aptgui.mainwindow.WindowType;
import uniol.aptgui.swing.JInternalFrameView;
import uniol.aptgui.swing.Resource;

@SuppressWarnings("serial")
public class InternalWindowViewImpl extends JInternalFrameView<InternalWindowPresenter> implements InternalWindowView {

	private final JPanel contentPanel;

	public InternalWindowViewImpl() {
		initWindowListener();
		contentPanel = new JPanel();
		getContentPane().add(contentPanel);
	}

	private void initWindowListener() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				getPresenter().onCloseButtonClicked();
			}

			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				getPresenter().onActivated();
			}

			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				getPresenter().onDeactivated();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				getPresenter().onWindowResized(contentPanel.getWidth(), contentPanel.getHeight());
			}
		});
	}

	@Override
	public void setContent(View<?> view) {
		contentPanel.removeAll();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add((Component) view, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	@Override
	public void focus() {
		try {
			setSelected(true);
		} catch (PropertyVetoException e) {
		}
	}

	@Override
	public void setPadding(int padding) {
		contentPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
	}

	@Override
	public void setIcon(WindowType windowType) {
		switch (windowType) {
		case PETRI_NET:
			setFrameIcon(Resource.getIconPetriNetDocument());
			break;
		case TRANSITION_SYSTEM:
			setFrameIcon(Resource.getIconTransitionSystemDocument());
			break;
		default:
			break;
		}
	}

	@Override
	public Point getPosition() {
		return new Point(getX(), getY());
	}

	@Override
	public void setPosition(int x, int y) {
		setBounds(x, y, getWidth(), getHeight());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
