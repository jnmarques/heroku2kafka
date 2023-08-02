package pt.altice.heroku2kafka.load;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class SinkKafkaProducer {

    @Value("${sink.bootstrap-servers}")
    public String bootstrapServers;

    @Value("${sink.client.id:heroku2kafka}")
    public String clientId;

    @Value("${sink.acks:all}")
    public String acks;

    @Value("${sink.idempotence:true}")
    public String idempotence;

    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

    private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

    KafkaProducer<String, String> producer;
    Logger logger = LoggerFactory.getLogger(SinkKafkaProducer.class);

    @PostConstruct
    public void init() {
        logger.info("SinkKafkaProducer initializing...");

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("client.id", clientId);
        props.put("acks", acks);
        props.put("idempotence", idempotence);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", valueSerializer);

        producer = new KafkaProducer<>(props);
    }

    public void produce(ProducerRecord<String, String> rec) throws InterruptedException, ExecutionException {
        logger.debug("Reading record.");
        producer.send(rec).get();
    }

    public void close() {
        producer.close();
    }

}
