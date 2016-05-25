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

package uniol.aptgui.editor.features.base;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A FeatureCollection is itself a feature. Internally it keeps a set of
 * features. Every method call to this feature is then replicated on every
 * feature in the internal set. This effectively combines the behaviour of many
 * features into a single one.
 */
public class FeatureCollection extends Feature {

	private final Map<FeatureId, Feature> features;
	private boolean listening;

	public FeatureCollection() {
		features = new HashMap<>();
		listening = false;
	}

	public void put(FeatureId id, Feature feature) {
		features.put(id, feature);
	}

	public Feature get(FeatureId id) {
		return features.get(id);
	}

	public void clear() {
		features.clear();
	}

	protected Collection<Feature> getActiveFeatureSet() {
		return features.values();
	}

	public boolean isListening() {
		return listening;
	}

	public void setListening(boolean listening) {
		this.listening = listening;
	}

	@Override
	public void onActivated() {
		for (Feature feature : getActiveFeatureSet()) {
			feature.onActivated();
		}
	}

	@Override
	public void onDeactivated() {
		for (Feature feature : getActiveFeatureSet()) {
			feature.onDeactivated();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mouseClicked(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mouseReleased(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mouseEntered(e);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mouseExited(e);
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mouseWheelMoved(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mouseDragged(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (!isListening()) {
			return;
		}
		for (Feature feature : getActiveFeatureSet()) {
			feature.mouseMoved(e);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
