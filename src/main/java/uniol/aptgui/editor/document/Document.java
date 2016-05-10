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

/**
 * <p>
 * Document is the main model class. It is parameterized with the underlying
 * Petri net or transition system type. A mapping of GraphicalElements to model
 * elements and back is maintained. Furthermore administrative properties such
 * as file location are contained.
 * </p>
 *
 * <p>
 * Listeners can be informed about changes to visual properties or even
 * structural changes by calling the corresponding <code>fire*</code> method.
 * </p>
 *
 * <p>
 * The specific implementations for Petri nets or transition systems are
 * {@link PnDocument} and {@link TsDocument}.
 * </p>
 *
 * @param <T>
 *                model type; should be PetriNet or TransitionSystem
 */
public abstract class Document<T> {

	/**
	 * List of DocumentListeners.
	 */
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

	/**
	 * Selection object that saves which elements are selected by the user.
	 */
	private final Selection selection;

	/**
	 * Name of the document.
	 */
	protected String name;

	/**
	 * File location of this document. The document will be saved there.
	 */
	protected File file;

	/**
	 * Flag that indicates if this document has unsaved changes.
	 */
	protected boolean hasUnsavedChanges;

	/**
	 * Width of the document area.
	 */
	protected int width;

	/**
	 * Height of the document area.
	 */
	protected int height;

	/**
	 * Flag that indicates visibility of the document to the user.
	 */
	protected boolean visible;

	/**
	 * Transform object that saves translation and scaling.
	 */
	private final Transform2D transform;

	/**
	 * Creates a document with a default size.
	 */
	public Document() {
		this(400, 300);
	}

	/**
	 * Creates a document.
	 *
	 * @param width
	 *                width of the new document
	 * @param height
	 *                height of the new document
	 */
	public Document(int width, int height) {
		this.listeners = new ArrayList<>();
		this.width = width;
		this.height = height;
		this.visible = false;
		this.hasUnsavedChanges = true;
		this.transform = new Transform2D();
		this.elements = new HashMap<>();
		this.selection = new Selection();
	}

	/**
	 * Returns a set of all selected elements.
	 *
	 * @return
	 */
	public Set<GraphicalElement> getSelection() {
		return selection.getSelection();
	}

	/**
	 * Returns the most specific base class that all selected elements are
	 * assignable to.
	 *
	 * @return common base class of all selected elements
	 */
	public Class<? extends GraphicalElement> getSelectionCommonBaseClass() {
		return selection.getCommonBaseClass();
	}

	/**
	 * Toggles selection status on the element. If it was previously
	 * unselected, it will be selected afterwards and the other way around.
	 *
	 * @param elem
	 */
	public void toggleSelection(GraphicalElement elem) {
		selection.toggleSelection(elem);
	}

	/**
	 * Adds the given element to the selection.
	 *
	 * @param elem
	 *                newly selected element
	 */
	public void addToSelection(GraphicalElement elem) {
		selection.addToSelection(elem);
	}

	/**
	 * Removes the given element from the selection.
	 *
	 * @param elem
	 *                the element to unselect
	 */
	public void removeFromSelection(GraphicalElement elem) {
		selection.removeFromSelection(elem);
	}

	/**
	 * Clears the current selection.
	 */
	public void clearSelection() {
		selection.clearSelection();
	}

	/**
	 * Returns the file that this document was last saved to.
	 *
	 * @return the file that this document was last saved to
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the file that this document will be saved to.
	 *
	 * @param file
	 *                file that this document will be saved to
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Returns the underlying model object.
	 *
	 * @return the underlying model object
	 */
	public T getModel() {
		return model;
	}

	/**
	 * Sets the underlying model object.
	 *
	 * @param model
	 *                new model object
	 */
	public void setModel(T model) {
		this.model = model;
	}

	/**
	 * Adds a new standalone GraphicalElement to this Document, i.e. it has
	 * no associated model element.
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
	public void add(GraphicalElement graphicalElem, IExtensible modelElem) {
		elements.put(graphicalElem, modelElem);
		if (modelElem != null) {
			modelElem.putExtension(GraphicalElement.EXTENSION_KEY, graphicalElem);
		}
	}

	/**
	 * Removes the given GraphicalElement from this Document.
	 *
	 * @param graphicalElem
	 */
	public void remove(GraphicalElement graphicalElem) {
		elements.remove(graphicalElem);
		removeFromSelection(graphicalElem);
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

	/**
	 * Returns this document's transform object.
	 *
	 * @return this document's transform object
	 */
	public Transform2D getTransform() {
		return transform;
	}

	/**
	 * Returns if this document is visible (in an editor).
	 *
	 * @return if this document is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets this document's visibility status.
	 *
	 * @param visible
	 *                new visibility status
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Returns this document's name.
	 *
	 * @return this document's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets this document's name.
	 *
	 * @param name
	 *                new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds a document listener.
	 *
	 * @param listener
	 *                listener to add
	 */
	public void addListener(DocumentListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a document listener.
	 *
	 * @param listener
	 *                listener to remove
	 */
	public void removeListener(DocumentListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Returns if this document has unsaved changes.
	 *
	 * @return if this document has unsaved changes
	 */
	public boolean hasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	/**
	 * Sets if this document has unsaved changes.
	 *
	 * @param hasUnsavedChanges
	 */
	public void setHasUnsavedChanges(boolean hasUnsavedChanges) {
		this.hasUnsavedChanges = hasUnsavedChanges;
	}

	/**
	 * Calls onDocumentDirty for every listener.
	 */
	public void fireDocumentDirty() {
		for (DocumentListener l : listeners) {
			l.onDocumentDirty();
		}
	}

	/**
	 * Sets hasUnsavedChanges to the given value and then calls
	 * onDocumentChanged for every listener.
	 *
	 * @param saveNecessary
	 *                true, if this change can be saved to a file
	 */
	public void fireDocumentChanged(boolean saveNecessary) {
		setHasUnsavedChanges(saveNecessary);
		for (DocumentListener l : listeners) {
			l.onDocumentChanged();
		}
	}

	/**
	 * Calls onSelectionChanged for every listener.
	 */
	public void fireSelectionChanged() {
		for (DocumentListener l : listeners) {
			l.onSelectionChanged(this);
		}
	}

	/**
	 * Applies the given layout to this document.
	 *
	 * @param layout
	 *                layout to apply
	 */
	public void applyLayout(Layout layout) {
		layout.applyTo(this);
	}

	/**
	 * Draw this document with the given Graphics2D object. Before any
	 * GraphicalElements are called to draw themselves the transform is
	 * applied to the graphics object.
	 *
	 * @param graphics
	 */
	public void draw(Graphics2D graphics) {
		if (!visible) {
			return;
		}

		// Setup graphics object.
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

	/**
	 * Returns the GraphicalElement associated with the given model element.
	 *
	 * @param obj
	 *                model element
	 * @return associated graphical element
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getGraphicalExtension(IExtensible obj) {
		return (T) obj.getExtension(GraphicalElement.EXTENSION_KEY);
	}

	/**
	 * Returns this document's width.
	 *
	 * @return this document's width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets this document's width.
	 *
	 * @param width
	 *                new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Returns this document's height.
	 *
	 * @return this document's height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets this document's height.
	 *
	 * @param height
	 *                new height
	 */
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
