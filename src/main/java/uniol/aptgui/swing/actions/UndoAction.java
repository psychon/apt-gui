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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.commands.History;
import uniol.aptgui.events.HistoryChangedEvent;
import uniol.aptgui.swing.Resource;

@SuppressWarnings("serial")
public class UndoAction extends AbstractAction {

	private static final String ACTION_NAME = "Undo";
	private final History history;

	@Inject
	public UndoAction(History history, EventBus eventBus) {
		this.history = history;

		setNameAndDescription(ACTION_NAME);
		putValue(SMALL_ICON, Resource.getIconUndo());
		putValue(MNEMONIC_KEY, KeyEvent.VK_U);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Z"));
		setEnabled(false);

		eventBus.register(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		history.undo();
	}

	@Subscribe
	public void onHistoryChangedEvent(HistoryChangedEvent e) {
		if (history.canUndo()) {
			setEnabled(true);
			setNameAndDescription(ACTION_NAME + " " + history.getNextUndoCommand().getName());
		} else {
			setEnabled(false);
			setNameAndDescription(ACTION_NAME);
		}
	}

	private void setNameAndDescription(String value) {
		putValue(NAME, value);
		putValue(SHORT_DESCRIPTION, value);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
