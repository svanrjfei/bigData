package cc.shunfu.bigdata.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-23
 */

public class DateUtils {
    public static String formatDate(Date date, String formatString) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatString);
        return formatter.format(date);
    }

    public static String formatDateTime(LocalDateTime dateTime, String formatString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatString);
        return dateTime.format(formatter);
    }
}
