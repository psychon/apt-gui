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

package uniol.aptgui.swing.moduletable;

import javax.swing.RowFilter;

import uniol.apt.module.Module;
import uniol.aptgui.modulebrowser.ModuleBrowserPresenter;

/**
 * Row filter that allows filtering by module name and category.
 */
public class ModuleRowFilter extends RowFilter<ModuleTableModel, Integer> {

	private String nameFilter = "";
	private String categoryFilter = "";

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter.trim();
	}

	public void setCategoryFilter(String categoryFilter) {
		if (ModuleBrowserPresenter.ANY_CATEGORY_STRING.equals(categoryFilter)) {
			this.categoryFilter = "";
		} else {
			this.categoryFilter = categoryFilter.trim();
		}
	}

	@Override
	public boolean include(RowFilter.Entry<? extends ModuleTableModel, ? extends Integer> entry) {
		ModuleTableModel model = entry.getModel();
		Module module = model.getModuleAt(entry.getIdentifier());

		if (nameFilter.isEmpty() && categoryFilter.isEmpty()) {
			return true;
		} else {
			return module.getName().contains(nameFilter)
			    && module.getCategories()[0].getName().contains(categoryFilter);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
