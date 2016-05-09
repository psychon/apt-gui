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

package uniol.aptgui.swing.filechooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import uniol.apt.io.parser.impl.AptLTSParser;
import uniol.apt.io.parser.impl.AptPNParser;

@SuppressWarnings("serial")
public class AptFileChooser extends JFileChooser {

	public AptFileChooser() {
		this(true, true);
	}

	public AptFileChooser(boolean allowPn, boolean allowTs) {
		if (allowPn) {
			FileFilter ff = new ParserFileFilter("Petri Net", new AptPNParser());
			addChoosableFileFilter(ff);
			setFileFilter(ff);
		}
		if (allowTs) {
			FileFilter ff = new ParserFileFilter("Transition system", new AptLTSParser());
			addChoosableFileFilter(ff);
			setFileFilter(ff);
		}
		if (allowPn && allowTs) {
			setFileFilter(getAcceptAllFileFilter());
		}
	}

	/**
	 * Returns the selected file while making sure that it has the correct
	 * extension for the selected file type. I.e. the extension is added if
	 * it is not present.
	 *
	 * @return the selected file
	 */
	public File getSelectedFileWithExtension() {
		File file = getSelectedFile();
		FileFilter filter = getFileFilter();

		if (filter instanceof ParserFileFilter) {
			ParserFileFilter pFilter = (ParserFileFilter) filter;
			if (!pFilter.accept(file)) {
				file = new File(file.getAbsolutePath() + "." + pFilter.getDefaultExtension());
			}
		}

		return file;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
