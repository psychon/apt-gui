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

package uniol.aptgui.editor.tools.edge;

import java.awt.Cursor;

import uniol.apt.adt.ts.State;
import uniol.aptgui.commands.CreateArcCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalArc;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalState;
import uniol.aptgui.swing.Resource;

public class CreateArcTool extends CreateEdgeTool<TsDocument, GraphicalArc> {

	private final Cursor cursor = Resource.getCursorCreateEdge();
	private final History history;

	public CreateArcTool(TsDocument document, History history) {
		super(document);
		this.history = history;
	}

	@Override
	public Cursor getCursor() {
		return cursor;
	}

	@Override
	protected GraphicalArc createGraphicalEdge(GraphicalNode source, GraphicalNode target) {
		GraphicalArc arc = new GraphicalArc(source, target);
		return arc;
	}

	@Override
	protected void commitEdgeCreation(GraphicalArc edge) {
		State source = document.getAssociatedModelElement(edge.getSource());
		State target = document.getAssociatedModelElement(edge.getTarget());
		CreateArcCommand cmd = new CreateArcCommand(document, source, target, "a", edge);
		history.execute(cmd);
	}

	@Override
	protected boolean isValidTarget(GraphicalElement target) {
		boolean typesCorrect = graphicalSource instanceof GraphicalState && target instanceof GraphicalState;
		return graphicalSource != target && typesCorrect;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
