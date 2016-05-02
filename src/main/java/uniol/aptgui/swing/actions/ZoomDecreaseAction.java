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

import javax.swing.AbstractAction;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.events.WindowClosedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.swing.Resource;

@SuppressWarnings("serial")
public class ZoomDecreaseAction extends AbstractAction {

	private final Application app;

	@Inject
	public ZoomDecreaseAction(Application app, EventBus eventBus) {
		this.app = app;
		String name = "Decrease Zoom Level";
		putValue(NAME, name);
		putValue(SHORT_DESCRIPTION, name);
		putValue(SMALL_ICON, Resource.getIconZoomOut());
		setEnabled(false);
		eventBus.register(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document<?> document = app.getActiveDocument();
		assert document != null;
		document.getTransform().decreaseScale(1);
		document.fireDocumentDirty();
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		setEnabled(e.getWindowId().getType().isEditorWindow());
	}

	@Subscribe
	public void onWindowClosedEvent(WindowClosedEvent e) {
		setEnabled(app.getActiveInternalWindow().getType().isEditorWindow());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
