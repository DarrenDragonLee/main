package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_BAR;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DETAILS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.source.SourceContainsKeywordsPredicate;

public class SearchCommandParserTest {

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyPrefixArgs_returnsSearchCommand() {
        String input = " " + PREFIX_DETAILS + " ";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input,
                PREFIX_TITLE, PREFIX_TYPE, PREFIX_DETAILS, PREFIX_TAG);
        SearchCommand expectedSearchCommand = new SearchCommand(new SourceContainsKeywordsPredicate(argMultimap));
        assertParseSuccess(parser, " " + PREFIX_DETAILS + " ", expectedSearchCommand);
    }

    @Test
    public void parse_validArgs_returnsSearchCommand() {
        String input = TITLE_DESC_AMY;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input,
                PREFIX_TITLE, PREFIX_TYPE, PREFIX_DETAILS, PREFIX_TAG);
        // no leading and trailing whitespaces
        SearchCommand expectedSearchCommand = new SearchCommand(new SourceContainsKeywordsPredicate(argMultimap));
        assertParseSuccess(parser, TITLE_DESC_AMY, expectedSearchCommand);
        // multiple whitespaces between keywords
        assertParseSuccess(parser, "\n \t" + TITLE_DESC_AMY, expectedSearchCommand);
    }

    @Test
    public void parse_validMultipleArgs_returnsSearchCommand() {
        String input = TYPE_DESC_AMY + TYPE_DESC_AMY;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input,
                PREFIX_TITLE, PREFIX_TYPE, PREFIX_DETAILS, PREFIX_TAG);
        SearchCommand expectedSearchCommand = new SearchCommand(new SourceContainsKeywordsPredicate(argMultimap));
        assertParseSuccess(parser, TYPE_DESC_AMY + TYPE_DESC_AMY, expectedSearchCommand);
    }

    @Test
    public void parse_validCompoundArgs_returnsSearchCommand() {
        String input = TYPE_DESC_AMY + TAG_DESC_BAR;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input,
                PREFIX_TITLE, PREFIX_TYPE, PREFIX_DETAILS, PREFIX_TAG);
        SearchCommand expectedSearchCommand = new SearchCommand(new SourceContainsKeywordsPredicate(argMultimap));
        assertParseSuccess(parser, TYPE_DESC_AMY + TAG_DESC_BAR, expectedSearchCommand);
    }

}
