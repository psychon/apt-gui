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

package uniol.aptgui.gui.editor;

import com.google.inject.Inject;

import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.application.events.ToolboxEventRouter;
import uniol.aptgui.gui.editor.graphicalelements.TsDocument;
import uniol.aptgui.gui.editor.tools.toolbox.Toolbox;
import uniol.aptgui.gui.history.History;

public class TsEditorPresenterImpl extends EditorPresenterImpl implements TsEditorPresenter {

	private TsDocument document;

	@Inject
	public TsEditorPresenterImpl(EditorView view, History history, ToolboxEventRouter toolboxEventRouter) {
		super(view, history, toolboxEventRouter);
	}


	@Override
	public void setTransitionSystem(TransitionSystem ts) {
		document = new TsDocument(ts);
		setDocument(document);
		setToolbox(Toolbox.createTsToolbox(view, document));
	}

	@Override
	public TransitionSystem getTransitionSystem() {
		return document.getTransitionSystem();
	}

}


// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
