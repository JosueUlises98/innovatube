package org.developers.common.utils.datetime;

import java.time.Duration;

public class DateTimeUtils {

    private DateTimeUtils() {}

    private static Duration generateDuration(long duration) {
        return Duration.ofSeconds(duration);
    }

    private static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        return String.valueOf(seconds);
    }
}
