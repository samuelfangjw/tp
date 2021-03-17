package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.ui.UiCommand;

/**
 * Show deadlines tab of a Project.
 */
public class ShowDeadlinesTabCommand extends Command {

    public static final String COMMAND_WORD = "tabD";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays the deadlines tab of an open project.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Viewing Deadlines Tab";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS,
                UiCommand.SHOW_DEADLINES);
    }
}