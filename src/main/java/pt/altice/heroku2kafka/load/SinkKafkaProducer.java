package pt.altice.heroku2kafka.load;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import pt.altice.heroku2kafka.models.RecordPair;

@Component
public class SinkKafkaProducer {

    KafkaProducer<String, String> producer;

    public SinkKafkaProducer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all"); // strongest producing guarantee
        props.put("idempotence", "true");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void produce(RecordPair recordPair) {
        try {
            producer
                    .send(new ProducerRecord<>("sink-users-altice", recordPair.key(), recordPair.value()))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error producing to topic: " + e.getMessage());
        }
    }

    public void close() {
        producer.close();
    }
}
