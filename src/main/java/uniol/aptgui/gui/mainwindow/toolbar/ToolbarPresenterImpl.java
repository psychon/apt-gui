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

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import uniol.aptgui.gui.AbstractPresenter;
import uniol.aptgui.gui.editor.tools.toolbox.ToolIds;

public class ToolbarPresenterImpl
	extends AbstractPresenter<ToolbarPresenter, ToolbarView>
	implements ToolbarPresenter {

	private final Map<ToolbarContext, ToolIds> activeTools;
	private ToolbarContext context;

	@Inject
	public ToolbarPresenterImpl(ToolbarView view) {
		super(view);
		activeTools = new HashMap<>();
		activeTools.put(ToolbarContext.PETRI_NET, ToolIds.SELECTION);
		activeTools.put(ToolbarContext.TRANSITION_SYSTEM, ToolIds.SELECTION);
		activeTools.put(ToolbarContext.NONE, ToolIds.SELECTION);

		setContext(ToolbarContext.NONE);
	}

	@Override
	public void fireActiveToolChanged() {
		ToolIds activeTool = activeTools.get(context);
		view.setActiveTool(activeTool);
	}

	@Override
	public void setActiveTool(ToolIds tool) {
		activeTools.put(context, tool);
		fireActiveToolChanged();
	}

	@Override
	public void setContext(ToolbarContext context) {
		this.context = context;
		view.setContext(context);
		fireActiveToolChanged();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
