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

public class Transform2D {

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

	public void translateView(int dx, int dy) {
		translationX += dx;
		translationY += dy;
	}

	public void scaleView(double scale) {
		this.scaleXY = this.scaleXY * scale;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
