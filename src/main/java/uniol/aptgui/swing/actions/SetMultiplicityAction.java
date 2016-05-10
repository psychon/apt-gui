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

import uniol.aptgui.Application;
import uniol.aptgui.commands.Command;
import uniol.aptgui.commands.SetMultiplicityCommand;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.traits.HasMultiplicity;
import uniol.aptgui.swing.actions.base.SetSimpleAttributeAction;

@SuppressWarnings("serial")
public class SetMultiplicityAction extends SetSimpleAttributeAction<HasMultiplicity, Integer> {

	@Inject
	public SetMultiplicityAction(Application app, EventBus eventBus) {
		super("Set Multiplicity", "New multiplicity:", app, eventBus);
	}

	@Override
	protected Command createCommand(Document<?> document, List<HasMultiplicity> selection, String userInput) {
		try {
			Integer value = Integer.valueOf(userInput);
			return new SetMultiplicityCommand(document, selection, value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	protected Integer getAttribute(HasMultiplicity element) {
		return element.getMultiplicity();
	}

	@Override
	protected boolean checkEnabled(Set<GraphicalElement> selection, Class<?> commonBaseTestClass) {
		return HasMultiplicity.class.isAssignableFrom(commonBaseTestClass);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
