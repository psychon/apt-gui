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

import uniol.aptgui.editor.document.GraphicalEdge;
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.GraphicalFlow;
import uniol.aptgui.editor.document.GraphicalNode;
import uniol.aptgui.editor.document.GraphicalPlace;
import uniol.aptgui.editor.document.GraphicalTransition;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.swing.Resource;

public class CreateFlowTool extends CreateEdgeTool {

	private final Cursor cursor = Resource.getCursorCreateEdge();

	public CreateFlowTool(PnDocument document) {
		super(document);
	}

	@Override
	public Cursor getCursor() {
		return cursor;
	}

	@Override
	protected GraphicalEdge createGraphicalEdge(GraphicalNode source, GraphicalNode target,
			Point modelTargetPosition) {
		GraphicalFlow flow = new GraphicalFlow(source, target);
		return flow;
	}

	@Override
	protected void commitEdgeCreation(GraphicalEdge edge) {
		// TODO actually create a flow in the pn document

	}

	@Override
	protected boolean isValidTarget(GraphicalElement target) {
		boolean typesCorrect = (source instanceof GraphicalPlace && target instanceof GraphicalTransition)
				    || (source instanceof GraphicalTransition && target instanceof GraphicalPlace);
		return source != target && typesCorrect;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120