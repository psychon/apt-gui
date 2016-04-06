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

package uniol.aptgui.editor.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import uniol.aptgui.Application;
import uniol.aptgui.editor.EditorView;
import uniol.aptgui.editor.graphicalelements.PnDocument;
import uniol.aptgui.editor.graphicalelements.TsDocument;
import uniol.aptgui.events.ToolSelectedEvent;
import uniol.aptgui.mainwindow.WindowId;

public class Toolbox {

	private final Logger logger = Logger.getLogger(Toolbox.class.getName());
	private final Application application;
	private final EditorView view;
	private final Map<Tool, BaseTool> tools;
	private WindowId windowId;

	private BaseTool activeTool;

	public Toolbox(Application application, EditorView view) {
		this.tools = new HashMap<>();
		this.application = application;
		this.view = view;
		application.getEventBus().register(this);
	}

	public void setWindowId(WindowId windowId) {
		this.windowId = windowId;
	}

	@Subscribe
	public void onToolSelectedEvent(ToolSelectedEvent e) {
		if (!application.getActiveWindow().equals(windowId)) {
			return;
		}

		deactivateCurrentTool();

		BaseTool selected = tools.get(e.getSelectionId());
		if (selected != null) {
			activeTool = selected;
		} else {
			logger.log(Level.WARNING, "Trying to select unavailable tool: " + e.getSelectionId());
		}

		activateCurrentTool();
	}

	private void deactivateCurrentTool() {
		if (activeTool != null) {
			activeTool.onDeactivated();
			view.removeMouseAdapter(activeTool);
		}
	}

	private void activateCurrentTool() {
		if (activeTool != null) {
			view.addMouseAdapter(activeTool);
			activeTool.onActivated();
		}
	}

	public void addTool(Tool id, BaseTool tool) {
		tools.put(id, tool);
	}

	public BaseTool getTool(Tool id) {
		return tools.get(id);
	}

	public BaseTool getActiveTool() {
		return activeTool;
	}

	public void addPnTools(PnDocument document) {
		addTool(Tool.PN_SELECTION, new SelectionTool(document));
		addTool(Tool.PN_CREATE_PLACE, new CreatePlaceTool(document, application.getHistory()));
		addTool(Tool.PN_CREATE_FLOW, new CreateFlowTool(document));
	}

	public void addTsTools(TsDocument document) {
		addTool(Tool.TS_SELECTION, new SelectionTool(document));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
