package pt.altice.heroku2kafka.load;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class SinkKafkaProducer {

    @Value("${sink." + ProducerConfig.BOOTSTRAP_SERVERS_CONFIG + "}")
    public String bootstrapServers;

    @Value("${sink." + ProducerConfig.CLIENT_ID_CONFIG + ":heroku2kafka}")
    public String clientId;

    @Value("${sink." + ProducerConfig.ACKS_CONFIG + ":all}")
    public String acks;

    @Value("${sink." + ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG + ":true}")
    public String idempotence;

    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
    
    private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

    KafkaProducer<String, String> producer;
    Logger logger = LoggerFactory.getLogger(SinkKafkaProducer.class);

    @PostConstruct
    public void init() {
        logger.info("SinkKafkaProducer initializing...");

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

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
