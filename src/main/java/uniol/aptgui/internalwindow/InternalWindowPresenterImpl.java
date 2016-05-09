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

package uniol.aptgui.internalwindow;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.Presenter;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.DocumentListener;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowFocusLostEvent;
import uniol.aptgui.mainwindow.WindowId;

public class InternalWindowPresenterImpl extends AbstractPresenter<InternalWindowPresenter, InternalWindowView>
		implements InternalWindowPresenter {

	private final Application application;
	private final EventBus eventBus;
	private String title;
	private String computedTitle;
	private WindowId id;

	private DocumentListener titleChangeListener = new DocumentListener() {
		@Override public void onSelectionChanged(Class<? extends GraphicalElement> commonBaseClass) {}
		@Override public void onDocumentDirty() {}
		@Override
		public void onDocumentChanged() {
			updateTitle();
		}
	};

	@Inject
	public InternalWindowPresenterImpl(InternalWindowView view, Application application, EventBus eventBus) {
		super(view);
		this.application = application;
		this.eventBus = eventBus;
		this.title = "";
		setPadding(3);
	}

	@Override
	public void setContentPresenter(Presenter<?> presenter) {
		getView().setContent(presenter.getView());
	}

	@Override
	public void onCloseButtonClicked() {
		application.closeWindow(id);
		eventBus.post(new WindowFocusLostEvent(id));
	}

	@Override
	public void setWindowId(WindowId id) {
		// Remove old listener.
		if (this.id != null) {
			Document<?> document = application.getDocument(id);
			if (document != null) {
				document.removeListener(titleChangeListener);
			}
		}

		// Add listener so that the title gets updated when the document changes.
		Document<?> document = application.getDocument(id);
		if (document != null) {
			document.addListener(titleChangeListener);
		}

		this.id = id;
		updateTitle();
	}

	@Override
	public WindowId getWindowId() {
		return id;
	}

	@Override
	public void onActivated() {
		eventBus.post(new WindowFocusGainedEvent(id));
	}

	@Override
	public void focus() {
		view.focus();
	}

	@Override
	public void setPadding(int padding) {
		view.setPadding(padding);
	}

	private void updateTitle() {
		if (!title.isEmpty()) {
			computedTitle = title;
			view.setTitle(computedTitle);
			return;
		}

		Document<?> document = application.getDocument(id);
		if (document != null) {
			computedTitle = "";
			if (document.hasUnsavedChanges()) {
				computedTitle += "* ";
			}

			if (document.getFile() != null) {
				computedTitle += String.format("%s (%s) (%s)", document.getName(), id.toString(), document.getFile());
			} else {
				computedTitle += String.format("%s (%s)", document.getName(), id.toString());
			}

			view.setTitle(computedTitle);
			return;
		}
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		updateTitle();
	}

	@Override
	public String getDisplayedTitle() {
		return computedTitle;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
