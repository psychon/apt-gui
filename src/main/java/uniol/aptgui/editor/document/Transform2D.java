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
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Transform2D encapsulates translation and scaling operations and allows to
 * easily apply them to points.
 */
public class Transform2D {

	/**
	 * Scale factor that gets used for increase/decrease scale methods.
	 */
	private static final double SCALE_FACTOR = 1.1;

	/**
	 * Translation in x-axis direction.
	 */
	private int translationX = 0;

	/**
	 * Translation in y-axis direction.
	 */
	private int translationY = 0;

	/**
	 * Scale in both the x and y dimension.
	 */
	private double scaleXY = 1.0;

	/**
	 * Returns the translation in x-axis direction.
	 *
	 * @return the translation in x-axis direction
	 */
	public int getTranslationX() {
		return translationX;
	}

	/**
	 * Sets the translation in x-axis direction.
	 *
	 * @param translationX
	 *                the new total translation in x-axis direction
	 */
	public void setTranslationX(int translationX) {
		this.translationX = translationX;
	}

	/**
	 * Returns the translation in y-axis direction.
	 *
	 * @return the translation in y-axis direction
	 */
	public int getTranslationY() {
		return translationY;
	}

	/**
	 * Sets the translation in y-axis direction.
	 *
	 * @param translationY
	 *                the new total translation in y-axis direction
	 */
	public void setTranslationY(int translationY) {
		this.translationY = translationY;
	}

	/**
	 * Returns the scale in both x and y dimension.
	 *
	 * @return the scale in both x and y dimension
	 */
	public double getScale() {
		return scaleXY;
	}

	/**
	 * Sets the scale in both x and y dimension.
	 *
	 * @param scale
	 *                the new scale in both x and y dimension
	 */
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

	/**
	 * Translates (moves) the view by the given amounts in x and y direction
	 * respectively.
	 *
	 * @param dx
	 *                translation in x-axis direction
	 * @param dy
	 *                translation in y-axis direction
	 */
	public void translateView(int dx, int dy) {
		translationX += dx;
		translationY += dy;
	}

	/**
	 * Scales the view by the given factor.
	 *
	 * @param scale
	 *                scale factor &gt; 0
	 */
	public void scaleView(double scale) {
		assert scale > 0;
		this.scaleXY = this.scaleXY * scale;
	}

	/**
	 * Returns an AffineTransform object that specifies the same transform
	 * as this object.
	 *
	 * @return an AffineTransform object that specifies the same transform
	 *         as this object
	 */
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
		} catch (NoninvertibleTransformException e) {
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
