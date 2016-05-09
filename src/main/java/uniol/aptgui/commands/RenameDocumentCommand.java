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

import com.google.common.eventbus.EventBus;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.events.DocumentTitleChangedEvent;
import uniol.aptgui.mainwindow.WindowId;

/**
 * Command that renames a document.
 */
public class RenameDocumentCommand extends Command {

	private final EventBus eventBus;
	private final WindowId windowId;
	private final Document<?> document;
	private final String newName;
	private String oldName;

	/**
	 * Creates a new RenameDocumentCommand.
	 *
	 * @param eventBus
	 *                event bus to notify windows of title changes
	 * @param windowId
	 *                window in which the document is opened
	 * @param document
	 *                the document to be renamed
	 * @param newName
	 *                the new document name
	 */
	public RenameDocumentCommand(EventBus eventBus, WindowId windowId, Document<?> document, String newName) {
		this.eventBus = eventBus;
		this.windowId = windowId;
		this.document = document;
		this.newName = newName;
	}

	@Override
	public void execute() {
		oldName = document.getName();
		document.setName(newName);
		eventBus.post(new DocumentTitleChangedEvent(windowId, document));
	}

	@Override
	public void undo() {
		document.setName(oldName);
		eventBus.post(new DocumentTitleChangedEvent(windowId, document));
	}

	@Override
	public String getName() {
		return "Rename Document to " + newName;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
