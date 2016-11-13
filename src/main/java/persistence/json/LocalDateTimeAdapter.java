package main.java.persistence.json;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;

class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime localDateTime,
                                 Type type, JsonSerializationContext jsonSerializationContext) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        return new JsonPrimitive(date.getTime());
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type type,
                                     JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
