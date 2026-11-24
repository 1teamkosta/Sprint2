package kr.swdl.sprint2.service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateFormatConvert {
    private DateFormatConvert() {}

    public static String format(String dateTime) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return LocalDateTime.parse(dateTime, inputFormat).format(outputFormat);
    }
}
