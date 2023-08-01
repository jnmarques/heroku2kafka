package pt.altice.heroku2kafka.load;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SinkKafkaProducer {

    KafkaProducer<String, String> producer;
    Logger logger = LoggerFactory.getLogger(SinkKafkaProducer.class);

    public SinkKafkaProducer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all"); // strongest producing guarantee
        props.put("idempotence", "true");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void produce(ProducerRecord<String, String> rec) throws InterruptedException, ExecutionException {
        producer.send(rec).get();
    }

    public void close() {
        producer.close();
    }
}
