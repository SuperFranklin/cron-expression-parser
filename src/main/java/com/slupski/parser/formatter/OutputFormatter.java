package com.slupski.parser.formatter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class OutputFormatter {

    private static final String FORMAT_PATTERN = "%-14s%s";
    private static final String LINE_BREAK = "\n";

    private OutputFormatter() {
    }

    public static String formatSingleRow(final String key, final String value) {
        return String.format(FORMAT_PATTERN, key, value);
    }

    public static String formatRows(final Map<String, String> map) {
        StringBuilder formattedOutput = new StringBuilder();

        Iterator<Entry<String, String>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> row = iterator.next();
            appendFormattedRow(row, formattedOutput);

            if (iterator.hasNext()) {
                appendLineBreak(formattedOutput);
            }
        }

        return formattedOutput.toString();
    }

    private static StringBuilder appendLineBreak(StringBuilder formattedOutput) {
        return formattedOutput.append(LINE_BREAK);
    }

    private static void appendFormattedRow(Entry<String, String> entry, StringBuilder formattedOutput) {
        String formattedField = getFormattedRow(entry);
        formattedOutput.append(formattedField);
    }

    private static String getFormattedRow(Entry<String, String> entry) {
        return String.format(FORMAT_PATTERN, entry.getKey(), entry.getValue());
    }

}
