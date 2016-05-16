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
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import uniol.aptgui.internalwindow.InternalWindowView;
import uniol.aptgui.mainwindow.menu.MenuView;
import uniol.aptgui.mainwindow.toolbar.ToolbarView;
import uniol.aptgui.swing.JFrameView;
import uniol.aptgui.swing.WindowClosingListener;

@SuppressWarnings("serial")
public class MainWindowViewImpl extends JFrameView<MainWindowPresenter> implements MainWindowView {

	private final JDesktopPane jDesktopPane;

	public MainWindowViewImpl() {
		initWindowListener();
		setLayout(new BorderLayout());
		setSize(1280, 720);
		setLocationByPlatform(true);

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

	@Override
	public void setMenu(MenuView menuView) {
		setJMenuBar((JMenuBar) menuView);
		revalidate();
	}

	@Override
	public void cascadeInternalWindows() {
		cascade(jDesktopPane);
	}

	public static void cascade(JDesktopPane desktopPane) {
		JInternalFrame[] frames = desktopPane.getAllFrames();
		if (frames.length == 0) {
			return;
		}

		Arrays.sort(frames, new ZOrderComparator(desktopPane));
		cascade(frames, desktopPane.getSize());
	}

	private static void cascade(JInternalFrame[] frames, Dimension desktopSize) {
		int maxOffset = (int) Math.min(desktopSize.getWidth(), desktopSize.getHeight()) - 30;
		int offset = 0;
		for (int i = 0; i < frames.length; i++) {
			frames[i].setLocation(offset, offset);
			Dimension frameDim = frames[i].getSize();
			Dimension contentDim = frames[i].getContentPane().getSize();
			offset += Math.max(frameDim.width - contentDim.width, frameDim.height - contentDim.height);
			if (offset >= maxOffset) {
				offset = 0;
			}
		}
	}

	private static class ZOrderComparator implements Comparator<JInternalFrame> {
		private final JLayeredPane desktop;

		public ZOrderComparator(JLayeredPane pane) {
			desktop = pane;
		}

		@Override
		public int compare(JInternalFrame o1, JInternalFrame o2) {
			return desktop.getPosition(o2) - desktop.getPosition(o1);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
