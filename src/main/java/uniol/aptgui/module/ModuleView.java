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

package uniol.aptgui.module;

import uniol.aptgui.View;
import uniol.aptgui.swing.parametertable.ParameterTableModel;
import uniol.aptgui.swing.parametertable.ResultTableModel;

public interface ModuleView extends View<ModulePresenter> {

	void setDescription(String description);

	void setParameterTableModel(ParameterTableModel model);

	void setResultTableModel(ResultTableModel resultTableModel);

	void showErrorTooFewParameters();

	void showErrorModuleException(String message);

	/**
	 * Causes the parameter drop-down boxes which allow to select PN/LTS
	 * windows to be invalidated. This means the set of available windows is
	 * refreshed.
	 */
	void invalidateWindowDropdowns();

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
