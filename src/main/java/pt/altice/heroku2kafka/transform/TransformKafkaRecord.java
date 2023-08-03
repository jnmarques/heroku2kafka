package pt.altice.heroku2kafka.transform;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.altice.heroku2kafka.models.TopicMappings;

@Component
public class TransformKafkaRecord {

    @Autowired
    TopicMappings topics;

    public ProducerRecord<String, String> transform(ConsumerRecord<String, String> rec) {
        return new ProducerRecord<>(topics.getTopicsMap().get(rec.topic()), null, rec.timestamp(), rec.key(),
                rec.value(), rec.headers());
    }
}
