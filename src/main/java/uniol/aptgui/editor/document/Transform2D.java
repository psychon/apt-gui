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

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Transform2D {

	/**
	 * Scale factor that gets used for increase/decrease scale methods.
	 */
	private static final double SCALE_FACTOR = 1.1;

	private int translationX = 0;
	private int translationY = 0;
	private double scaleXY = 1.0;

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
		return scaleXY;
	}

	public void setScale(double scale) {
		this.scaleXY = scale;
	}

	/**
	 * Increases the scale by a constant factor multiplied by the given
	 * level.
	 *
	 * @param levels
	 *                amount of "zoom-levels" to zoom in
	 */
	public void increaseScale(int levels) {
		scaleView(SCALE_FACTOR * levels);
	}

	/**
	 * Decreases the scale by a constant factor multiplied by the given
	 * level.
	 *
	 * @param levels
	 *                amount of "zoom-levels" to zoom out
	 */
	public void decreaseScale(int levels) {
		scaleView(levels / SCALE_FACTOR);
	}

	public void translateView(int dx, int dy) {
		translationX += dx;
		translationY += dy;
	}

	public void scaleView(double scale) {
		this.scaleXY = this.scaleXY * scale;
	}

	public AffineTransform getAffineTransform() {
		AffineTransform tx = new AffineTransform();
		tx.translate(translationX, translationY);
		tx.scale(scaleXY, scaleXY);
		return tx;
	}

	/**
	 * Returns a new Point that is calculated by applying the inverse
	 * transform of this Transform2D's parameters.
	 *
	 * @param point
	 *                the point to transform
	 * @return a transformed point
	 */
	public Point applyInverse(Point2D point) {
		try {
			AffineTransform tx = getAffineTransform();
			Point2D res = tx.inverseTransform(point, null);
			return new Point((int) res.getX(), (int) res.getY());
		} catch (Exception e) {
			throw new AssertionError();
		}
	}

	/**
	 * Returns a new Point that is calculated by transforming the given
	 * point with this Transform2D's parameters.
	 *
	 * @param point
	 *                the point to transform
	 * @return a transformed point
	 */
	public Point apply(Point2D point) {
		AffineTransform tx = getAffineTransform();
		Point2D res = tx.transform(point, null);
		return new Point((int) res.getX(), (int) res.getY());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
