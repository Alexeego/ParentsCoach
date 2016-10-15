package com.example.alexey.parentscoach.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Alexey on 15.10.2016.
 */
public class ConnectionUtil {
    public static String transformToJson(Object object) throws IOException {
        StringWriter writer = new StringWriter();
        new ObjectMapper().writeValue(writer, object);
        return writer.toString();
    }

    public static <A> A transformFromJson(final TypeReference<A> type, final String json) throws IOException {
        return new ObjectMapper().readValue(json, type);
    }
}
