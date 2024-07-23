package game.slam.util.animation.data;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public enum NodeType {
    BONE,
    STRUCT,
    CAMERA,
    LOCATOR,
    TEXT_DISPLAY,
    ITEM_DISPLAY,
    BLOCK_DISPLAY,
    UNKNOWN;

    /**
     * Type adapter to teach Gson builder how to serialize/deserialize this object type
     */
    public static class TypeAdapter extends com.google.gson.TypeAdapter<NodeType> {

        @Override
        public void write(JsonWriter out, NodeType value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.name().toLowerCase());
            }
        }

        @Override
        public NodeType read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String stringValue = in.nextString();
            try {
                return valueOf(stringValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }
    }
}
