package pt.altice.heroku2kafka.service;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.altice.heroku2kafka.extract.SourceKafkaConsumer;
import pt.altice.heroku2kafka.load.SinkKafkaProducer;
import pt.altice.heroku2kafka.transform.TransformKafkaRecord;

@Service
public class Heroku2KafkaService {

    Logger logger = LoggerFactory.getLogger(Heroku2KafkaService.class);

    @Autowired
    SourceKafkaConsumer extractor;

    @Autowired
    TransformKafkaRecord transformer;

    @Autowired
    SinkKafkaProducer loader;

    boolean keepRunning = true;

    public void run() {
        try {
            while (keepRunning) {
                ConsumerRecord<String, String> rec = extractor.read();
                if (rec != null) {
                    try {
                        loader.produce(transformer.transform(rec));
                        commit();
                    } catch (InterruptedException e) {
                        logger.error("Interrupted while producing record", e);
                        shutdown();
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        logger.error("Error while producing record", e);
                        shutdown();
                    }
                }
            }
        } finally {
            extractor.close();
            loader.close();
        }
    }

    public void shutdown() {
        keepRunning = false;
    }

    public void commit() {
        extractor.commit();
    }
}
