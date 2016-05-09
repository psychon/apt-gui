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

package uniol.aptgui.modulebrowser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Inject;

import uniol.apt.module.Module;
import uniol.apt.module.ModuleRegistry;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.swing.moduletable.ModuleTableModel;

public class ModuleBrowserPresenterImpl extends AbstractPresenter<ModuleBrowserPresenter, ModuleBrowserView>
		implements ModuleBrowserPresenter {

	private final Application application;
	private final ModuleTableModel moduleTableModel;

	@Inject
	public ModuleBrowserPresenterImpl(ModuleBrowserView view, ModuleRegistry moduleRegistry,
			Application application) {
		super(view);
		this.application = application;
		this.moduleTableModel = createTableModel(moduleRegistry.getModules());

		view.setModuleTableModel(moduleTableModel);
		view.setCategoryFilters(getCategoryStrings(moduleRegistry.getModules()));
	}

	private ModuleTableModel createTableModel(Collection<Module> moduleCollection) {
		List<Module> moduleList = new ArrayList<>(moduleCollection);
		Collections.sort(moduleList, new Comparator<Module>() {
			@Override
			public int compare(Module o1, Module o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return new ModuleTableModel(moduleList);
	}

	private List<String> getCategoryStrings(Collection<Module> moduleCollection) {
		List<String> categories = new ArrayList<>();
		categories.add(ANY_CATEGORY_STRING);
		for (Module mod : moduleCollection) {
			String cat = mod.getCategories()[0].getName();
			if (!categories.contains(cat)) {
				categories.add(cat);
			}
		}
		return categories;
	}

	@Override
	public void onModuleRequestOpen(int tableRow) {
		Module mod = moduleTableModel.getModuleAt(tableRow);
		application.openModule(mod);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
