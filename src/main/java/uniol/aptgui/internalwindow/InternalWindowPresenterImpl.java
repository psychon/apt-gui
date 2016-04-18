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
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.Presenter;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowFocusLostEvent;
import uniol.aptgui.mainwindow.WindowId;

public class InternalWindowPresenterImpl extends AbstractPresenter<InternalWindowPresenter, InternalWindowView>
		implements InternalWindowPresenter {

	private final Application application;
	private final EventBus eventBus;
	private WindowId id;

	@Inject
	public InternalWindowPresenterImpl(InternalWindowView view, Application application, EventBus eventBus) {
		super(view);
		this.application = application;
		this.eventBus = eventBus;
		eventBus.register(this);
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		if (e.getWindowId() != id) {
			return;
		}

		Document<?> doc = application.getDocument(id);
		String title = doc.getTitle();
		if (title.trim().isEmpty()) {
			title = "$" + id;
		} else {
			title = title + "($" + id + ")";
		}

		view.setTitle(title);
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
		this.id = id;
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

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
