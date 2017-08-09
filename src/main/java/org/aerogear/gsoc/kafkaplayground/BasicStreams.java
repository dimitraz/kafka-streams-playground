package org.aerogear.gsoc.kafkaplayground;

import org.aerogear.gsoc.kafkaplayground.model.User;
import org.aerogear.gsoc.kafkaplayground.serialization.UserSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;

/**
 * Testing basic streams functionality by printing each record in the
 * <code>produce-users</code> topic, which is "populated" by running the
 * {@link org.aerogear.gsoc.kafkaplayground.clients.Producer}
 */
public class BasicStreams {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testing-basic-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "172.18.0.3:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, UserSerde.class);

        KStreamBuilder builder = new KStreamBuilder();

        // Read from the source stream
        KStream<String, User> source = builder.stream("produce-users");

        // Print each key value pair
        source.foreach((key, value) -> System.out.println(key + ": " + value));

        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();
    }
}