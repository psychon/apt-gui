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

package uniol.aptgui.io;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;

import uniol.apt.io.renderer.RenderException;
import uniol.apt.io.renderer.impl.AptLTSRenderer;
import uniol.apt.io.renderer.impl.AptPNRenderer;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;
import uniol.aptgui.editor.document.graphical.GraphicalElement;
import uniol.aptgui.editor.document.graphical.edges.GraphicalEdge;
import uniol.aptgui.editor.document.graphical.nodes.GraphicalNode;

/**
 * Class that handles writing a Document to a file.
 */
public class AptDocumentRenderer implements DocumentRenderer {

	@Override
	public void render(Document<?> document, File file) throws RenderException, IOException {
		// TODO re-enable rendering of persistent model extensions
		// document.renderPersistentModelExtensions(transformer);
		if (document instanceof PnDocument) {
			renderPetriNet((PnDocument) document, file);
		} else if (document instanceof TsDocument) {
			renderTransitionSystem((TsDocument) document, file);
		} else {
			assert false;
		}
	}

	private void renderPetriNet(PnDocument document, File file) throws RenderException, IOException {
		AptPNRenderer renderer = new AptPNRenderer();
		renderer.renderFile(document.getModel(), file);
	}

	private void renderTransitionSystem(TsDocument document, File file) throws RenderException, IOException {
		AptLTSRenderer renderer = new AptLTSRenderer();
		renderer.renderFile(document.getModel(), file);
	}

	/**
	 * Creates a string-representation of a GraphicalElement that can be
	 * saved to the APT file. When the file is re-openend this string can be
	 * parsed to restore all properties of this GraphicalElement.
	 */
	private final Function<GraphicalElement, String> transformer = new Function<GraphicalElement, String>() {

		@Override
		public String apply(GraphicalElement input) {
			List<String> properties = new ArrayList<>();
			String repr;

			if (input instanceof GraphicalNode) {
				properties.add(getPositionRepresentation((GraphicalNode) input));
			} else if (input instanceof GraphicalEdge) {
				repr = getBreakpointsRepresentation((GraphicalEdge) input);
				if (repr != null) {
					properties.add(repr);
				}
			}

			repr = getColorRepresentation(input);
			if (repr != null) {
				properties.add(repr);
			}

			return String.join("; ", properties);
		}

	};

	/**
	 * Returns a string representation of the breakpoint attribute of a
	 * GraphicalEdge or null if there are no breakpoints.
	 */
	private String getBreakpointsRepresentation(GraphicalEdge edge) {
		if (edge.getBreakpointCount() == 0) {
			return null;
		}
		List<String> coordinates = new ArrayList<>();
		for (Point bp : edge.getBreakpoints()) {
			coordinates.add(String.format("%d,%d", bp.x, bp.y));
		}
		return String.format("bp %s", String.join(" ", coordinates));
	}

	/**
	 * Returns a string representation of the position attribute of a
	 * GraphicalNode.
	 */
	private String getPositionRepresentation(GraphicalNode node) {
		return String.format("pos %d,%d", node.getCenter().x, node.getCenter().y);
	}

	/**
	 * Returns a string representation of the color attribute of a
	 * GraphicalElement or null if the element is the default color (black).
	 */
	private String getColorRepresentation(GraphicalElement elem) {
		if (elem.getColor().equals(Color.BLACK)) {
			return null;
		} else {
			return String.format("col %s", colorToRgbString(elem.getColor()));
		}
	}

	/**
	 * Returns the hex RGB representation of the color.
	 *
	 * @param color
	 *                color to represent as a string
	 * @return hex RGB representation, e.g. #12F3A4
	 */
	private static String colorToRgbString(Color color) {
		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
