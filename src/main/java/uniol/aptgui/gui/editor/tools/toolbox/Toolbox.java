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

import uniol.aptgui.gui.editor.EditorView;
import uniol.aptgui.gui.editor.graphicalelements.PnDocument;
import uniol.aptgui.gui.editor.graphicalelements.TsDocument;
import uniol.aptgui.gui.editor.tools.SelectionTool;
import uniol.aptgui.gui.editor.tools.Tool;

public class Toolbox {

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
		getActiveTool().onActivated();
	}

	public Tool getActiveTool() {
		return tools.get(active);
	}

	public static Toolbox createPnToolbox(PnDocument document, EditorView view) {
		Toolbox pnToolbox = new Toolbox();
		pnToolbox.addTool(ToolIds.SELECTION, new SelectionTool(document, view));
		return pnToolbox;
	}

	public static Toolbox createTsToolbox(TsDocument document, EditorView view) {
		Toolbox pnToolbox = new Toolbox();
		pnToolbox.addTool(ToolIds.SELECTION, new SelectionTool(document, view));
		return pnToolbox;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
