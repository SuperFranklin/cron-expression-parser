package com.slupski.parser.cron;

import java.util.LinkedHashMap;
import java.util.Map;

public class CronExpressionParser {

    private static final String WHITESPACE = " ";
    private static final int MINUTE_PART_IDX = 0;
    private static final int HOUR_PART_IDX = 1;
    private static final int DAY_OF_MONTH_PART_IDX = 2;
    private static final int MONTH_PART_IDX = 3;
    private static final int DAY_OF_WEEK_PART_IDX = 4;
    private final CronFieldParser minuteParser;
    private final CronFieldParser hourParser;
    private final CronFieldParser dayOfMonthParser;
    private final CronFieldParser monthParser;
    private final CronFieldParser dayOfWeekParser;

    public CronExpressionParser() {
        this.minuteParser = CronFieldParser.withValuesBetween(0, 59);
        this.hourParser = CronFieldParser.withValuesBetween(0, 23);
        this.dayOfMonthParser = CronFieldParser.withValuesBetween(1, 31);
        this.monthParser = CronFieldParser.withValuesBetween(1, 12);
        this.dayOfWeekParser = CronFieldParser.withValuesBetween(1, 7);
    }

    public Map<String, String> parse(final String cronExpression) {
        String[] cronParts = splitByWhitespace(cronExpression);

        verify(cronParts);

        return parse(cronParts);
    }

    private String[] splitByWhitespace(final String cronExpression) {
        try {
            return cronExpression.split(WHITESPACE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid cron expression. Cannot split by whitespace.", e);
        }
    }

    private void verify(final String[] cronParts) {
        if(cronParts.length != 5) {
            throw new IllegalArgumentException("Invalid cron expression. Expected 5 parts, but got " + cronParts.length);
        }
    }

    private Map<String, String> parse(final String[] cronParts) {
        String minutePart = cronParts[MINUTE_PART_IDX];
        String hourPart = cronParts[HOUR_PART_IDX];
        String dayOfMonthPart = cronParts[DAY_OF_MONTH_PART_IDX];
        String monthPart = cronParts[MONTH_PART_IDX];
        String dayOfWeekPart = cronParts[DAY_OF_WEEK_PART_IDX];

        String parsedMinutes = minuteParser.parse(minutePart);
        String parsedHours = hourParser.parse(hourPart);
        String parsedDaysOfMonth = dayOfMonthParser.parse(dayOfMonthPart);
        String parsedMonths = monthParser.parse(monthPart);
        String parsedDaysOfWeek = dayOfWeekParser.parse(dayOfWeekPart);

        Map<String, String> parsedCronExpression = new LinkedHashMap<>();

        parsedCronExpression.put("minute", parsedMinutes);
        parsedCronExpression.put("hour", parsedHours);
        parsedCronExpression.put("day of month", parsedDaysOfMonth);
        parsedCronExpression.put("month", parsedMonths);
        parsedCronExpression.put("day of week", parsedDaysOfWeek);

        return parsedCronExpression;
    }

}
