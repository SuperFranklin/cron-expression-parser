package com.slupski.parser.cron;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CronFieldParser {

    private static final String ASTERISK = "*";
    private static final String SLASH = "/";
    private static final String DASH = "-";
    private static final String COMMA = ",";
    private static final String WHITESPACE = " ";
    private static final String SINGLE_NUMBER_REGEX = "\\d+";
    private final List<Integer> allowedValues;

    private CronFieldParser(final List<Integer> allowedValues) {
        this.allowedValues = allowedValues;
    }

    static CronFieldParser withValuesBetween(final int start, final int end) {
        List<Integer> values = IntStream.rangeClosed(start, end)
                .boxed()
                .toList();
        return new CronFieldParser(values);
    }

    String parse(final String expression) {
        if (isSeparatedBy(expression, COMMA)) {
            return handleComma(expression);
        } else if (isSeparatedBy(expression, SLASH)) {
            return handleSlash(expression);
        } else if (isSeparatedBy(expression, DASH)) {
            return handleDash(expression);
        } else if (isAsterisk(expression)) {
            return handleAsterisk();
        } else if (isSingleNumber(expression)) {
            return expression;
        }

        throw new IllegalArgumentException("Invalid expression: " + expression);
    }

    private boolean isAsterisk(final String expression) {
        return expression.equals(ASTERISK);
    }

    private boolean isSingleNumber(final String expression) {
        return expression.matches(SINGLE_NUMBER_REGEX);
    }

    private boolean isSeparatedBy(final String expression, final String separator) {
        return expression.contains(separator);
    }

    private String handleAsterisk() {
        return allowedValues.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(WHITESPACE));
    }

    private String handleDash(final String expression) {
        String[] parts = splitBySeparator(expression, DASH);
        int start = Integer.parseInt(parts[0]);
        int end = Integer.parseInt(parts[1]);

        return allowedValues.stream()
                .filter(between(start, end))
                .map(String::valueOf)
                .collect(Collectors.joining(WHITESPACE));
    }

    private Predicate<Integer> between(int start, int end) {
        return value -> value >= start && value <= end;
    }

    private String handleSlash(final String expression) {
        String[] parts = splitBySeparator(expression, SLASH);

        int shiftedBy;
        if (parts[0].equals(ASTERISK)) {
            shiftedBy = possibleValuesStartFromZero() ? 0 : 1;
        } else {
            shiftedBy = Integer.parseInt(parts[0]);
        }

        int incrementBy = Integer.parseInt(parts[1]);

        return allowedValues.stream()
                .filter(everyDividedByShiftedBy(incrementBy, shiftedBy))
                .map(String::valueOf)
                .collect(Collectors.joining(WHITESPACE));
    }

    private Predicate<Integer> everyDividedByShiftedBy(final int incrementBy, final int shiftedBy) {
        return value -> (value - shiftedBy) >= 0 && (value - shiftedBy) % incrementBy == 0;
    }

    private boolean possibleValuesStartFromZero() {
        return allowedValues.get(0) == 0;
    }

    private String handleComma(final String expression) {
        return String.join(" ", splitBySeparator(expression, ","));
    }

    private String[] splitBySeparator(final String expression, final String split) {
        return expression.split(split);
    }
}
