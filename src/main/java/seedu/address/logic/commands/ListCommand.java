package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SOURCES;

import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.source.Source;


/**
 * Lists all sources in the Source Database to the user, or the top N sources where N may
 * be an optional argument supplied by the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": List all the sources in the database to the user, or optionally, the top N sources if"
            + " an argument is supplied.\n"
            + "Parameters: [INDEX] (Optional, must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 20";

    public static final String MESSAGE_LIST_ALL_SUCCESS = "Listed all sources!";

    public static final String MESSAGE_LIST_TOP_N_SUCCESS = "Listed top %d sources!";

    public static final String MESSAGE_LIST_LAST_N_SUCCESS = "Listed last %d sources!";

    public static final String MESSAGE_LIST_X_TO_Y_SUCCESS = "Listed sources from %d to %d!";

    private Index targetIndex = null;
    private Index toIndex = null;
    private Index fromIndex = null;
    private boolean posFlag = true;

    public ListCommand() {}

    //Constructor overloading to account for an optional parameter
    public ListCommand(Index targetIndex, boolean posFlag) {
        this.targetIndex = targetIndex;
        this.posFlag = posFlag;
    }

    //Constructor overloading to account for two optional parameter
    public ListCommand(Index fromIndex, Index toIndex) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    /**
     * A method which constructs a predicate to output top n sources
     * @param n positive integer
     * @return Predicate which evaluates true for first n sources only
     */
    public Predicate<Source> makePredicateForTopN(int n) {
        return new Predicate<>() {
            private int count = 0;
            public boolean test(Source source) {
                if (count < n) {
                    count++;
                    return true;
                } else {
                    count++;
                    return false;
                }
            }
        };
    }

    /**
     * A method which constructs a predicate to output last n sources
     * @param n positive integer
     * @return Predicate which evaluates true for last n sources only
     */
    public Predicate<Source> makePredicateForLastN(int n, int size) {
        return new Predicate<>() {
            private int count = 1;
            public boolean test(Source source) {
                if (size - count < n) {
                    count++;
                    return true;
                } else {
                    count++;
                    return false;
                }
            }
        };
    }

    /**
     * A method which constructs a predicate to output sources between index x and y
     * @param x a positive number
     * @param y a positive number
     * @return Predicate which evaluates true for sources between x and y only
     */
    public Predicate<Source> makePredicateForXToY(int x, int y) {
        return new Predicate<>() {
            private int count = 1;
            public boolean test(Source source) {
                if (count >= x && count <= y) {
                    count++;
                    return true;
                } else {
                    count++;
                    return false;
                }
            }
        };
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        try {
            requireNonNull(model);
            //shortcut to obtain the entire list of all sources by first displaying an unfiltered list
            model.updateFilteredSourceList(PREDICATE_SHOW_ALL_SOURCES);
            int size = model.getFilteredSourceList().size();
            if (toIndex != null && fromIndex != null) {
                if (fromIndex.getOneBased() > toIndex.getOneBased()) {
                    throw new CommandException("To-Index cannot be greater than From-Index!");
                }
                toIndex = toIndex.getOneBased() > size ? Index.fromOneBased(size) : toIndex;
                model.updateFilteredSourceList(makePredicateForXToY(fromIndex.getOneBased(), toIndex.getOneBased()));
                return new CommandResult(String.format(MESSAGE_LIST_X_TO_Y_SUCCESS,
                        fromIndex.getOneBased(), toIndex.getOneBased()));
            } else if (targetIndex != null) { //when LIST is used with an argument (show N)
                if (posFlag) {
                    //to ensure N is capped at list size
                    targetIndex = targetIndex.getOneBased() > size ? Index.fromOneBased(size) : targetIndex;
                    model.updateFilteredSourceList(makePredicateForTopN(targetIndex.getOneBased()));
                    return new CommandResult(String.format(MESSAGE_LIST_TOP_N_SUCCESS, targetIndex.getOneBased()));
                } else {
                    targetIndex = targetIndex.getOneBased() > size ? Index.fromOneBased(size) : targetIndex;
                    model.updateFilteredSourceList(makePredicateForLastN(targetIndex.getOneBased(), size));
                    return new CommandResult(String.format(MESSAGE_LIST_LAST_N_SUCCESS, targetIndex.getOneBased()));
                }
            } else { //when LIST is used without an argument (show all)
                return new CommandResult(MESSAGE_LIST_ALL_SUCCESS);
            }
        } catch (Exception ce) {
            throw new CommandException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), ce);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && ((targetIndex == ((ListCommand) other).targetIndex
                        || targetIndex.equals(((ListCommand) other).targetIndex))
                    && (toIndex == ((ListCommand) other).toIndex
                        || toIndex.equals(((ListCommand) other).toIndex))
                    && (fromIndex == ((ListCommand) other).fromIndex
                        || fromIndex.equals(((ListCommand) other).fromIndex))
                    && (posFlag == ((ListCommand) other).posFlag))); // state check
    }
}
