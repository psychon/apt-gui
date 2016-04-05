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

package uniol.aptgui.gui.editor.graphicalelements;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import uniol.apt.adt.extension.IExtensible;
import uniol.aptgui.gui.editor.layout.Layout;

public abstract class Document {

	protected int width;
	protected int height;

	protected int translationX = 0;
	protected int translationY = 0;
	protected double scale = 1.0;

	public Document() {
		this(400, 300);
	}

	public Document(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public abstract void applyLayout(Layout layout);

	public void drawDocument(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform originalTransform = graphics.getTransform();
		graphics.translate(translationX, translationY);
		graphics.scale(scale, scale);
		draw(graphics);
		graphics.setTransform(originalTransform);
	}

	protected abstract void draw(Graphics2D graphics);

	@SuppressWarnings("unchecked")
	protected static <T> T getGraphicalExtension(IExtensible obj) {
		return (T) obj.getExtension(GraphicalElement.EXTENSION_KEY);
	}

	public void translateView(int dx, int dy) {
		translationX += dx;
		translationY += dy;
	}

	public void scaleView(double scale) {
		this.scale = this.scale * scale;
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

	public int getTranslationX() {
		return translationX;
	}

	public void setTranslationX(int translationX) {
		this.translationX = translationX;
	}

	public int getTranslationY() {
		return translationY;
	}

	public void setTranslationY(int translationY) {
		this.translationY = translationY;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
