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

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.commands.CreateTransitionCommand;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.GraphicalElement;
import uniol.aptgui.editor.document.GraphicalTransition;
import uniol.aptgui.editor.document.PnDocument;

public class CreateTransitionTool extends Tool {

	private final History history;
	private final PnDocument document;
	private GraphicalTransition transition;

	public CreateTransitionTool(PnDocument document, History history) {
		this.history = history;
		this.document = document;
		initPlace();
	}

	private void initPlace() {
		this.transition = new GraphicalTransition();
		this.transition.setVisible(false);
	}

	@Override
	public void onActivated() {
		document.add(transition);
	}

	@Override
	public void onDeactivated() {
		document.remove(transition);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Use view coordinates since the Tool draw method doesn't take the Document transform into account.
		transition.setCenter(e.getPoint());
		transition.setVisible(true);
		document.fireDocumentDirty();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point modelPoint = document.transformViewToModel(e.getPoint());
		GraphicalElement elem = document.getGraphicalElementAt(modelPoint);
		// Only add the place if the user clicked the canvas instead of
		// an element.
		if (elem == null) {
			transition.setCenter(modelPoint);
			history.execute(new CreateTransitionCommand(document, transition));
			initPlace();
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
