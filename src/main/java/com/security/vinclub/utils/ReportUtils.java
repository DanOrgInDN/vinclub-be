package com.security.vinclub.utils;

import com.security.vinclub.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ReportUtils {

    public static final String REPORT = "Report";
    public static final String NO_DATA_FOUND = "Không có dữ liệu";

    public static String generateReportHeader(String reportName, List<String> headerCells) {
        return createLine(Arrays.asList(REPORT, reportName))
                + createLine(headerCells);
    }


    private static String createLine(List<String> cells) {
        String fullLine = cells.stream().map(ReportUtils::csvCell).reduce(StringUtils.EMPTY, String::concat);
        return fullLine.substring(0, fullLine.length() - 1)
                + Constant.NEW_LINE;
    }

    private static String csvCell(String cell) {
        return cell.concat(Constant.COMMA);
    }

    public static String formatStringCsv(String input) {
        if (input == null || StringUtils.equalsIgnoreCase("null", input)) {
            return StringUtils.EMPTY;
        }
        return input.replaceAll(Constant.COMMA, Constant.EMPTY_SPACE)
                .replaceAll("[\\n,\\r]", Constant.EMPTY_SPACE);
    }

}
