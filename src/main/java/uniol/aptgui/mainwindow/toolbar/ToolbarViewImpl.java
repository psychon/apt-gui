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

package uniol.aptgui.mainwindow.toolbar;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import com.google.inject.Inject;
import com.google.inject.Injector;

import uniol.aptgui.editor.tools.Tool;
import uniol.aptgui.swing.JToolBarView;
import uniol.aptgui.swing.actions.NewPetriNetAction;
import uniol.aptgui.swing.actions.NewTransitionSystemAction;
import uniol.aptgui.swing.actions.PnCreateFlowToolAction;
import uniol.aptgui.swing.actions.PnCreatePlaceToolAction;
import uniol.aptgui.swing.actions.PnSelectionToolAction;
import uniol.aptgui.swing.actions.TsSelectionToolAction;

@SuppressWarnings("serial")
public class ToolbarViewImpl extends JToolBarView<ToolbarPresenter> implements ToolbarView {

	// GENERAL BUTTONS
	private final JButton newPetriNet;
	private final JButton newTransitionSystem;

	// PN BUTTONS
	private final ButtonGroup pnToolGroup;
	private final JToggleButton pnSelectionTool;
	private final JToggleButton pnCreatePlaceTool;
	private final JToggleButton pnCreateFlowTool;

	// TS BUTTONS
	private final ButtonGroup tsToolGroup;
	private final JToggleButton tsSelectionTool;

	@Inject
	public ToolbarViewImpl(Injector injector) {
		setFloatable(false);

		// GENERAL BUTTONS
		newPetriNet = new JButton(injector.getInstance(NewPetriNetAction.class));
		newTransitionSystem = new JButton(injector.getInstance(NewTransitionSystemAction.class));

		add(newPetriNet);
		add(newTransitionSystem);

		// PN BUTTONS
		pnToolGroup = new ButtonGroup();
		pnSelectionTool = new JToggleButton(injector.getInstance(PnSelectionToolAction.class));
		pnCreatePlaceTool = new JToggleButton(injector.getInstance(PnCreatePlaceToolAction.class));
		pnCreateFlowTool = new JToggleButton(injector.getInstance(PnCreateFlowToolAction.class));

		addToToolbarAndGroup(pnSelectionTool, pnToolGroup);
		addToToolbarAndGroup(pnCreatePlaceTool, pnToolGroup);
		addToToolbarAndGroup(pnCreateFlowTool, pnToolGroup);

		// TS BUTTONS
		tsToolGroup = new ButtonGroup();
		tsSelectionTool = new JToggleButton(injector.getInstance(TsSelectionToolAction.class));

		addToToolbarAndGroup(tsSelectionTool, tsToolGroup);
	}

	private void addToToolbarAndGroup(AbstractButton button, ButtonGroup group) {
		add(button);
		group.add(button);
	}

	@Override
	public void setPetriNetToolsVisible(boolean visible) {
		pnSelectionTool.setVisible(visible);
		pnCreatePlaceTool.setVisible(visible);
		pnCreateFlowTool.setVisible(visible);
	}

	@Override
	public void setTransitionSystemToolsVisible(boolean visible) {
		tsSelectionTool.setVisible(visible);
	}

	@Override
	public void setActiveTool(Tool tool) {
		switch (tool) {
		case PN_CREATE_FLOW:
			pnCreateFlowTool.doClick();
			break;
		case PN_CREATE_PLACE:
			pnCreatePlaceTool.doClick();
			break;
		case PN_CREATE_TRANSITION:
			break;
		case PN_SELECTION:
			pnSelectionTool.doClick();
			break;
		case TS_CREATE_ARC:
			break;
		case TS_CREATE_STATE:
			break;
		case TS_SELECTION:
			tsSelectionTool.doClick();
			break;
		default:
			break;

		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
