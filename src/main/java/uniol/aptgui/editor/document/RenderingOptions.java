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

/**
 * Saves additional information used during the document drawing process.
 */
public class RenderingOptions {

	public static final RenderingOptions DEFAULT = new RenderingOptions();

	private boolean stateIdLabelVisible = true;
	private boolean placeIdLabelVisible = true;
	private boolean transitionIdLabelVisible = true;

	public boolean isStateIdLabelVisible() {
		return stateIdLabelVisible;
	}

	public void setStateIdLabelVisible(boolean stateIdLabelVisible) {
		this.stateIdLabelVisible = stateIdLabelVisible;
	}

	public boolean toggleStateIdLabelVisible() {
		stateIdLabelVisible = !stateIdLabelVisible;
		return stateIdLabelVisible;
	}

	public boolean isPlaceIdLabelVisible() {
		return placeIdLabelVisible;
	}

	public void setPlaceIdLabelVisible(boolean placeIdLabelVisible) {
		this.placeIdLabelVisible = placeIdLabelVisible;
	}

	public boolean togglePlaceIdLabelVisible() {
		placeIdLabelVisible = !placeIdLabelVisible;
		return placeIdLabelVisible;
	}

	public boolean isTransitionIdLabelVisible() {
		return transitionIdLabelVisible;
	}

	public void setTransitionIdLabelVisible(boolean transitionIdLabelVisible) {
		this.transitionIdLabelVisible = transitionIdLabelVisible;
	}

	public boolean toggleTransitionIdLabelVisible() {
		transitionIdLabelVisible = !transitionIdLabelVisible;
		return transitionIdLabelVisible;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
