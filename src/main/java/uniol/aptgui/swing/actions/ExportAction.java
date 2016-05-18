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
import java.io.File;

import javax.swing.JFileChooser;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.Resource;
import uniol.aptgui.swing.actions.base.DocumentAction;
import uniol.aptgui.swing.filechooser.AptFileChooser;

@SuppressWarnings("serial")
public class ExportAction extends DocumentAction {

	@Inject
	public ExportAction(Application app, EventBus eventBus) {
		super(app, eventBus);
		String name = "Export...";
		putValue(NAME, name);
		putValue(SMALL_ICON, Resource.getIconExport());
		putValue(SHORT_DESCRIPTION, name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WindowId activeWindow = app.getActiveInternalWindow();
		Document<?> document = app.getDocument(activeWindow);

		AptFileChooser fc = AptFileChooser.exportChooser(document);
		int res = fc.showSaveDialog((Component) app.getMainWindow().getView());
		if (res == JFileChooser.APPROVE_OPTION) {
			File exportFile = fc.getSelectedFileWithExtension();
			if (fc.isSvgFilterSelected()) {
				app.exportSvg(document, exportFile);
			} else if (fc.isPngFilterSelected()) {
				app.exportPng(document, exportFile);
			}
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
