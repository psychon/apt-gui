/*
 * Copyright (C) 2008-2010 Martin Riesz <riesz.martin at gmail.com>
 *               2016 Jonas Prellberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uniol.aptgui.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import uniol.aptgui.events.HistoryChangedEvent;

/**
 * History class that allows to undo and redo commands.
 */
public class History {

	private final EventBus eventBus;

	private List<Command> executedCommands = new ArrayList<Command>();
	private int currentCommandIndex = -1;

	@Inject
	public History(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Executes the given command.
	 *
	 * @param command
	 *                command to be executed
	 */
	public void execute(Command command) {
		command.execute();

		// Only modify history if the command can be undone.
		if (command.canUndo()) {
			List<Command> nonRedoneCommandsView = executedCommands.subList(
				currentCommandIndex + 1,
				executedCommands.size()
			);
			List<Command> nonRedoedCommands = new ArrayList<>(nonRedoneCommandsView);
			executedCommands.removeAll(nonRedoedCommands);
			executedCommands.add(command);
			currentCommandIndex = executedCommands.size() - 1;
			eventBus.post(new HistoryChangedEvent(this));
		}
	}

	/**
	 * Performs the undo action.
	 */
	public void undo() {
		if (canUndo()) {
			Command command = executedCommands.get(currentCommandIndex);
			command.undo();
			currentCommandIndex--;
			eventBus.post(new HistoryChangedEvent(this));
		}
	}

	/**
	 * Performs the redo action.
	 */
	public void redo() {
		if (canRedo()) {
			Command command = executedCommands.get(currentCommandIndex + 1);
			command.redo();
			currentCommandIndex++;
			eventBus.post(new HistoryChangedEvent(this));
		}
	}

	/**
	 * Determines if undo is possible.
	 *
	 * @return true if undo action is possible otherwise false
	 */
	public boolean canUndo() {
		return currentCommandIndex != -1;
	}

	/**
	 * Determines if redo is possible.
	 *
	 * @return true if redo action is possible otherwise false
	 */
	public boolean canRedo() {
		return currentCommandIndex < executedCommands.size() - 1;
	}

	/**
	 * Clears the history.
	 */
	public void clear() {
		executedCommands = new ArrayList<Command>();
		currentCommandIndex = -1;
	}

	/**
	 * Returns the Command that would be undone if undo is called.
	 *
	 * @return the Command or null if undo is impossible
	 */
	public Command getNextUndoCommand() {
		return canUndo() ? executedCommands.get(currentCommandIndex) : null;
	}

	/**
	 * Returns the Command that would be redone if redo is called.
	 *
	 * @return the Command or null if redo is impossible
	 */
	public Command getNextRedoCommand() {
		return canRedo() ? executedCommands.get(currentCommandIndex + 1) : null;
	}

}
