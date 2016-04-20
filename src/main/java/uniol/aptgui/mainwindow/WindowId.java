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

package uniol.aptgui.mainwindow;

public class WindowId {

	private static int nextId = 0;

	private final int id;
	private final WindowType type;

	public WindowId(WindowType type) {
		this(getNextId(), type);
	}

	private WindowId(int id, WindowType type) {
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public WindowType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "WindowId [id=" + id + ", type=" + type + "]";
	}

	private static synchronized int getNextId() {
		return nextId++;
	}

	public String toStringWithTitle(String name) {
		if (name.trim().isEmpty()) {
			return "$" + id;
		} else {
			return name + "($" + id + ")";
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
