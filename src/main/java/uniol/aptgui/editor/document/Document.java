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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uniol.apt.adt.extension.IExtensible;
import uniol.apt.adt.pn.Flow;
import uniol.apt.adt.pn.Node;
import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.pn.Place;
import uniol.apt.adt.pn.Transition;
import uniol.apt.adt.ts.Arc;
import uniol.apt.adt.ts.State;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.editor.layout.Layout;

public class Document<T> {

	private List<DocumentListener> listeners;

	/**
	 * Underlying model element. Either PetriNet or TransitionSystem.
	 */
	private T model;

	/**
	 * Map from GraphicalElements to their model objects of undetermined
	 * type. The model object may be null if there is no corresponding model
	 * object.
	 */
	private Map<GraphicalElement, Object> elements;

	protected String title;
	protected boolean hasUnsavedChanges;
	protected int width;
	protected int height;
	protected boolean visible;

	private Transform2D transform;

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

	public void drawDocument(Graphics2D graphics) {
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
		graphics.translate(transform.getTranslationX(), transform.getTranslationY());
		graphics.scale(transform.getScale(), transform.getScale());
		// Draw document.
		for (GraphicalElement elem : elements.keySet()) {
			elem.draw(graphics);
		}
		// Restore original transform.
		graphics.setTransform(originalTransform);
	}

	/**
	 * Transforms the given point (in view coordinates) into
	 * model coordinates.
	 *
	 * @param point
	 *                the original point in view coordinates
	 * @return the transformed point in model coordinates
	 */
	public Point transformViewToModel(Point point) {
		try {
			AffineTransform tx = new AffineTransform();
			tx.translate(transform.getTranslationX(), transform.getTranslationY());
			tx.scale(transform.getScale(), transform.getScale());
			Point2D p2d = tx.inverseTransform(point, null);
			return point2DtoPoint(p2d);
		} catch (Exception e) {
			throw new AssertionError();
		}
	}

	/**
	 * Transforms the given point (in model coordinates) into
	 * view coordinates.
	 *
	 * @param point
	 *                the original point in model coordinates
	 * @return the transformed point in view coordinates
	 */
	public Point transformModelToView(Point point) {
		AffineTransform tx = new AffineTransform();
		tx.translate(transform.getTranslationX(), transform.getTranslationY());
		tx.scale(transform.getScale(), transform.getScale());
		Point2D p2d = tx.transform(point, null);
		return point2DtoPoint(p2d);
	}

	private Point point2DtoPoint(Point2D p2d) {
		return new Point((int) p2d.getX(), (int) p2d.getY());
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
	 * Returns the GraphicalElement that covers the given point or null if
	 * there is no element at that position.
	 *
	 * @param point point in model coordinates
	 * @return
	 */
	public GraphicalElement getGraphicalElementAt(Point point) {
		for (GraphicalElement elem : elements.keySet()) {
			if (elem.containsPoint(point)) {
				return elem;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <U> U getModelElementAt(Point point) {
		for (GraphicalElement elem : elements.keySet()) {
			if (elem.containsPoint(point)) {
				return (U) elements.get(elem);
			}
		}
		return null;
	}

	public GraphicalElement getGraphialElementAtViewCoordinates(Point cursor) {
		return getGraphicalElementAt(transformViewToModel(cursor));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
