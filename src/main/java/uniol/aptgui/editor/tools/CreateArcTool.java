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

import java.awt.Cursor;
import java.awt.Point;

import uniol.apt.adt.ts.State;
import uniol.aptgui.commands.CreateArcCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.GraphicalArc;
import uniol.aptgui.editor.document.GraphicalEdge;
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.GraphicalNode;
import uniol.aptgui.editor.document.GraphicalState;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.swing.Resource;

public class CreateArcTool extends CreateEdgeTool {

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
	protected GraphicalEdge createGraphicalEdge(GraphicalNode source, GraphicalNode target,
			Point modelTargetPosition) {
		GraphicalArc arc = new GraphicalArc(source, target);
		return arc;
	}

	@Override
	protected void commitEdgeCreation(GraphicalEdge edge) {
		State source = document.getModelElementAt(edge.getSource().getCenter());
		State target = document.getModelElementAt(edge.getTarget().getCenter());
		TsDocument tsDocument = (TsDocument) document;
		GraphicalArc graphicalArc = (GraphicalArc) edge;
		CreateArcCommand cmd = new CreateArcCommand(tsDocument, source, target, "a", graphicalArc);
		history.execute(cmd);
	}

	@Override
	protected boolean isValidTarget(GraphicalElement target) {
		boolean typesCorrect = source instanceof GraphicalState && target instanceof GraphicalState;
		return source != target && typesCorrect;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
