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

package uniol.aptgui.gui.mainwindow.toolbar;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.application.events.ToolSelectedEvent;
import uniol.aptgui.application.events.WindowFocusGainedEvent;
import uniol.aptgui.application.events.WindowFocusLostEvent;
import uniol.aptgui.gui.AbstractPresenter;
import uniol.aptgui.gui.editor.tools.toolbox.Tool;

public class ToolbarPresenterImpl extends AbstractPresenter<ToolbarPresenter, ToolbarView> implements ToolbarPresenter {

	private final EventBus eventBus;
	private Tool activePnTool;
	private Tool activeTsTool;

	@Inject
	public ToolbarPresenterImpl(ToolbarView view, EventBus eventBus) {
		super(view);
		this.eventBus = eventBus;
		this.activePnTool = Tool.PN_SELECTION;
		this.activeTsTool = Tool.TS_SELECTION;

		this.eventBus.register(this);
		view.setPetriNetToolsVisible(false);
		view.setTransitionSystemToolsVisible(false);
	}

	@Subscribe
	public void onWindowFocusLostEvent(WindowFocusLostEvent e) {
		view.setPetriNetToolsVisible(false);
		view.setTransitionSystemToolsVisible(false);
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		switch (e.getWindowId().getType()) {
		case MODULE:
			break;
		case PETRI_NET:
			view.setPetriNetToolsVisible(true);
			view.setTransitionSystemToolsVisible(false);
			view.setActiveTool(activePnTool);
			break;
		case TRANSITION_SYSTEM:
			view.setPetriNetToolsVisible(false);
			view.setTransitionSystemToolsVisible(true);
			view.setActiveTool(activeTsTool);
			break;
		}
	}

	@Subscribe
	public void onToolSelectedEvent(ToolSelectedEvent e) {
		if (e.getSelectionId().isPetriNetTool()) {
			activePnTool = e.getSelectionId();
		}
		if (e.getSelectionId().isTransitionSystemTool()) {
			activeTsTool = e.getSelectionId();
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
