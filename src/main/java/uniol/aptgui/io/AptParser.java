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

import java.io.File;
import java.io.IOException;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.io.parser.ParseException;
import uniol.apt.io.parser.impl.AptLTSParser;
import uniol.apt.io.parser.impl.AptPNParser;
import uniol.aptgui.editor.document.Document;
import uniol.aptgui.editor.document.PnDocument;
import uniol.aptgui.editor.document.TsDocument;

public class AptParser {

	private File file;
	private PetriNet petriNet;
	private TransitionSystem transitionSystem;

	public Document<?> parse(File file)  throws ParseException, IOException {
		this.file = file;
		if (!tryParsePetriNet() && !tryParseTransitionSystem()) {
			throw new ParseException("The specified file is neither a Petri net nor a transition system in APT-format.");
		}
		assert (petriNet != null && transitionSystem == null)
		    || (petriNet == null && transitionSystem != null);

		Document<?> result = null;
		if (petriNet != null) {
			result = new PnDocument(petriNet);
		} else if (transitionSystem != null) {
			result = new TsDocument(transitionSystem);
		}
		result.setFile(file);
		return result;
	}

	private boolean tryParsePetriNet() throws IOException {
		AptPNParser parser = new AptPNParser();
		try {
			petriNet = parser.parseFile(file);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean tryParseTransitionSystem() throws IOException {
		AptLTSParser parser = new AptLTSParser();
		try {
			transitionSystem = parser.parseFile(file);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
