package pt.altice.heroku2kafka.load;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
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

    // * Security Configurations */

    @Value("${sink." + CommonClientConfigs.SECURITY_PROTOCOL_CONFIG + ":#{null}}")
    public String securityProtocol;

    @Value("${sink." + SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG + ":#{null}}")
    public String sslTruststoreLocation;

    @Value("${sink." + SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG + ":#{null}}")
    public String sslTruststorePassword;

    @Value("${sink." + SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG + ":#{null}}")
    public String sslTruststoreType;

    @Value("${sink." + SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG + ":#{null}}")
    public String sslKeystoreLocation;

    @Value("${sink." + SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG + ":#{null}}")
    public String sslKeystorePassword;

    @Value("${sink." + SslConfigs.SSL_KEYSTORE_TYPE_CONFIG + ":#{null}}")
    public String sslKeystoreType;

    @Value("${sink." + SslConfigs.SSL_KEY_PASSWORD_CONFIG + ":#{null}}")
    public String sslKeyPassword;

    @Value("${sink." + SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG + ":#{null}}")
    public String sslEndpointIdentificationAlgorithm;

    @Value("${sink." + SaslConfigs.SASL_MECHANISM + ":#{null}}")
    public String saslMechanism;

    @Value("${sink." + SaslConfigs.SASL_JAAS_CONFIG + ":#{null}}")
    public String saslJaasConfig;

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
        if (securityProtocol != null) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        }
        if (sslTruststoreLocation != null) {
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, sslTruststoreLocation);
        }
        if (sslTruststorePassword != null) {
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststorePassword);
        }
        if (sslTruststoreType != null) {
            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, sslTruststoreType);
        }
        if (sslKeystoreLocation != null) {
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, sslKeystoreLocation);
        }
        if (sslKeystorePassword != null) {
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslKeystorePassword);
        }
        if (sslKeystoreType != null) {
            props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, sslKeystoreType);
        }
        if (sslKeyPassword != null) {
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslKeyPassword);
        }
        if (sslEndpointIdentificationAlgorithm != null) {
            props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, sslEndpointIdentificationAlgorithm);
        }
        if (saslMechanism != null) {
            props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        }
        if (saslJaasConfig != null) {
            props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        }

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
