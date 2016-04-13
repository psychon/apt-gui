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

import uniol.apt.adt.pn.Transition;
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.GraphicalTransition;
import uniol.aptgui.editor.document.PnDocument;

public class CreateTransitionCommand implements Command {

	private final PnDocument pnDocument;
	private final GraphicalTransition graphicalTransition;
	private Transition pnTransition;

	public CreateTransitionCommand(PnDocument pnDocument, GraphicalTransition transition) {
		this.pnDocument = pnDocument;
		this.graphicalTransition = transition;
	}

	@Override
	public void execute() {
		pnTransition = pnDocument.getModel().createTransition();
		pnTransition.putExtension(GraphicalElement.EXTENSION_KEY, graphicalTransition);
		pnDocument.fireDocumentDirty();
	}

	@Override
	public void undo() {
		pnDocument.getModel().removeTransition(pnTransition);
		pnDocument.fireDocumentDirty();
	}

	@Override
	public void redo() {
		execute();
	}

}


// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
