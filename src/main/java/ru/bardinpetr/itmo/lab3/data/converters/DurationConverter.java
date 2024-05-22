package ru.bardinpetr.itmo.lab3.data.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {
    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute.toNanos();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return Duration.ofNanos(dbData);
    }
}
