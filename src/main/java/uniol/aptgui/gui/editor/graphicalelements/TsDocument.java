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

import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.gui.editor.layout.Layout;

public class TsDocument extends Document {

	private final TransitionSystem transitionSystem;

	public TsDocument(TransitionSystem transitionSystem) {
		this.transitionSystem = transitionSystem;
	}

	public TransitionSystem getTransitionSystem() {
		return transitionSystem;
	}

	@Override
	public void applyLayout(Layout layout) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void draw(Graphics2D graphics) {
		// TODO Auto-generated method stub

	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
