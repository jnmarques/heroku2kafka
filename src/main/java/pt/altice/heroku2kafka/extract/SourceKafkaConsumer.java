package pt.altice.heroku2kafka.extract;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/***
 * Class SourceKafkaConsumer
 * 
 * This class is responsible for consuming the records from the source topic
 * 
 */
@Component
public class SourceKafkaConsumer {

    @Value("${source." + ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG + "}")
    public String bootstrapServers;

    @Value("${source.topic}")
    public String topic;

    @Value("${source." + ConsumerConfig.GROUP_ID_CONFIG + ":heroku2kafka}")
    public String groupId;

    @Value("${source." + ConsumerConfig.CLIENT_ID_CONFIG + ":heroku2kafka}")
    public String clientId;

    @Value("${source." + ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG + ":false}")
    public String enableAutoCommit;

    @Value("${source." + ConsumerConfig.AUTO_OFFSET_RESET_CONFIG + ":earliest}")
    public String autoOffsetReset;

    private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

    private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

    Logger logger = LoggerFactory.getLogger(SourceKafkaConsumer.class);

    KafkaConsumer<String, String> consumer;
    ConsumerRecords<String, String> records;
    ConsumerRecord<String, String> currentRecord;
    Iterator<ConsumerRecord<String, String>> iterator;

    /***
     * Method init
     * 
     * 
     * 
     */
    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        // Initialize the records list to eliminate the need for the read to check for
        // null
        records = new ConsumerRecords<>(new HashMap<>());
        iterator = records.iterator();
    }

    /***
     * Method read
     * 
     * This method is responsible for reading the next record from the source topic
     * 
     */
    public ConsumerRecord<String, String> read() {
        // Check if the current records list has any records
        // If not poll for records
        if (!iterator.hasNext()) {
            records = consumer.poll(Duration.ofMillis(10000));
            iterator = records.iterator();
        }

        // Check if the records list is still empty
        // If it is return null
        if (!iterator.hasNext()) {
            return null;
        }

        // Advance the iterator and return the current record
        currentRecord = iterator.next();
        logger.debug("Reading record with offset: " + currentRecord.offset());
        return currentRecord;
    }

    /***
     * Method commit
     * 
     * This method is responsible for committing the current record offset
     * 
     */
    public void commit() {
        // Check if there is a current record
        if (currentRecord == null) {
            return;
        }

        // Commit the current record offset
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        currentOffsets.put(new TopicPartition(currentRecord.topic(), currentRecord.partition()),
                new OffsetAndMetadata(currentRecord.offset()));
        consumer.commitSync(currentOffsets);
        currentRecord = null;
    }

    /***
     * Method close
     * 
     * This method is responsible for closing the consumer
     */
    public void close() {
        consumer.close();
    }

}
