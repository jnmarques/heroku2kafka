package pt.altice.heroku2kafka.transform;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransformKafkaRecord {

    @Value("${sink.topic}")
    public String topic;

    public ProducerRecord<String, String> transform(ConsumerRecord<String, String> rec) {
        return new ProducerRecord<>(topic, null, rec.timestamp(), rec.key(), rec.value(), rec.headers());
    }
}
