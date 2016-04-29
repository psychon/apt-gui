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

package uniol.aptgui.commands;

import uniol.apt.adt.ts.State;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalState;

public class CreateStateCommand extends Command {

	private final TsDocument tsDocument;
	private final GraphicalState graphicalState;
	private State tsState;

	public CreateStateCommand(TsDocument document, GraphicalState graphicalState) {
		this.tsDocument = document;
		this.graphicalState = graphicalState;
	}

	@Override
	public void execute() {
		tsState = tsDocument.getModel().createState();
		tsState.putExtension(GraphicalElement.EXTENSION_KEY, graphicalState);
		tsDocument.add(graphicalState, tsState);
		tsDocument.fireDocumentDirty();
	}

	@Override
	public void undo() {
		tsDocument.getModel().removeState(tsState);
		tsDocument.remove(graphicalState);
		tsDocument.fireDocumentDirty();
	}

	@Override
	public String getName() {
		return "Create State";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
