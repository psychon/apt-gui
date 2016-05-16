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

package uniol.aptgui.editor;

import java.awt.Graphics2D;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.commands.History;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.DocumentListener;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.features.ContextMenuFeature;
import uniol.aptgui.editor.features.FireTransitionTool;
import uniol.aptgui.editor.features.HoverFeature;
import uniol.aptgui.editor.features.SelectionTool;
import uniol.aptgui.editor.features.ViewportFeature;
import uniol.aptgui.editor.features.base.Feature;
import uniol.aptgui.editor.features.base.FeatureCollection;
import uniol.aptgui.editor.features.base.FeatureId;
import uniol.aptgui.editor.features.base.SingleFeatureCollection;
import uniol.aptgui.editor.features.edge.CreateArcTool;
import uniol.aptgui.editor.features.edge.CreateFlowTool;
import uniol.aptgui.editor.features.node.CreatePlaceTool;
import uniol.aptgui.editor.features.node.CreateStateTool;
import uniol.aptgui.editor.features.node.CreateTransitionTool;
import uniol.aptgui.events.ToolSelectedEvent;
import uniol.aptgui.events.WindowFocusGainedEvent;
import uniol.aptgui.events.WindowFocusLostEvent;
import uniol.aptgui.mainwindow.WindowId;

public class EditorPresenterImpl extends AbstractPresenter<EditorPresenter, EditorView>
		implements EditorPresenter, DocumentListener {

	private final Application application;

	private final FeatureCollection features;
	private final SingleFeatureCollection tools;

	private WindowId windowId;
	private Document<?> document;

	@Inject
	public EditorPresenterImpl(EditorView view, Application application) {
		super(view);
		this.application = application;
		this.features = new FeatureCollection();
		this.tools = new SingleFeatureCollection();

		view.addMouseEventListener(features);
		view.addMouseEventListener(tools);
		application.getEventBus().register(this);
	}

	@Subscribe
	public void onToolSelectedEvent(ToolSelectedEvent e) {
		if (application.getActiveInternalWindow() != windowId) {
			return;
		}

		features.setListening(false);
		tools.setListening(false);
		tools.setActive(e.getSelectionId());
		tools.setListening(true);
		features.setListening(true);
		Feature tool = tools.getActiveFeature();
		view.setCursor(tool.getCursor());
		view.repaint();
	}

	@Subscribe
	public void onWindowFocusGainedEvent(WindowFocusGainedEvent e) {
		if (e.getWindowId() != windowId) {
			return;
		}
		tools.onActivated();
	}

	@Subscribe
	public void onWindowFocusLostEvent(WindowFocusLostEvent e) {
		if (e.getWindowId() != windowId) {
			return;
		}
		tools.onDeactivated();
	}

	@Override
	public void setWindowId(WindowId windowId) {
		this.windowId = windowId;
	}

	@Override
	public void setDocument(Document<?> document) {
		if (document != null) {
			document.removeListener(this);
		}

		this.document = document;
		this.document.addListener(this);

		features.onDeactivated();
		features.put(FeatureId.VIEWPORT, new ViewportFeature(document));
		features.put(FeatureId.HOVER, new HoverFeature(document));
		features.put(FeatureId.CONTEXT_MENU, new ContextMenuFeature(document, view));
		features.onActivated();
		features.setListening(true);

		History hist = application.getHistory();
		tools.clear();
		if (document instanceof PnDocument) {
			PnDocument pnDocument = (PnDocument) document;
			tools.put(FeatureId.PN_SELECTION, new SelectionTool(document, hist));
			tools.put(FeatureId.PN_CREATE_PLACE, new CreatePlaceTool(pnDocument, hist));
			tools.put(FeatureId.PN_CREATE_TRANSITION, new CreateTransitionTool(pnDocument, hist));
			tools.put(FeatureId.PN_CREATE_FLOW, new CreateFlowTool(pnDocument, hist));
			tools.put(FeatureId.PN_FIRE_TRANSITION, new FireTransitionTool(pnDocument, hist));
		} else if (document instanceof TsDocument) {
			TsDocument tsDocument = (TsDocument) document;
			tools.put(FeatureId.TS_SELECTION, new SelectionTool(document, hist));
			tools.put(FeatureId.TS_CREATE_STATE, new CreateStateTool(tsDocument, hist));
			tools.put(FeatureId.TS_CREATE_ARC, new CreateArcTool(tsDocument, hist, view));
		}
		tools.setListening(true);
	}

	@Override
	public void onPaint(Graphics2D graphics) {
		if (document.isVisible()) {
			document.draw(graphics);
		}
	}

	@Override
	public void onDocumentDirty() {
		if (document.isVisible()) {
			getView().repaint();
		}
	}

	@Override
	public void onSelectionChanged(Document<?> source) {
		// Do nothing.
	}

	@Override
	public void onDocumentChanged() {
		// No other action necessary except a repaint.
		onDocumentDirty();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
