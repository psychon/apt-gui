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

package uniol.aptgui.swing;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

public class Resource {

	public static Cursor getCursorCreateEdge() {
		return getCursor("/cursor_create_edge.gif");
	}

	private static Cursor getCursor(String path) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL url = Resource.class.getResource(path);
		Image image = tk.getImage(url);
		return tk.createCustomCursor(image, new Point(0, 0), path);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
