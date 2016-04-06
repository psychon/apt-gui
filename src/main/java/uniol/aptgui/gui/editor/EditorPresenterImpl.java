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

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.application.events.ToolSelectedEvent;
import uniol.aptgui.gui.AbstractPresenter;
import uniol.aptgui.gui.editor.features.Feature;
import uniol.aptgui.gui.editor.features.HoverFeature;
import uniol.aptgui.gui.editor.graphicalelements.Document;
import uniol.aptgui.gui.editor.graphicalelements.DocumentListener;
import uniol.aptgui.gui.editor.layout.Layout;
import uniol.aptgui.gui.editor.tools.Tool;
import uniol.aptgui.gui.editor.tools.toolbox.Toolbox;
import uniol.aptgui.gui.history.History;

public abstract class EditorPresenterImpl extends AbstractPresenter<EditorPresenter, EditorView>
		implements EditorPresenter, DocumentListener {

	protected History history;
	protected Document document;
	protected Toolbox toolbox;
	protected List<Feature> features;

	@Inject
	public EditorPresenterImpl(EditorView view, History history, EventBus eventBus) {
		super(view);
		this.history = history;
		this.features = new ArrayList<>();
		eventBus.register(this);
	}

	@Override
	public void setDocument(Document document) {
		this.document = document;
		this.document.addListener(this);
		for (Feature feature : features) {
			view.removeMouseAdapter(feature);
		}
		features.clear();
		features.add(new HoverFeature(document, view));
		for (Feature feature : features) {
			view.addMouseAdapter(feature);
		}
	}

	@Override
	public void setToolbox(Toolbox toolbox) {
		this.toolbox = toolbox;
	}

	@Subscribe
	public void onToolSelected(ToolSelectedEvent e) {
		toolbox.setActiveTool(e.getSelectionId());
		Tool tool = toolbox.getActiveTool();
		if (tool != null) {
			view.getGraphicalComponent().setCursor(tool.getCursor());
		}
		view.repaint();
	}

	@Override
	public void applyLayout(Layout layout) {
		int width = getView().getCanvasWidth();
		int height = getView().getCanvasHeight();
		if (width == 0 && height == 0) {
			throw new AssertionError(
					"Layout can only be applied to the editor once it is visible (and its size is known).");
		}
		if (document != null) {
			document.setWidth(width);
			document.setHeight(height);
			document.applyLayout(layout);
		}
	}

	@Override
	public void onPaint(Graphics2D graphics) {
		document.drawDocument(graphics);
		for (Feature feature : features) {
			feature.draw(graphics);
		}
		Tool tool = toolbox.getActiveTool();
		if (tool != null) {
			tool.draw(graphics);
		}
	}

	@Override
	public void translateView(int dx, int dy) {
		document.translateView(dx, dy);
		getView().repaint();
	}

	@Override
	public void scaleView(double scale) {
		document.scaleView(scale);
		getView().repaint();
	}

	@Override
	public void onDocumentDirty() {
		getView().repaint();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
