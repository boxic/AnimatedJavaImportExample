package game.slam.util.animation.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

/**
 * Type adapter to teach Gson builder how to serialize/deserialize this object type
 */
public class UUIDTypeAdapter extends TypeAdapter<UUID> {

    @Override
    public void write(JsonWriter out, UUID value) throws IOException {

        // Value is null
        if (value == null) {
            out.nullValue();
        }
        // Not null
        else {

            // Parse value as String
            out.value(value.toString());
        }
    }

    @Override
    public UUID read(JsonReader in) throws IOException {

        // Value is null
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        // Parse next value as String
        String stringValue = in.nextString();

        // Catch "root"
        if (stringValue.equals("root")) {

            // Return special UUID to identify the root node
            return new UUID(0, 0);
        }

        try {
            // Convert value from String to UUID
            return UUID.fromString(stringValue);
        }
        // Invalid UUID
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}