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
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.GraphicalState;
import uniol.aptgui.editor.document.TsDocument;

public class CreateStateCommand implements Command {

	private final TsDocument document;
	private final GraphicalState graphicalState;
	private State tsState;

	public CreateStateCommand(TsDocument document, GraphicalState graphicalState) {
		this.document = document;
		this.graphicalState = graphicalState;
	}

	@Override
	public void execute() {
		tsState = document.getModel().createState();
		tsState.putExtension(GraphicalElement.EXTENSION_KEY, graphicalState);
		document.fireDocumentDirty();
	}

	@Override
	public void undo() {
		document.getModel().removeState(tsState);
		document.fireDocumentDirty();
	}

	@Override
	public void redo() {
		execute();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
