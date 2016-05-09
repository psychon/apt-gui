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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.events.DocumentTitleChangedEvent;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.mainwindow.WindowType;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.filechooser.AptFileChooser;

@SuppressWarnings("serial")
public class SaveAction extends AbstractAction {

	private final Application app;

	@Inject
	public SaveAction(Application app) {
		this.app = app;
		String name = "Save file...";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconSaveFile());
		putValue(SHORT_DESCRIPTION, name);
		putValue(MNEMONIC_KEY, KeyEvent.VK_S);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WindowId activeWindow = app.getActiveInternalWindow();
		Document<?> document = app.getDocument(activeWindow);
		if (shouldShowSaveDialog(document)) {
			File file = getSaveFile(activeWindow.getType());
			if (file != null) {
				document.setFile(file);
				app.saveToFile(document);
				// Make windows display new file name.
				app.getEventBus().post(new DocumentTitleChangedEvent(activeWindow, document));
			}
		} else {
			app.saveToFile(document);
		}

	}

	protected boolean shouldShowSaveDialog(Document<?> document) {
		return document.getFile() == null;
	}

	private File getSaveFile(WindowType type) {
		AptFileChooser fc = new AptFileChooser(
				type == WindowType.PETRI_NET,
				type == WindowType.TRANSITION_SYSTEM
		);
		int res = fc.showSaveDialog((Component) app.getMainWindow().getView());
		if (res == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFileWithExtension();
		}
		return null;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
