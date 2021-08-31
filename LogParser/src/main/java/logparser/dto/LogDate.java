package logparser.dto;

import java.util.Date;

public class LogDate {
    private final Date date;

    public LogDate(final Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
