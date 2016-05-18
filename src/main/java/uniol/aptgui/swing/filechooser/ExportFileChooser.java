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
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class ExportFileChooser extends JFileChooser {

	private FileFilter epsFilter = new FileNameExtensionFilter("EPS vector image", "eps");
	private FileFilter pngFilter = new FileNameExtensionFilter("PNG raster image", "png");

	public ExportFileChooser() {
		addChoosableFileFilter(epsFilter);
		addChoosableFileFilter(pngFilter);
	}

	public boolean isEpsExport() {
		return getFileFilter() == epsFilter;
	}

	public boolean isPngExport() {
		return getFileFilter() == pngFilter;
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

		if (filter instanceof FileNameExtensionFilter) {
			FileNameExtensionFilter extFilter = (FileNameExtensionFilter) filter;
			if (!extFilter.accept(file)) {
				file = new File(file.getAbsolutePath() + "." + extFilter.getExtensions()[0]);
			}
		}

		return file;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
