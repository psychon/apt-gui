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

package uniol.aptgui.events;

import java.util.List;

import uniol.apt.module.Module;
import uniol.aptgui.module.ModulePresenter;

public class ModuleExecutedEvent {

	private final ModulePresenter source;
	private final Module module;
	private final List<Object> returnValues;

	public ModuleExecutedEvent(ModulePresenter source, Module module, List<Object> returnValues) {
		this.source = source;
		this.module = module;
		this.returnValues = returnValues;
	}

	public ModulePresenter getSource() {
		return source;
	}

	public Module getModule() {
		return module;
	}

	public List<Object> getReturnValues() {
		return returnValues;
	}

	@Override
	public String toString() {
		return "ModuleExecutedEvent [module=" + module + ", returnValues=" + returnValues + "]";
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
