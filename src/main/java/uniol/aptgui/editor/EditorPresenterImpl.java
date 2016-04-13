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
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.DocumentListener;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.features.HoverFeature;
import uniol.aptgui.editor.tools.Tool;
import uniol.aptgui.editor.tools.Toolbox;
import uniol.aptgui.events.ToolSelectedEvent;
import uniol.aptgui.mainwindow.WindowId;

public class EditorPresenterImpl extends AbstractPresenter<EditorPresenter, EditorView>
		implements EditorPresenter, MouseEventListener, DocumentListener {

	private final Application application;

	private final Toolbox toolbox;
	private HoverFeature hoverFeature;

	private WindowId windowId;
	private Document<?> document;

	@Inject
	public EditorPresenterImpl(EditorView view, Application application) {
		super(view);
		view.setMouseEventListener(this);
		this.application = application;
		this.toolbox = new Toolbox(application);
		application.getEventBus().register(this);
	}

	public void setWindowId(WindowId windowId) {
		this.windowId = windowId;
	}

	@Override
	public void setDocument(Document<?> document) {
		this.document = document;
		this.document.addListener(this);
		hoverFeature = new HoverFeature(document);
		if (document instanceof PnDocument) {
			toolbox.addPnTools((PnDocument)document);
		} else if (document instanceof TsDocument) {
			toolbox.addTsTools((TsDocument)document);
		}
	}

	@Subscribe
	public void onToolSelected(ToolSelectedEvent e) {
		if (!application.getActiveInternalWindow().equals(windowId)) {
			return;
		}
		toolbox.setActiveTool(e.getSelectionId());
		Tool tool = toolbox.getActiveTool();
		if (tool != null) {
			view.setCursor(tool.getCursor());
		}
		view.repaint();
	}

	@Override
	public void onPaint(Graphics2D graphics) {
		document.drawDocument(graphics);
		hoverFeature.draw(graphics);
	}

	@Override
	public void translateView(int dx, int dy) {
		document.getTransform().translateView(dx, dy);
		getView().repaint();
	}

	@Override
	public void scaleView(double scale) {
		document.getTransform().scaleView(scale);
		getView().repaint();
	}

	@Override
	public void onDocumentDirty() {
		getView().repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (document.isVisible()) {
			toolbox.getActiveTool().mouseClicked(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (document.isVisible()) {
			toolbox.getActiveTool().mouseDragged(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (document.isVisible()) {
			toolbox.getActiveTool().mouseMoved(e);
			hoverFeature.mouseMoved(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (document.isVisible()) {
			toolbox.getActiveTool().mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (document.isVisible()) {
			toolbox.getActiveTool().mouseReleased(e);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (document.isVisible()) {
			toolbox.getActiveTool().mouseWheelMoved(e);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
