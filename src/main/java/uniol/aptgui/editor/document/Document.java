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

package uniol.aptgui.editor.document;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uniol.apt.adt.extension.IExtensible;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.layout.Layout;

public abstract class Document<T> {

	private final List<DocumentListener> listeners;

	/**
	 * Underlying model element. Either PetriNet or TransitionSystem.
	 */
	private T model;

	/**
	 * Map from GraphicalElements to their model objects of undetermined
	 * type. The model object may be null if there is no corresponding model
	 * object.
	 */
	private final Map<GraphicalElement, Object> elements;

	protected String title;
	protected File file;
	protected boolean hasUnsavedChanges;
	protected int width;
	protected int height;
	protected boolean visible;

	private final Transform2D transform;

	public Document() {
		this(400, 300);
	}

	public Document(int width, int height) {
		this.listeners = new ArrayList<>();
		this.width = width;
		this.height = height;
		this.visible = false;
		this.hasUnsavedChanges = false;
		this.transform = new Transform2D();
		this.elements = new HashMap<>();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	/**
	 * Adds a new standalone GraphicalElement to this Document.
	 *
	 * @param graphicalElem
	 */
	public void add(GraphicalElement graphicalElem) {
		add(graphicalElem, null);
	}

	/**
	 * Adds a new GraphicalElement to this Document together with its
	 * corresponding model element.
	 *
	 * @param graphicalElem
	 * @param modelElem
	 */
	public void add(GraphicalElement graphicalElem, Object modelElem) {
		elements.put(graphicalElem, modelElem);
	}

	/**
	 * Removes the given GraphicalElement from this Document.
	 * @param graphicalElem
	 */
	public void remove(GraphicalElement graphicalElem) {
		elements.remove(graphicalElem);
	}

	/**
	 * Returns a view of all GraphicalElements in this Document. The view is
	 * backed by the Document implementation, so changes to the return value
	 * are reflected in the Document.
	 *
	 * @return
	 */
	public Set<GraphicalElement> getGraphicalElements() {
		return elements.keySet();
	}

	/**
	 * Returns the model element that is associated with the given
	 * GraphicalElement.
	 *
	 * @param graphicalElem
	 *                GraphicalElement to look for
	 * @return the associated model element
	 */
	@SuppressWarnings("unchecked")
	public <U> U getAssociatedModelElement(GraphicalElement graphicalElem) {
		return (U) elements.get(graphicalElem);
	}

	public Transform2D getTransform() {
		return transform;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addListener(DocumentListener listener) {
		listeners.add(listener);
	}

	public void removeListener(DocumentListener listener) {
		listeners.remove(listener);
	}

	public boolean hasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
		this.hasUnsavedChanges = hasUnsavedChanges;
	}

	public void fireDocumentDirty() {
		for (DocumentListener l : listeners) {
			l.onDocumentDirty();
		}
	}

	public void applyLayout(Layout layout) {
		layout.applyTo(this, width, height);
	}

	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}

		// Setup graphics object.
		graphics.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		// Save original transform.
		AffineTransform originalTransform = graphics.getTransform();
		// Apply document transform.
		// graphics.transform(transform.getAffineTransform());
		graphics.translate(transform.getTranslationX(), transform.getTranslationY());
		graphics.scale(transform.getScale(), transform.getScale());
		// Draw document.
		for (GraphicalElement elem : elements.keySet()) {
			elem.draw(graphics);
		}
		// Restore original transform.
		graphics.setTransform(originalTransform);
	}

	@SuppressWarnings("unchecked")
	protected static <T> T getGraphicalExtension(IExtensible obj) {
		return (T) obj.getExtension(GraphicalElement.EXTENSION_KEY);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Returns a GraphicalElement that covers the given point and has an
	 * associated model element. That means standalone GraphicalElements are
	 * not considered for this check!
	 *
	 * @param point
	 *                test point in model coordinates
	 * @return the GraphicalElement or null
	 */
	public GraphicalElement getGraphicalElementAt(Point point) {
		for (GraphicalElement elem : elements.keySet()) {
			if (elem.coversPoint(point) && elements.get(elem) != null) {
				return elem;
			}
		}
		return null;
	}

	/**
	 * Returns the model element whose associated GraphicalElement covers
	 * the given point or null if there is not element at that position.
	 *
	 * @param point
	 *                test point in model coordinates
	 * @return the model element or null
	 */
	@SuppressWarnings("unchecked")
	public <U> U getModelElementAt(Point point) {
		for (GraphicalElement elem : elements.keySet()) {
			if (elem.coversPoint(point)) {
				return (U) elements.get(elem);
			}
		}
		return null;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
