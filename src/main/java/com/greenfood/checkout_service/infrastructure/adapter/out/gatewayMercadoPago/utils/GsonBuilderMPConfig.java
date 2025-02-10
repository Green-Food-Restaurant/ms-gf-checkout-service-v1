package com.greenfood.checkout_service.infrastructure.adapter.out.gatewayMercadoPago.utils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class GsonBuilderMPConfig {

    private static final String SERIALIZE_DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final DateTimeFormatter[] ISO8601_DATETIME_FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX][XX][X]"),
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss[.SSS][XXX][XX][X]"),
    };
    
    public static Gson gson() {
        return new GsonBuilder()
        .registerTypeAdapter(
                OffsetDateTime.class,
                (JsonDeserializer<OffsetDateTime>) (json, type, context) -> parseDateTime(json))
        .registerTypeAdapter(
                OffsetDateTime.class,
                (JsonSerializer<OffsetDateTime>) (offsetDateTime, type, context) -> new JsonPrimitive(
                        DateTimeFormatter.ofPattern(SERIALIZE_DATE_FORMAT_ISO8601)
                                .format(offsetDateTime)))
        .registerTypeAdapter(
                LocalDate.class,
                (JsonSerializer<LocalDate>) (localDate, type,
                        context) -> new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .registerTypeAdapter(
                LocalDate.class,
                (JsonDeserializer<LocalDate>) (localDate, type, context) -> LocalDate
                        .parse(localDate.getAsString()))
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    }
    
    private static OffsetDateTime parseDateTime(JsonElement json) {
        if (json == null || json.getAsString().isEmpty()) {
            return null;
        }

        for (int i = 0; i < ISO8601_DATETIME_FORMATTERS.length; i++) {
            try {
                return OffsetDateTime.parse(json.getAsString(), ISO8601_DATETIME_FORMATTERS[i]);
            } catch (DateTimeParseException e) {
                // try all formatters, if none works, throw exception from last one
                if (i == ISO8601_DATETIME_FORMATTERS.length - 1) {
                    throw e;
                }
            }
        }
        return null;
    }
}
