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

package uniol.aptgui.swing.actions;

import java.util.List;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.apt.adt.pn.Marking;
import uniol.apt.adt.pn.Place;
import uniol.aptgui.Application;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.SetTokensCommand;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalPlace;
import uniol.aptgui.editor.document.graphical.traits.HasTokens;
import uniol.aptgui.swing.actions.base.SetSimpleAttributeAction;

@SuppressWarnings("serial")
public class SetTokensAction extends SetSimpleAttributeAction<GraphicalPlace, Long> {

	@Inject
	public SetTokensAction(Application app, EventBus eventBus) {
		super("Set Tokens", "New token count:", app, eventBus);
	}

	@Override
	protected Command createCommand(Document<?> document, List<GraphicalPlace> selection, String userInput) {
		try {
			Long value = Long.valueOf(userInput);
			return new SetTokensCommand((PnDocument) document, selection, value);
		} catch (NumberFormatException e) {
			return null;
		}

	}

	@Override
	protected Long getAttribute(GraphicalPlace element) {
		PnDocument pnDoc = (PnDocument) document;
		Marking marking = pnDoc.getModel().getInitialMarking();
		Place place = pnDoc.getAssociatedModelElement(element);
		return marking.getToken(place).getValue();
	}

	@Override
	protected boolean checkEnabled(Set<GraphicalElement> selection, Class<?> commonBaseTestClass) {
		return HasTokens.class.isAssignableFrom(commonBaseTestClass);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
