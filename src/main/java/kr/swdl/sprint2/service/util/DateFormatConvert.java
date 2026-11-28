package kr.swdl.sprint2.service.util;

import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;

public final class DateFormatConvert {
    private DateFormatConvert() {}

    public static String format(String dateTime) {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return Timestamp.valueOf(dateTime).toLocalDateTime().format(outputFormat);
    }
}
