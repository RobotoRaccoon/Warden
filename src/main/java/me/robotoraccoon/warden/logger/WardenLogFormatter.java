package me.robotoraccoon.warden.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class WardenLogFormatter extends Formatter {

    private java.util.Date date;
    private java.text.DateFormat formatter;
    private static final java.lang.String format = "yyyy-MM-dd HH:mm:ss";

    public WardenLogFormatter() {
        date = new java.util.Date();
    }

    public String format(LogRecord record) {
        if (formatter == null)
            formatter = new java.text.SimpleDateFormat(format);

        date.setTime(record.getMillis());
        return String.format("%s %s%n", formatter.format(date), record.getMessage());
    }

}
