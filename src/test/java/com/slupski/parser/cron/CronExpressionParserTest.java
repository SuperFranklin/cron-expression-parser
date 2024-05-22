package com.slupski.parser.cron;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class CronExpressionParserTest {

    @Test
    void should_handle_specific_minute_hour_day_month_and_day_of_week() {
        //given
        String cronExpression = "15 12 10 6 5";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("15", parsedExpression.get("minute"));
        assertEquals("12", parsedExpression.get("hour"));
        assertEquals("10", parsedExpression.get("day of month"));
        assertEquals("6", parsedExpression.get("month"));
        assertEquals("5", parsedExpression.get("day of week"));
    }

    @Test
    void should_handle_interval_values() {
        //given
        String cronExpression = "0-4 12-14 30-31 10-12 6-7";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("0 1 2 3 4", parsedExpression.get("minute"));
        assertEquals("12 13 14", parsedExpression.get("hour"));
        assertEquals("30 31", parsedExpression.get("day of month"));
        assertEquals("10 11 12", parsedExpression.get("month"));
        assertEquals("6 7", parsedExpression.get("day of week"));
    }


    @Test
    void should_handle_wildcard_expression() {
        //given
        String cronExpression = "* * * * *";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals(IntStream.rangeClosed(0,59).boxed().map(String::valueOf).collect(Collectors.joining(" ")), parsedExpression.get("minute"));
        assertEquals(IntStream.rangeClosed(0,23).boxed().map(String::valueOf).collect(Collectors.joining(" ")), parsedExpression.get("hour"));
        assertEquals(IntStream.rangeClosed(1,31).boxed().map(String::valueOf).collect(Collectors.joining(" ")), parsedExpression.get("day of month"));
        assertEquals(IntStream.rangeClosed(1,12).boxed().map(String::valueOf).collect(Collectors.joining(" ")), parsedExpression.get("month"));
        assertEquals(IntStream.rangeClosed(1,7).boxed().map(String::valueOf).collect(Collectors.joining(" ")), parsedExpression.get("day of week"));
    }

    @Test
    void should_handle_comma_separated_values() {
        //given
        String cronExpression = "1,2,3 4,5,6 7,8,9 10,11,12 1,2,3";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("1 2 3", parsedExpression.get("minute"));
        assertEquals("4 5 6", parsedExpression.get("hour"));
        assertEquals("7 8 9", parsedExpression.get("day of month"));
        assertEquals("10 11 12", parsedExpression.get("month"));
        assertEquals("1 2 3", parsedExpression.get("day of week"));
    }

    @Test
    void should_handle_slash_separated_values() {
        //given
        String cronExpression = "*/30 */12 */15 */6 */3";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("0 30", parsedExpression.get("minute"));
        assertEquals("0 12", parsedExpression.get("hour"));
        assertEquals("1 16 31", parsedExpression.get("day of month"));
        assertEquals("1 7", parsedExpression.get("month"));
        assertEquals("1 4 7", parsedExpression.get("day of week"));
    }

    @Test
    void should_handle_shift_in_slash_separated_values() {
        //given
        String cronExpression = "10/30 1/12 2/15 2/6 1/3";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("10 40", parsedExpression.get("minute"));
        assertEquals("1 13", parsedExpression.get("hour"));
        assertEquals("2 17", parsedExpression.get("day of month"));
        assertEquals("2 8", parsedExpression.get("month"));
        assertEquals("1 4 7", parsedExpression.get("day of week"));
    }


    @Test
    void should_handle_max_values() {
        //given
        String cronExpression = "59 23 31 12 7";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("59", parsedExpression.get("minute"));
        assertEquals("23", parsedExpression.get("hour"));
        assertEquals("31", parsedExpression.get("day of month"));
        assertEquals("12", parsedExpression.get("month"));
        assertEquals("7", parsedExpression.get("day of week"));
    }

    @Test
    void should_handle_min_values() {
        //given
        String cronExpression = "0 0 1 1 1";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("0", parsedExpression.get("minute"));
        assertEquals("0", parsedExpression.get("hour"));
        assertEquals("1", parsedExpression.get("day of month"));
        assertEquals("1", parsedExpression.get("month"));
        assertEquals("1", parsedExpression.get("day of week"));
    }

    @Test
    void should_handle_dash_separated_values() {
        //given
        String cronExpression = "1-3 4-6 7-9 10-12 1-3";

        //when
        CronExpressionParser parser = new CronExpressionParser();
        Map<String, String> parsedExpression = parser.parse(cronExpression);

        //then
        assertEquals("1 2 3", parsedExpression.get("minute"));
        assertEquals("4 5 6", parsedExpression.get("hour"));
        assertEquals("7 8 9", parsedExpression.get("day of month"));
        assertEquals("10 11 12", parsedExpression.get("month"));
        assertEquals("1 2 3", parsedExpression.get("day of week"));
    }

    @Test
    void should_throw_illegal_argument_exception_when_expression_is_too_short() {
        //given
        String cronExpression = "1 2 3 4";

        //when
        CronExpressionParser parser = new CronExpressionParser();

        //then
        assertThrows(IllegalArgumentException.class, () -> parser.parse(cronExpression));
    }

    @Test
    void should_throw_illegal_argument_exception_when_expression_is_too_long() {
        //given
        String cronExpression = "1 2 3 4 5 6";

        //when
        CronExpressionParser parser = new CronExpressionParser();

        //then
        assertThrows(IllegalArgumentException.class, () -> parser.parse(cronExpression));
    }

    @Test
    void should_throw_illegal_argument_exception_when_cannot_parse_expression() {
        //given
        String cronExpression = "1as1235";

        //when
        CronExpressionParser parser = new CronExpressionParser();

        //then
        assertThrows(IllegalArgumentException.class, () -> parser.parse(cronExpression));
    }

    @Test
    void should_throw_illegal_argument_exception_when_operator_is_not_supported() {
        //given
        String cronExpression = "* & * * *";

        //when
        CronExpressionParser parser = new CronExpressionParser();

        //then
        assertThrows(IllegalArgumentException.class, () -> parser.parse(cronExpression));
    }
}