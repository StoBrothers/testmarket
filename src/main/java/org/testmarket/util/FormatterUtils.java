package org.testmarket.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Transformation date to String. This class used DateUtil.
 * 
 * @author Sergey Stotskiy
 */
public class FormatterUtils {

    public static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter
        .ofPattern(DateUtil.DATE_SIMPLE_FORMAT);

    public static final DateTimeFormatter FULL_DATE_FORMAT = DateTimeFormatter
        .ofPattern(DateUtil.DATE_FULL_FORMAT);

    /**
     * Get transformed Date to String.
     *
     * @return - Get transformed date to string.
     */
    public static String getFormattedSimpleDate(Date date) {
        return getFormattedDate(date, DateUtil.DATE_SIMPLE_FORMAT);
    }

    /**
     * Get transformed Date to String.
     *
     * @return - transformed date to string.
     */
    public static String getFormattedFullDate(Date date) {
        return getFormattedDate(date, DateUtil.DATE_FULL_FORMAT);
    }

    /**
     * Get transformed Date to String.
     * 
     * @param date
     * @param format
     * @return transformed Date to String.
     */
    public static String getFormattedDate(Date date, String format) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        } else {
            return null;
        }
    }

    /**
     * Get transformed Date to String.
     * 
     * @param date
     * @return - date with format {@link DateUtil#DATE_FULL_FORMAT_FOR_FILE_NAME}
     */
    public static String getFormattedFullDateForFileName(Date date) {
        return getFormattedDate(date, DateUtil.DATE_FULL_FORMAT_FOR_FILE_NAME);
    }
}
