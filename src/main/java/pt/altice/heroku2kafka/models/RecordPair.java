package pt.altice.heroku2kafka.models;

import java.util.Objects;

public record RecordPair(String key, String value) {
    public RecordPair {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
    }
}
