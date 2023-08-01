package pt.altice.heroku2kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

import pt.altice.heroku2kafka.extract.SourceKafkaConsumer;
import pt.altice.heroku2kafka.load.SinkKafkaProducer;
import pt.altice.heroku2kafka.transform.TransformKafkaRecord;

@Service
public class Heroku2KafkaService {

    @Autowired
    SourceKafkaConsumer extractor;

    @Autowired
    TransformKafkaRecord transformer;

    @Autowired
    SinkKafkaProducer loader;

    boolean keepRunning = true;

    public void run(ApplicationArguments args) {
        System.out.println(args.getOptionNames());
        try {
            while (keepRunning) {
                ConsumerRecord<String, String> rec = extractor.read();
                if (rec != null) {
                    loader.produce(transformer.transform(rec));
                    extractor.commit();
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
}
