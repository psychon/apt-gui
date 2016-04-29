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

import java.util.ArrayList;
import java.util.List;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.traits.HasLabel;

public class SetLabelCommand extends Command {

	private final Document<?> document;
	private final List<HasLabel> elements;
	private final String newLabel;
	private final List<String> oldLabels;

	public SetLabelCommand(Document<?> document, List<HasLabel> elements, String newLabel) {
		this.document = document;
		this.elements = elements;
		this.newLabel = newLabel;
		this.oldLabels = new ArrayList<>();
		for (HasLabel labelElem : elements) {
			this.oldLabels.add(labelElem.getLabel());
		}
	}

	@Override
	public void execute() {
		for (HasLabel labelElem : elements) {
			invokeSetLabelOnModelElem(labelElem, newLabel);
		}
		document.fireDocumentDirty();
	}

	private void invokeSetLabelOnModelElem(HasLabel elem, String arg) {
		Object modelElem = document.getAssociatedModelElement((GraphicalElement) elem);
		try {
			modelElem.getClass().getMethod("setLabel", String.class).invoke(modelElem, arg);
		} catch (Exception e) {
			throw new AssertionError("The setLabel method of the associated model element for a HasLabel graphical element could not be invoked.", e);
		}
	}

	@Override
	public void undo() {
		for (int i = 0; i < elements.size(); i++) {
			invokeSetLabelOnModelElem(elements.get(i), oldLabels.get(i));
		}
	}

	@Override
	public String getName() {
		return "Set Label";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
