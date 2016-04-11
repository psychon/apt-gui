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

import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;

public class Toolbox {

	private final Logger logger = Logger.getLogger(Toolbox.class.getName());
	private final Application application;
	private final Map<ToolId, Tool> tools;

	private Tool activeTool;

	@Inject
	public Toolbox(Application application) {
		this.tools = new HashMap<>();
		this.application = application;
	}

	public void setActiveTool(ToolId id) {
		deactivateCurrentTool();
		Tool selected = tools.get(id);
		if (selected != null) {
			activeTool = selected;
		} else {
			logger.log(Level.WARNING, "Trying to select unavailable tool: " + id);
		}
		activateCurrentTool();
	}

	private void deactivateCurrentTool() {
		if (activeTool != null) {
			activeTool.onDeactivated();
		}
	}

	private void activateCurrentTool() {
		if (activeTool != null) {
			activeTool.onActivated();
		}
	}

	public void addTool(ToolId id, Tool tool) {
		tools.put(id, tool);
	}

	public Tool getTool(ToolId id) {
		return tools.get(id);
	}

	public Tool getActiveTool() {
		return activeTool;
	}

	public void addPnTools(PnDocument document) {
		addTool(ToolId.PN_SELECTION, new SelectionTool(document));
		addTool(ToolId.PN_CREATE_PLACE, new CreatePlaceTool(document, application.getHistory()));
		addTool(ToolId.PN_CREATE_TRANSITION, new CreateTransitionTool(document, application.getHistory()));
		addTool(ToolId.PN_CREATE_FLOW, new CreateFlowTool(document));
	}

	public void addTsTools(TsDocument document) {
		addTool(ToolId.TS_SELECTION, new SelectionTool(document));
		addTool(ToolId.TS_CREATE_STATE, new CreateStateTool(document, application.getHistory()));
		addTool(ToolId.TS_CREATE_ARC, new CreateArcTool(document));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
