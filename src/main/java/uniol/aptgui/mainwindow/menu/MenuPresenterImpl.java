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

package uniol.aptgui.mainwindow.menu;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import uniol.apt.module.Category;
import uniol.apt.module.Module;
import uniol.aptgui.AbstractPresenter;

public class MenuPresenterImpl extends AbstractPresenter<MenuPresenter, MenuView> implements MenuPresenter {

	private final Set<Category> categoriesFilter;

	@Inject
	public MenuPresenterImpl(MenuView view) {
		super(view);
		categoriesFilter = new HashSet<>();
		categoriesFilter.add(Category.PN);
		categoriesFilter.add(Category.LTS);
		categoriesFilter.add(Category.GENERATOR);
	}

	@Override
	public void setRecentlyUsedModule(Module module) {
		for (Category cat : module.getCategories()) {
			if (categoriesFilter.contains(cat)) {
				view.setRecentlyUsedModule(module);
			}
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
