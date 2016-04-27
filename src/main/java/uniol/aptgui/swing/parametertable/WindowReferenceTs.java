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

package uniol.aptgui.swing.parametertable;

import uniol.apt.adt.ts.TransitionSystem;
import uniol.aptgui.mainwindow.WindowId;

public class WindowReferenceTs extends WindowReference {

	private final TransitionSystem transitionSystem;

	public WindowReferenceTs(WindowId id, String name, TransitionSystem transitionSystem) {
		super(id, name);
		this.transitionSystem = transitionSystem;
	}

	public WindowReferenceTs(WindowId id, String name) {
		this(id, name, null);
	}

	public WindowReferenceTs(WindowId id) {
		this(id, "", null);
	}

	public TransitionSystem getTransitionSystem() {
		return transitionSystem;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
