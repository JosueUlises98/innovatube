package org.developers.common.utils.datetime;

import com.google.api.client.util.DateTime;

import java.time.*;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static LocalDateTime toLocalDateTime(DateTime googleDateTime) {
        if (googleDateTime == null) {
            return null;
        } else {
            long milliseconds = googleDateTime.getValue();
            int tzShift = googleDateTime.getTimeZoneShift();
            Instant instant = Instant.ofEpochMilli(milliseconds);
            if (googleDateTime.isDateOnly()) {
                return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).withNano(0);
            } else {
                ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(tzShift * 60);
                return LocalDateTime.ofInstant(instant, zoneOffset);
            }
        }
    }

    public static DateTime toGoogleDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            long milliseconds = zonedDateTime.toInstant().toEpochMilli();
            int tzShift = zonedDateTime.getOffset().getTotalSeconds() / 60;
            return new DateTime(milliseconds, tzShift);
        }
    }

    public static long convertirDuracion(String duracion) {
        try {
            if (duracion.startsWith("P")) {
                return Duration.parse(duracion).getSeconds();
            } else if (duracion.startsWith("PT")) {
                return Duration.parse(duracion).getSeconds();
            } else if (duracion.contains(":")) {
                String[] partes = duracion.split(":");
                long horas = 0L;
                long minutos = 0L;
                long segundos = 0L;
                if (partes.length == 3) {
                    horas = Long.parseLong(partes[0]);
                    minutos = Long.parseLong(partes[1]);
                    segundos = Long.parseLong(partes[2]);
                } else if (partes.length == 2) {
                    minutos = Long.parseLong(partes[0]);
                    segundos = Long.parseLong(partes[1]);
                }

                return Duration.ofHours(horas).plusMinutes(minutos).plusSeconds(segundos).getSeconds();
            } else {
                throw new IllegalArgumentException("Formato de duración no soportado: " + duracion);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear la duración: " + duracion, e);
        }
    }

    public static Duration convertYouTubeDuration(String youtubeDuration) {
        Duration duration = Duration.parse(youtubeDuration);
        long seconds = duration.getSeconds();
        return Duration.ofSeconds(seconds);
    }
}
