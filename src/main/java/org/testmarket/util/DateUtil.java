package org.testmarket.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

/**
 * Utility for work with dates.
 * 
 * @author Sergey Stotskiy
 */
@Component
public class DateUtil {

    public static final String DATE_SIMPLE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_FULL_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_FULL_FORMAT_FOR_FILE_NAME = "dd-MM-yyyy HH.mm.ss.SSS";
    public static final String DATE_FORMAT_WITHOUT_SECONDS = "dd.MM.yyyy HH:mm";
    public static final String DATE_FORMAT_VIIS = "yyyy-MM-dd HH:mm:ss";

    public static final Date MAX_DATE = new GregorianCalendar(9999, Calendar.DECEMBER, 31)
        .getTime();

    /**
     * Get current year.
     * 
     * @return
     */
    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

    /**
     * Get current year.
     * 
     * @return
     */
    public static int getCurrentDate() {
        return LocalDateTime.now().minusDays(1).getYear();
    }

    /**
     * Get date full format pattern.
     * 
     * @return
     */
    public String getFullFormat() {
        return DATE_FULL_FORMAT;
    }

    /**
     * Get date short format pattern.
     * 
     * @return
     */
    public String getShortDateFormat() {
        return DATE_SIMPLE_FORMAT;
    }

    /**
     * Parse string date to date.
     * 
     * @param date
     *            date string
     * @return
     */
    public static Date parseAsShortDate(String date) {
        return Date.from(LocalDate.parse(date, FormatterUtils.SIMPLE_DATE_FORMAT)
            .atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Get full date format.
     * 
     * @return
     */
    public String getFullDateFormatWithoutSeconds() {
        return DATE_FORMAT_WITHOUT_SECONDS;
    }
}
