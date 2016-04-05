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

package uniol.aptgui.gui.mainwindow.toolbar;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.aptgui.gui.JToolBarView;
import uniol.aptgui.gui.actions.NewPetriNetAction;
import uniol.aptgui.gui.actions.NewTransitionSystemAction;
import uniol.aptgui.gui.actions.PnCreateFlowToolAction;
import uniol.aptgui.gui.actions.PnCreatePlaceToolAction;
import uniol.aptgui.gui.actions.PnSelectionToolAction;
import uniol.aptgui.gui.editor.tools.toolbox.ToolIds;

@SuppressWarnings("serial")
public class ToolbarViewImpl extends JToolBarView<ToolbarPresenter> implements ToolbarView {

	private final Map<ToolIds, JToggleButton> pnToolButtons;
	private final ButtonGroup pnToolGroup;

	private final Map<ToolIds, JToggleButton> tsToolButtons;
	private final ButtonGroup tsToolGroup;

	private Map<ToolIds, JToggleButton> contextToolButtons;
	private ButtonGroup contextToolGroup;

	@Inject
	public ToolbarViewImpl(Injector injector) {
		// Create toolbar items.
		JButton newPn = new JButton(injector.getInstance(NewPetriNetAction.class));
		JButton newTs = new JButton(injector.getInstance(NewTransitionSystemAction.class));
		JToggleButton pnSelectionTool = new JToggleButton(injector.getInstance(PnSelectionToolAction.class));
		JToggleButton pnCreatePlaceTool = new JToggleButton(injector.getInstance(PnCreatePlaceToolAction.class));
		JToggleButton pnCreateFlowTool = new JToggleButton(injector.getInstance(PnCreateFlowToolAction.class));

		// Add items to toolbar.
		add(newPn);
		add(newTs);
		add(pnSelectionTool);
		add(pnCreatePlaceTool);
		add(pnCreateFlowTool);

		// Put PN specific item in their hash map.
		pnToolButtons = new HashMap<>();
		pnToolButtons.put(ToolIds.SELECTION, pnSelectionTool);
		pnToolButtons.put(ToolIds.CREATE_PLACE, pnCreatePlaceTool);
		pnToolButtons.put(ToolIds.CREATE_FLOW, pnCreateFlowTool);
		pnToolGroup = new ButtonGroup();
		for (JToggleButton btn : pnToolButtons.values()) {
			btn.setVisible(false);
			pnToolGroup.add(btn);
		}

		// Put TS specific items in their hash map.
		tsToolButtons = new HashMap<>();
		tsToolButtons.put(ToolIds.SELECTION, new JToggleButton("TODO"));
		tsToolGroup = new ButtonGroup();
		for (JToggleButton btn : tsToolButtons.values()) {
			btn.setVisible(false);
			tsToolGroup.add(btn);
		}

		contextToolButtons = new HashMap<>();
		contextToolGroup = new ButtonGroup();
	}

	@Override
	public void setActiveTool(ToolIds tool) {
		contextToolGroup.clearSelection();
		JToggleButton btn = contextToolButtons.get(tool);
		if (btn != null) {
			btn.doClick();
		}
	}

	@Override
	public void setContext(ToolbarContext context) {
		// hide old toolbar buttons
		for (JToggleButton btn : contextToolButtons.values()) {
			btn.setVisible(false);
		}

		switch (context) {
		case PETRI_NET:
			contextToolButtons = pnToolButtons;
			contextToolGroup = pnToolGroup;
			break;
		case TRANSITION_SYSTEM:
			contextToolButtons = tsToolButtons;
			contextToolGroup = tsToolGroup;
			break;
		default:
			contextToolButtons = new HashMap<>();
			contextToolGroup = new ButtonGroup();
			break;
		}

		// show new toolbar buttons
		for (JToggleButton btn : contextToolButtons.values()) {
			btn.setVisible(true);
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
