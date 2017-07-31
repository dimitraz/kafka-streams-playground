package org.aerogear.gsoc.kafkaplayground;

import com.eneco.trading.kafka.connect.twitter.domain.TwitterStatus;
import com.google.common.io.Resources;

import org.aerogear.gsoc.kafkaplayground.utils.GenericDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import java.util.Random;

/**
 * Simple Kafka Consumer class to verify the Streams are
 * working as expected
 */
public class Consumer {

    private final static String OUTPUT_TOPIC = "wordcount5m";

    public static void main(String[] args) throws IOException {
        KafkaConsumer<String, TwitterStatus> consumer;

        // Load in consumer.props file
        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            Properties properties = new Properties();

            // Properties for custom deserialisers
            properties.put("key.deserializer", Serdes.String().deserializer().getClass());
            properties.put("value.deserializer", GenericDeserializer.class.getName());
            properties.put("value.deserializer.type", TwitterStatus.class.getName());

            properties.load(props);
            if (properties.getProperty("group.id") == null) {
                properties.setProperty("group.id", "group-" + new Random().nextInt(100000));
            }
            consumer = new KafkaConsumer<>(properties);
        }

        // Subscribe to topics and start consuming
        consumer.subscribe(Arrays.asList(OUTPUT_TOPIC));

        try {
            while (true) {
                ConsumerRecords<String, TwitterStatus> records = consumer.poll(200);
                records.forEach((record) -> System.out.println(record.offset() + " " + record.value()));
            }
        } catch (Exception e) {
            // Handle later
        } finally {
            consumer.close();
        }
    }
}
