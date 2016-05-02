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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.module.Category;
import uniol.apt.module.Module;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.events.ModuleExecutedEvent;
import uniol.aptgui.mainwindow.WindowId;

public class MenuPresenterImpl extends AbstractPresenter<MenuPresenter, MenuView> implements MenuPresenter {

	private static final int MAX_RECENTLY_USED = 5;

	private final Application app;
	private final Set<Category> categoriesFilter;
	private final List<Module> recentlyUsedModules;

	@Inject
	public MenuPresenterImpl(Application app, EventBus eventBus, MenuView view) {
		super(view);
		this.app = app;
		eventBus.register(this);
		recentlyUsedModules = new ArrayList<>();
		categoriesFilter = new HashSet<>();
		categoriesFilter.add(Category.PN);
		categoriesFilter.add(Category.LTS);
		categoriesFilter.add(Category.GENERATOR);
	}

	@Subscribe
	public void onModuleExecutedEvent(ModuleExecutedEvent e) {
		Module module = e.getModule();
		if (recentlyUsedModules.contains(module)) {
			// Remove module from the middle of the list if it
			// already exists in the list.
			recentlyUsedModules.remove(module);
		} else if (recentlyUsedModules.size() >= MAX_RECENTLY_USED) {
			// Remove last module if the list is full.
			recentlyUsedModules.remove(recentlyUsedModules.size() - 1);
		}
		// Put module at the front of the list.
		recentlyUsedModules.add(0, module);
		// Show new module list.
		view.setRecentlyUsedModule(app, recentlyUsedModules);
	}

	@Override
	public void setInternalWindows(Set<WindowId> windows) {
		List<WindowId> sortedWindows = new ArrayList<>(windows);
		Collections.sort(sortedWindows, new Comparator<WindowId>() {
			@Override
			public int compare(WindowId o1, WindowId o2) {
				return o1.getType().ordinal() - o2.getType().ordinal();
			}
		});
		view.setWindows(app, sortedWindows);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
