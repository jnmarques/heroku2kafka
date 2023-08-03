# Kafka 2 Kafka Data Replication Util

This is a project for a kafka to Kafka data replication without using any kafka connect features.

# Configurations

The configurations clones some of the common Kafka Client configurations. Since it will have to connect to two different Kafka clusters, it will have to have two different configurations.

For the source configurations you should prefix the configurations "source.". For the sink configurations you should prefix the configurations "sink.".

## Source Configurations

### Mandatory

source.bootstrap.servers
source.topic

### Optional

- **source.group.id** - Default is "heroku2kafka". A unique string that identifies the consumer group this consumer belongs to. This property is required if the consumer uses either the group management functionality by using `subscribe(topic)` or the Kafka-based offset management strategy.
- **client.id** - Default is "heroku2kafka". An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging.
- **enable.auto.commit** - Default is false. If true the consumer's offset will be periodically committed in the background.
- **auto.offset.reset** - What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server (e.g. because that data has been deleted):
  - **earliest**(default): automatically reset the offset to the earliest offset
    \*latest: automatically reset the offset to the latest offset
  - **none**: throw exception to the consumer if no previous offset is found for the consumer's group
  - anything else: throw exception to the consumer.
- **security.protocol** - Protocol used to communicate with brokers. Valid values are:
  - PLAINTEXT (Default)
  - SSL
  - SASL_PLAINTEXT
  - SASL_SSL
- **ssl.truststore.location** - The location of the trust store file.
- **ssl.truststore.password** - The password for the trust store file.
- **ssl.truststore.type** - The file format of the trust store file. The values currently supported by the default `ssl.engine.factory.class` are [JKS, PKCS12, PEM].
- **ssl.keystore.location** - The location of the key store file. Can be used for two-way authentication for client.
- **ssl.keystore.password** - The store password for the key store file.
- **ssl.keystore.type** - The file format of the key store file. The values currently supported by the default `ssl.engine.factory.class` are [JKS, PKCS12, PEM].
- **ssl.key.password** - The password of the private key in the key store file or the PEM key specified in 'ssl.keystore.key'.
- **ssl.endpoint.identification.algorithm** - The endpoint identification algorithm to validate server hostname using server certificate.
- **sasl.mechanism** - SASL mechanism used for client connections. This may be any mechanism for which a security provider is available. GSSAPI is the default mechanism.
- **sasl.jaas.config** - JAAS login context parameters for SASL connections in the format used by JAAS configuration files. JAAS configuration file format is described <a href=\"https://docs.oracle.com/javase/8/docs/technotes/guides/security/jgss/tutorials/LoginConfigFile.html\">here</a>. The format for the value is: `loginModuleClass controlFlag (optionName=optionValue)*;`. For brokers, the config must be prefixed with listener prefix and SASL mechanism name in lower-case. For example, listener.name.sasl_ssl.scram-sha-256.sasl.jaas.config=com.example.ScramLoginModule required;

## Sink Configurations

### Mandatory

source.bootstrap.servers
source.topic

### Optional

- **source.group.id** - Default is "heroku2kafka". A unique string that identifies the consumer group this consumer belongs to. This property is required if the consumer uses either the group management functionality by using `subscribe(topic)` or the Kafka-based offset management strategy.
- **client.id** - Default is "heroku2kafka". An id string to pass to the server when making requests. The purpose of this is to be able to track the source of requests beyond just ip/port by allowing a logical application name to be included in server-side request logging.
- **acks** - Default is "all". "The number of acknowledgments the producer requires the leader to have received before considering a request complete. This controls the
  - **acks=0** - If set to zero then the producer will not wait for any acknowledgment from the server at all. The record will be immediately added to the socket buffer and considered sent. No guarantee can be made that the server has received the record in this case, and the `retries` configuration will not take effect (as the client won't generally know of any failures). The offset given back for each record will always be set to `-1`.
  - **acks=1** - This will mean the leader will write the record to its local log but will respond without awaiting full acknowledgement from all followers. In this case should the leader fail immediately after acknowledging the record but before the followers have replicated it then the record will be lost.
  - **acks=all** - This means the leader will wait for the full set of in-sync replicas to acknowledge the record. This guarantees that the record will not be lost as long as at least one in-sync replica remains alive. This is the strongest available guarantee. This is equivalent to the acks=-1 setting.
  - Note that enabling idempotence requires this config value to be 'all'. If conflicting configurations are set and idempotence is not explicitly enabled, idempotence is disabled.
- **enable.idempotence** - "When set to 'true', the producer will ensure that exactly one copy of each message is written in the stream. If 'false', producer retries due to broker failures, etc., may write duplicates of the retried message in the stream. Note that enabling idempotence requires `max.in.flight.requests.per.connection` to be less than or equal to " + MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION_FOR_IDEMPOTENCE (with message ordering preserved for any allowable value), `retries` to be greater than 0, and `acks` must be 'all'. Idempotence is enabled by default if no conflicting configurations are set. If conflicting configurations are set and idempotence is not explicitly enabled, idempotence is disabled. If idempotence is explicitly enabled and conflicting configurations are set, a `ConfigException` is thrown.
- **security.protocol** - Protocol used to communicate with brokers. Valid values are:
  - PLAINTEXT (Default)
  - SSL
  - SASL_PLAINTEXT
  - SASL_SSL
- **ssl.truststore.location** - The location of the trust store file.
- **ssl.truststore.password** - The password for the trust store file.
- **ssl.truststore.type** - The file format of the trust store file. The values currently supported by the default `ssl.engine.factory.class` are [JKS, PKCS12, PEM].
- **ssl.keystore.location** - The location of the key store file. Can be used for two-way authentication for client.
- **ssl.keystore.password** - The store password for the key store file.
- **ssl.keystore.type** - The file format of the key store file. The values currently supported by the default `ssl.engine.factory.class` are [JKS, PKCS12, PEM].
- **ssl.key.password** - The password of the private key in the key store file or the PEM key specified in 'ssl.keystore.key'.
- **ssl.endpoint.identification.algorithm** - The endpoint identification algorithm to validate server hostname using server certificate.
- **sasl.mechanism** - SASL mechanism used for client connections. This may be any mechanism for which a security provider is available. GSSAPI is the default mechanism.
- **sasl.jaas.config** - JAAS login context parameters for SASL connections in the format used by JAAS configuration files. JAAS configuration file format is described <a href=\"https://docs.oracle.com/javase/8/docs/technotes/guides/security/jgss/tutorials/LoginConfigFile.html\">here</a>. The format for the value is: `loginModuleClass controlFlag (optionName=optionValue)*;`. For brokers, the config must be prefixed with listener prefix and SASL mechanism name in lower-case. For example, listener.name.sasl_ssl.scram-sha-256.sasl.jaas.config=com.example.ScramLoginModule required;

# Running

To run the software do:
* Build it
```bash
mvn clean package
```
* fill a config file with the mandatory fields. Take the example-config.yaml as a reference.
* run the jar with passing the config file as a argument
```bash
java -jar target/heroku2kafka-0.0.1.jar --spring.config.location=file:./example-config.yaml
```