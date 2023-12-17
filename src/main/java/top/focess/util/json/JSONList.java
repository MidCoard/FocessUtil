package top.focess.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class is used to define a JSON object as List.
 */
public class JSONList extends JSONObject implements Iterable<JSONObject>,IJSONList {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<Object>> TYPE_REFERENCE = new TypeReference<List<Object>>() {
    };
    private final List<?> values;


    /**
     * Constructs a JSONList from a JSON string
     * @param json the JSON string
     */
    public JSONList(final String json) {
        try {
            this.values = OBJECT_MAPPER.readValue(json, TYPE_REFERENCE);
        } catch (final IOException e) {
            throw new JSONParseException(json);
        }
    }

    /**
     * Initializes the JSONList with existed values
     * @param values the JSON list values
     */
    public JSONList(final List<?> values) {
        this.values = values;
    }

    public <T> T get(final int index) {
        return (T) this.values.get(index);
    }

    public JSON getJSON(final int index) {
        if (this.values.get(index) instanceof Map)
            return new JSON((Map<String, Object>) this.values.get(index));
        throw new IllegalStateException("This element is not a valid map.");
    }

    public JSONList getList(final int index) {
        if (this.values.get(index) instanceof List)
            return new JSONList((List<?>) this.values.get(index));
        throw new IllegalStateException("This element is not a valid list.");
    }

    public List<?> getValues() {
        return this.values;
    }

    public String toJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this.values);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int size() {
        return this.values.size();
    }

    @Override
    public String toString() {
        return this.values.toString();
    }

    @NotNull
    @Override
    public Iterator<JSONObject> iterator() throws JSONParseException {
        return values.stream().map(JSONObject::parse).iterator();
    }

    @NotNull
    public JSONIntList toInts() {
        return new JSONIntList(this);
    }

    @NotNull
    public JSONDoubleList toDoubles() {
        return new JSONDoubleList(this);
    }

    @Override
    public JSONList getJSONList() {
        return this;
    }
}
