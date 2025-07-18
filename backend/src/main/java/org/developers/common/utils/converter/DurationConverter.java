package org.developers.common.utils.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(
        autoApply = true
)
public class DurationConverter implements AttributeConverter<Duration, String> {
    public String convertToDatabaseColumn(Duration duration) {
        return duration != null ? duration.toString() : null;
    }

    public Duration convertToEntityAttribute(String dbData) {
        return dbData != null ? Duration.parse(dbData) : null;
    }
}
