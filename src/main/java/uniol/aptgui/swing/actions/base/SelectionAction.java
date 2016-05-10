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

package uniol.aptgui.swing.actions.base;

import java.util.Set;

import javax.swing.AbstractAction;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.events.DocumentSelectionChangedEvent;

@SuppressWarnings("serial")
public abstract class SelectionAction extends AbstractAction {

	protected final Application app;

	public SelectionAction(Application app, EventBus eventBus) {
		this.app = app;
		setEnabled(false);
		eventBus.register(this);
	}

	@Subscribe
	public void onDocumentSelectionChangedEvent(DocumentSelectionChangedEvent e) {
		Class<? extends GraphicalElement> commonBase = e.getDocument().getSelectionCommonBaseClass();
		// Compare traits with Object if commonBase is null, since it
		// will not throw an exception but return false for the
		// isAssignableFrom calls as expected.
		Class<?> testClass = (commonBase == null) ? Object.class : commonBase;

		setEnabled(checkEnabled(e.getDocument().getSelection(), testClass));
	}

	/**
	 * Called by the superclass when the document selection changes. This
	 * method must return true, if the action should still be enabled.
	 *
	 * @return true, if the action is applicable for the given selection
	 */
	protected abstract boolean checkEnabled(Set<GraphicalElement> selection, Class<?> commonBaseTestClass);

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
