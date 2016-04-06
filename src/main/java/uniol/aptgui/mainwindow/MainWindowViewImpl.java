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

package uniol.aptgui.mainwindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import uniol.aptgui.internalwindow.InternalWindowView;
import uniol.aptgui.mainwindow.toolbar.ToolbarView;
import uniol.aptgui.swing.JFrameView;
import uniol.aptgui.swing.WindowClosingListener;

@SuppressWarnings("serial")
public class MainWindowViewImpl extends JFrameView<MainWindowPresenter> implements MainWindowView {

	private final JMenuBar jMenuBar;
	private final JDesktopPane jDesktopPane;

	public MainWindowViewImpl() {

		initWindowListener();
		setLayout(new BorderLayout());
		setSize(1280, 720);

		jMenuBar = createMenuBar();
		setJMenuBar(jMenuBar);

		jDesktopPane = createDesktopPane();
		add(jDesktopPane, BorderLayout.CENTER);
	}

	private void initWindowListener() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowClosingListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				getPresenter().onCloseButtonClicked();
			}
		});
	}

	private JMenuBar createMenuBar() {
		JMenuBar jMenuBar = new JMenuBar();
		JMenu addMenu = new JMenu("Add");
		JMenuItem newFrame = new JMenuItem("Internal Frame");
		addMenu.add(newFrame);
		jMenuBar.add(addMenu);
		return jMenuBar;
	}

	private JDesktopPane createDesktopPane() {
		JDesktopPane jDesktopPane = new JDesktopPane();
		return jDesktopPane;
	}

	@Override
	public void close() {
		dispose();
	}

	@Override
	public void addInternalWindow(InternalWindowView windowView) {
		jDesktopPane.add((Component) windowView);
	}

	@Override
	public void removeInternalWindow(InternalWindowView windowView) {
		jDesktopPane.remove((Component) windowView);
		jDesktopPane.repaint();
	}

	@Override
	public void setToolbar(ToolbarView toolbarView) {
		add((Component) toolbarView, BorderLayout.PAGE_START);
		revalidate();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
