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

import javax.swing.AbstractAction;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.mainwindow.WindowType;

/**
 * Abstract base class for all actions that should only be enabled when a
 * document editor is the active window.
 *
 */
@SuppressWarnings("serial")
public abstract class DocumentAction extends AbstractAction {

	protected final Application app;

	public DocumentAction(Application app, EventBus eventBus) {
		this.app = app;
		setEnabled(false);
		eventBus.register(this);
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		WindowType type = e.getWindowId().getType();
		setEnabled(type.isEditorWindow());
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		Document<?> document = app.getActiveDocument();
		setEnabled(document != null);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
