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

package uniol.aptgui.gui.mainwindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.aptgui.gui.JFrameView;
import uniol.aptgui.gui.actions.NewPnAction;
import uniol.aptgui.gui.misc.WindowClosingListener;

@SuppressWarnings("serial")
public class MainWindowViewImpl extends JFrameView<MainWindowPresenter> implements MainWindowView {

	private final Injector injector;
	private final JMenuBar jMenuBar;
	private final JToolBar jToolBar;
	private final JDesktopPane jDesktopPane;

	@Inject
	public MainWindowViewImpl(Injector injector) {
		this.injector = injector;

		initWindowListener();
		setLayout(new BorderLayout());
		setSize(1280, 720);

		jMenuBar = createMenuBar();
		setJMenuBar(jMenuBar);

		jToolBar = createToolBar();
		add(jToolBar, BorderLayout.PAGE_START);

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

	private JToolBar createToolBar() {
		JToolBar jToolBar = new JToolBar();
		JButton btn = new JButton(injector.getInstance(NewPnAction.class));
		jToolBar.add(btn);
		return jToolBar;
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
	public void addInternalWindow(Component component) {
		jDesktopPane.add(component);
	}

	@Override
	public void removeInternalWindow(Component component) {
		jDesktopPane.remove(component);
		jDesktopPane.repaint();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
