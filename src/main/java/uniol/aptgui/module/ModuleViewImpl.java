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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;

import uniol.aptgui.swing.JPanelView;
import uniol.aptgui.swing.parametertable.PropertyTable;
import uniol.aptgui.swing.parametertable.PropertyTableModel;
import uniol.aptgui.swing.parametertable.WindowRefProvider;

@SuppressWarnings("serial")
public class ModuleViewImpl extends JPanelView<ModulePresenter> implements ModuleView {

	private static final int WIDTH = 250;
	private static final int HEIGHT = 350;

	private JEditorPane descriptionPane;

	private JTabbedPane tabbedPane;
	private JPanel parametersContainer;
	private PropertyTable parametersTable;
	private JPanel resultsContainer;
	private PropertyTable resultsTable;

	private JPanel bottomPanel;
	private JButton runButton;

	public ModuleViewImpl() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setupDescriptionPane();
		setupTabbedPane();
		setupBottomPane();
	}

	private void setupDescriptionPane() {
		descriptionPane = new JEditorPane();
		descriptionPane.setEditable(false);
		JScrollPane editorScrollPane = new JScrollPane(descriptionPane);
		editorScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		editorScrollPane.setPreferredSize(new Dimension(WIDTH, 100));
		add(editorScrollPane, BorderLayout.PAGE_START);
	}

	private void setupParametersContainer() {
		parametersTable = new PropertyTable();
		parametersTable.setFillsViewportHeight(true);

		parametersContainer = new JPanel();
		parametersContainer.setLayout(new BorderLayout());
		parametersContainer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		parametersContainer.add(new JScrollPane(parametersTable), BorderLayout.CENTER);
	}

	private void setupResultsContainer() {
		resultsTable = new PropertyTable();
		resultsTable.setFillsViewportHeight(true);

		resultsContainer = new JPanel();
		resultsContainer.setLayout(new BorderLayout());
		resultsContainer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		resultsContainer.add(new JScrollPane(resultsTable), BorderLayout.CENTER);
	}

	private void setupTabbedPane() {
		setupParametersContainer();
		setupResultsContainer();

		tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		tabbedPane.addTab("Parameters", parametersContainer);
		tabbedPane.addTab("Results", resultsContainer);

		add(tabbedPane, BorderLayout.CENTER);
	}

	private void setupBottomPane() {
		runButton = new JButton("Run");
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPresenter().onRunButtonClicked();
			}
		});
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(runButton);

		add(bottomPanel, BorderLayout.PAGE_END);
	}

	@Override
	public void setDescription(String description) {
		descriptionPane.setText(description);
	}

	@Override
	public void setParameterTableModel(PropertyTableModel model) {
		parametersTable.setModel(model);
	}

	@Override
	public void showErrorTooFewParameters() {
		JOptionPane.showMessageDialog(this, "Too few parameters supplied.", "Cannot run module",
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void showErrorModuleException(String message) {
		JOptionPane.showMessageDialog(this, message, "Module exception", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void setResultTableModel(PropertyTableModel resultTableModel) {
		resultsTable.setModel(resultTableModel);
	}

	@Override
	public void setPetriNetWindowRefProvider(WindowRefProvider refProvider) {
		parametersTable.setPetriNetWindowRefProvider(refProvider);
	}

	@Override
	public void setTransitionSystemWindowRefProvider(WindowRefProvider refProvider) {
		parametersTable.setTransitionSystemWindowRefProvider(refProvider);
	}

	@Override
	public void showResultsPane() {
		tabbedPane.setSelectedIndex(1);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
