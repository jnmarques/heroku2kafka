package pt.altice.heroku2kafka.transform;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import pt.altice.heroku2kafka.models.RecordPair;

@Component
public class TransformKafkaRecord {
    public RecordPair transform(ConsumerRecord<String, String> record) {
        return new RecordPair(record.key(), record.value());
    }
}
