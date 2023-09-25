package com.wangtao.social.common.core.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wangtao
 * Created at 2023-09-25
 */
public final class DateUtils {

    public static final String STANDARD_DATE = "yyyy-MM-dd";

    public static final String SHORT_DATE = "yyyyMMdd";

    public static final String STANDARD_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter STANDARD_DATE_FORMATTER = DateTimeFormatter.ofPattern(STANDARD_DATE);

    private static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern(SHORT_DATE);

    private static final DateTimeFormatter STANDARD_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(STANDARD_DATE_TIME);

    private DateUtils() {

    }

    public static LocalDate parseDate(String dataStr) {
        if (StringUtils.isBlank(dataStr)) {
            return null;
        }
        return LocalDate.parse(dataStr, STANDARD_DATE_FORMATTER);
    }

    public static LocalDate parseDate(String dataStr, String pattern) {
        if (StringUtils.isBlank(dataStr)) {
            return null;
        }
        return LocalDate.parse(dataStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseShortDate(Integer date) {
        if (date == null) {
            return null;
        }
        return parseShortDate(String.valueOf(date));
    }

    public static LocalDate parseShortDate(String dataStr) {
        if (StringUtils.isBlank(dataStr)) {
            return null;
        }
        return LocalDate.parse(dataStr, SHORT_DATE_FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(STANDARD_DATE_FORMATTER);
    }

    public static String formatDate(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatShortDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(SHORT_DATE_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(STANDARD_DATE_TIME_FORMATTER);
    }
}
