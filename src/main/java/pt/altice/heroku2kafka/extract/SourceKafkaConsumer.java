package pt.altice.heroku2kafka.extract;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;


/***
 * Class SourceKafkaConsumer
 * 
 * This class is responsible for consuming the records from the source topic
 * 
 */
@Component
public class SourceKafkaConsumer {

    KafkaConsumer<String, String> consumer;
    ConsumerRecords<String, String> records;
    ConsumerRecord<String, String> currentRecord;
    Iterator<ConsumerRecord<String, String>> iterator;

    /***
     * Constructor for the SourceKafkaConsumer class
     * 
     * This class is responsible for consuming the records from the source topic
     * 
     */
    public SourceKafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "heroku2kafka");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("source-users-heroku"));

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