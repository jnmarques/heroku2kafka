package pt.altice.heroku2kafka.transform;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
public class TransformKafkaRecord {
    public ProducerRecord<String, String> transform(ConsumerRecord<String, String> rec) {
        return new ProducerRecord<>("sink-users-altice", null, rec.timestamp(), rec.key(), rec.value(), rec.headers());
    }
}
