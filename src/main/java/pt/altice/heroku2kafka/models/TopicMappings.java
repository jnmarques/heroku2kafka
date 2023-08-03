package pt.altice.heroku2kafka.models;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Component
@ConfigurationProperties
@Data
public class TopicMappings {
    public List<TopicMapping> topics;

    public Map<String, String> topicsMap;

    @PostConstruct
    public void init() {
        topicsMap = topics.stream().collect(Collectors.toMap(TopicMapping::source, TopicMapping::sink));
    }
}
