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

package uniol.aptgui.gui.editor.tools.toolbox;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import uniol.aptgui.gui.editor.EditorView;
import uniol.aptgui.gui.editor.graphicalelements.PnDocument;
import uniol.aptgui.gui.editor.graphicalelements.TsDocument;
import uniol.aptgui.gui.editor.tools.CreateFlowTool;
import uniol.aptgui.gui.editor.tools.CreatePlaceTool;
import uniol.aptgui.gui.editor.tools.SelectionTool;
import uniol.aptgui.gui.editor.tools.Tool;
import uniol.aptgui.gui.history.History;

public class Toolbox {

	private final Logger logger = Logger.getLogger(Toolbox.class.getName());
	private final Map<ToolIds, Tool> tools;
	private ToolIds active;

	public Toolbox() {
		this.tools = new HashMap<>();
	}

	public void addTool(ToolIds id, Tool tool) {
		this.tools.put(id, tool);
	}

	public void setActiveTool(ToolIds id) {
		Tool previous = getActiveTool();
		if (previous != null) {
			previous.onDeactivated();
		}
		active = id;
		Tool current = getActiveTool();
		if (current != null) {
			current.onActivated();
		} else {
			logger.log(Level.WARNING, "Trying to select unavailable tool.");
		}
	}

	public Tool getActiveTool() {
		return tools.get(active);
	}

	public static Toolbox createPnToolbox(EditorView view, PnDocument document, History history) {
		Toolbox pnToolbox = new Toolbox();
		pnToolbox.addTool(ToolIds.SELECTION, new SelectionTool(view, document));
		pnToolbox.addTool(ToolIds.CREATE_PLACE, new CreatePlaceTool(view, document, history));
		pnToolbox.addTool(ToolIds.CREATE_FLOW, new CreateFlowTool(view, document));
		return pnToolbox;
	}

	public static Toolbox createTsToolbox(EditorView view, TsDocument document) {
		Toolbox pnToolbox = new Toolbox();
		pnToolbox.addTool(ToolIds.SELECTION, new SelectionTool(view, document));
		return pnToolbox;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
