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

import com.google.inject.Inject;

import uniol.aptgui.application.events.ToolboxEventListener;
import uniol.aptgui.application.events.ToolboxEventRouter;
import uniol.aptgui.gui.AbstractPresenter;
import uniol.aptgui.gui.editor.graphicalelements.Document;
import uniol.aptgui.gui.editor.layout.Layout;
import uniol.aptgui.gui.editor.tools.Tool;
import uniol.aptgui.gui.editor.tools.toolbox.ToolIds;
import uniol.aptgui.gui.editor.tools.toolbox.Toolbox;

public abstract class EditorPresenterImpl
	extends AbstractPresenter<EditorPresenter, EditorView>
	implements EditorPresenter, ToolboxEventListener {

	private Document document;
	private Toolbox toolbox;

	@Inject
	public EditorPresenterImpl(EditorView view, ToolboxEventRouter toolboxEventRouter) {
		super(view);
		toolboxEventRouter.addListener(this);
	}

	@Override
	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public void setToolbox(Toolbox toolbox) {
		this.toolbox = toolbox;
	}

	@Override
	public void onToolSelected(ToolIds tool) {
		toolbox.setActiveTool(tool);
	}

	@Override
	public void applyLayout(Layout layout) {
		int width = getView().getCanvasWidth();
		int height = getView().getCanvasHeight();
		if (width == 0 && height == 0) {
			throw new AssertionError("Layout can only be applied to the editor once it is visible (and its size is known).");
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

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
