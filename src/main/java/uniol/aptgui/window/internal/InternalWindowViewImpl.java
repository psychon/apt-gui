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

package uniol.aptgui.window.internal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
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
public class InternalWindowViewImpl extends JInternalFrameView<InternalWindowPresenterImpl>
		implements InternalWindowView {

	private final JPanel contentPanel;

	private final InternalFrameAdapter frameAdapter = new InternalFrameAdapter() {
		@Override
		public void internalFrameClosing(InternalFrameEvent e) {
			getPresenter().onCloseButtonClicked();
		}

		@Override
		public void internalFrameActivated(InternalFrameEvent e) {
			getPresenter().onActivated();
			getGlassPane().setEnabled(false);
			getGlassPane().setVisible(false);
		}

		@Override
		public void internalFrameDeactivated(InternalFrameEvent e) {
			getGlassPane().setEnabled(true);
			getGlassPane().setVisible(true);
			getPresenter().onDeactivated();
		}
	};

	private final ComponentAdapter componentAdapter = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			getPresenter().onWindowResized(contentPanel.getWidth(), contentPanel.getHeight());
		}
		@Override
		public void componentMoved(ComponentEvent e) {
			if (ignoreNextWindowMovedEvent) {
				ignoreNextWindowMovedEvent = false;
			} else {
				getPresenter().onWindowMoved(getX(), getY());
			}
		}
	};

	private boolean ignoreNextWindowMovedEvent = false;

	public InternalWindowViewImpl() {
		initWindowListener();
		contentPanel = new JPanel();
		getContentPane().add(contentPanel);

		// This is necessary for the glass pane to intercept mouse events.
		getGlassPane().addMouseListener(new MouseAdapter() {});
	}

	private void initWindowListener() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addInternalFrameListener(frameAdapter);
		addComponentListener(componentAdapter);
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

	@Override
	public void dispose() {
		removeComponentListener(componentAdapter);
		removeInternalFrameListener(frameAdapter);
		super.dispose();
	}

	@Override
	public void ignoreNextWindowMovedEvent() {
		ignoreNextWindowMovedEvent = true;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
