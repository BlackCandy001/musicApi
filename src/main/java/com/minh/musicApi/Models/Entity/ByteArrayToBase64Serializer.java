package com.minh.musicApi.Models.Entity;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ByteArrayToBase64Serializer extends JsonSerializer<byte[]> {
    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String base64Encoded = Base64.getEncoder().encodeToString(value);
        gen.writeString(base64Encoded);
    }
}