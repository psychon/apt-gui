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

import uniol.apt.adt.ts.Arc;
import uniol.apt.adt.ts.State;
import uniol.aptgui.editor.document.GraphicalArc;
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.TsDocument;

public class CreateArcCommand implements Command {

	private final TsDocument document;
	private final State source;
	private final State target;
	private final String label;
	private final GraphicalArc graphialArc;

	private Arc tsArc;

	public CreateArcCommand(TsDocument document, State source, State target, String label,
			GraphicalArc graphialArc) {
		this.document = document;
		this.source = source;
		this.target = target;
		this.label = label;
		this.graphialArc = graphialArc;
	}

	@Override
	public void execute() {
		tsArc = document.getTransitionSystem().createArc(source, target, label);
		tsArc.putExtension(GraphicalElement.EXTENSION_KEY, graphialArc);
		document.fireDocumentDirty();
	}

	@Override
	public void undo() {
		document.getTransitionSystem().removeArc(tsArc);
	}

	@Override
	public void redo() {
		execute();
	}

}


// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
