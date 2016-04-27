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

import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import uniol.apt.adt.pn.PetriNet;
import uniol.apt.adt.ts.TransitionSystem;
import uniol.apt.module.Module;
import uniol.apt.module.exception.ModuleException;
import uniol.apt.module.impl.ModuleInvoker;
import uniol.apt.module.impl.ModuleUtils;
import uniol.apt.module.impl.Parameter;
import uniol.apt.module.impl.ReturnValue;
import uniol.aptgui.AbstractPresenter;
import uniol.aptgui.Application;
import uniol.aptgui.events.ModuleExecutedEvent;
import uniol.aptgui.mainwindow.WindowId;
import uniol.aptgui.swing.parametertable.ParameterTableModel;
import uniol.aptgui.swing.parametertable.ResultTableModel;
import uniol.aptgui.swing.parametertable.WindowReferencePn;
import uniol.aptgui.swing.parametertable.WindowReferenceTs;

public class ModulePresenterImpl extends AbstractPresenter<ModulePresenter, ModuleView> implements ModulePresenter {

	private final Application application;

	private Module module;
	private List<Parameter> parameters;
	private List<Parameter> allParameters;
	private List<ReturnValue> returnValues;

	private ParameterTableModel parameterTableModel;
	private ResultTableModel resultTableModel;

	@Inject
	public ModulePresenterImpl(ModuleView view, Application application) {
		super(view);
		this.application = application;
		application.getEventBus().register(this);
	}

	@Override
	public void setModule(Module module) {
		this.module = module;
		parameters = ModuleUtils.getParameters(module);
		allParameters = ModuleUtils.getAllParameters(module);
		returnValues = ModuleUtils.getReturnValues(module);

		parameterTableModel = new ParameterTableModel(allParameters);
		view.setDescription(module.getLongDescription());
		view.setParameterTableModel(parameterTableModel);
	}

	@Override
	public void onRunButtonClicked() {
		Object[] paramValues = parameterTableModel.getParameterValues();

		if (paramValues.length < parameters.size()) {
			view.showErrorTooFewParameters();
			return;
		}

		try {
			ModuleInvoker invoker = new ModuleInvoker();
			List<Object> filledReturnValues = invoker.invoke(module, paramValues);
			application.getEventBus().post(new ModuleExecutedEvent(module, filledReturnValues));
		} catch (ModuleException e) {
			view.showErrorModuleException(e.getMessage());
		}
	}

	@Subscribe
	public void onModuleExecutedEvent(ModuleExecutedEvent e) {
		List<Object> filledReturnValues = e.getReturnValues();
		for (int i = 0; i < filledReturnValues.size(); i++) {
			Object retVal = filledReturnValues.get(i);
			if (retVal.getClass().equals(PetriNet.class)) {
				PetriNet pn = (PetriNet) retVal;
				WindowId id = application.openPetriNet(pn);
				WindowReferencePn ref = new WindowReferencePn(id);
				filledReturnValues.set(i, ref);
			} else if (retVal.getClass().equals(TransitionSystem.class)) {
				TransitionSystem ts = (TransitionSystem) retVal;
				WindowId id = application.openTransitionSystem(ts);
				WindowReferenceTs ref = new WindowReferenceTs(id);
				filledReturnValues.set(i, ref);
			}
		}

		resultTableModel = new ResultTableModel(returnValues, filledReturnValues);
		view.setResultTableModel(resultTableModel);
	}

}


// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
