package com.slupski.parser;

import com.slupski.parser.cron.CronExpressionParser;
import com.slupski.parser.formatter.OutputFormatter;

import java.util.Arrays;
import java.util.Map;

public class App {

    private static final String WHITESPACE = " ";
    private static final int CRON_ARGUMENT_INDEX = 0;
    private static final int COMMAND_ARGUMENT_INDEX = 1;
    private static final String COMMAND_COLUMN_NAME = "command";

    private static final int NUMBER_OF_CRON_ELEMENTS = 5;

    public static void main(String[] args) {
        validateArguments(args);

        String inputArgument = args[0];

        String[] splittedArguments = splitArguments(inputArgument);
        String cronFields = splittedArguments[CRON_ARGUMENT_INDEX];
        String command = splittedArguments[COMMAND_ARGUMENT_INDEX];

        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedCron = parser.parse(cronFields);

        System.out.println(OutputFormatter.formatRows(parsedCron));
        System.out.println(OutputFormatter.formatSingleRow(COMMAND_COLUMN_NAME, command));
    }

    private static void validateArguments(String[] args) {
        if (args.length != 1) {
            System.err.println("Incorrect number of arguments. Please provide a single argument with a cron expression and a command.");
            System.exit(1);
        }
    }

    private static String[] splitArguments(final String inputArgument) {
        String[] parts = inputArgument.split(WHITESPACE);

        validateParts(parts);

        String firstPart = String.join(" ", Arrays.copyOfRange(parts, 0, NUMBER_OF_CRON_ELEMENTS));
        String lastPart = parts[NUMBER_OF_CRON_ELEMENTS];

        return new String[]{firstPart, lastPart};
    }

    private static void validateParts(String[] parts) {
        if (parts.length != NUMBER_OF_CRON_ELEMENTS + 1) {
            System.err.println("Invalid cron expression. It should have 5 fields and a command.");
            System.exit(1);
        }
    }
}