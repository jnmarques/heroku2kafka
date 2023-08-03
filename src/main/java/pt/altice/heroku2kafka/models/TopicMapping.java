package pt.altice.heroku2kafka.models;

import java.util.Objects;


public record TopicMapping(String source, String sink) {
    public TopicMapping {
        Objects.requireNonNull(source);
        Objects.requireNonNull(sink);
    }
}
