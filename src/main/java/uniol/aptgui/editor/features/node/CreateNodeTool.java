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

package uniol.aptgui.editor.features.node;

import java.awt.Point;
import java.awt.event.MouseEvent;

import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.Transform2D;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;
import uniol.aptgui.editor.features.base.Feature;

public abstract class CreateNodeTool<T extends Document<?>, U extends GraphicalNode> extends Feature {

	/**
	 * Document this tool operates on.
	 */
	protected final T document;

	/**
	 * Reference to the Document's transform object.
	 */
	protected final Transform2D transform;

	/**
	 * The visual representation of the node to be created.
	 */
	protected U node;

	/**
	 * Creates a a new CreateNodeTool for the given document.
	 *
	 * @param document
	 */
	public CreateNodeTool(T document) {
		this.document = document;
		this.transform = document.getTransform();
		initPlace();
	}

	/**
	 * Creates a new instance of this tool's GraphicalNode type.
	 *
	 * @return the new instance
	 */
	protected abstract U createGraphicalNode();

	/**
	 * Commits the creation of the node to the history.
	 *
	 * @param node
	 *                node to be created
	 */
	protected abstract void commitNodeCreation(U node);

	private void initPlace() {
		node = createGraphicalNode();
		node.setVisible(false);
		document.add(node);
	}

	@Override
	public void onActivated() {
		initPlace();
	}

	@Override
	public void onDeactivated() {
		document.remove(node);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point modelPosition = transform.applyInverse(e.getPoint());
		node.setCenter(modelPosition);
		node.setVisible(true);
		document.fireDocumentDirty();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		Point modelPosition = transform.applyInverse(e.getPoint());
		node.setCenter(modelPosition);
		commitNodeCreation(node);
		initPlace();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
