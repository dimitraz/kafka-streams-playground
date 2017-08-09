package org.aerogear.gsoc.kafkaplayground.clients;

import org.aerogear.gsoc.kafkaplayground.model.User;
import org.aerogear.gsoc.kafkaplayground.serialization.GenericSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.google.common.io.Resources;
import org.apache.kafka.common.serialization.Serdes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Simple Kafka Producer to produce messages to the <code>produce-users</code> topic,
 * which will be consumed later by the {@link org.aerogear.gsoc.kafkaplayground.BasicStreams} class
 *
 *  @author Dimitra Zuccarelli
 */
public class Producer {

    final private static String OUTPUT_TOPIC = "produce-users";

    public static void main(String[] args) throws IOException {
        Producer producer = new Producer();
        producer.startProducer(OUTPUT_TOPIC);
    }

    private void startProducer(String topic) throws IOException {
        KafkaProducer<String, User> producer;

        // Read in properties file
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            Properties properties = new Properties();

            // Properties for custom serialisers
            properties.put("key.serializer", Serdes.String().serializer().getClass());
            properties.put("value.serializer", GenericSerializer.class.getName());
            properties.put("value.serializer.type", User.class.getName());
            properties.load(props);

            System.out.println("Attempting to connect to bootstrap server: " + properties.getProperty("bootstrap.servers"));
            producer = new KafkaProducer<>(properties);
        }

        // Send messages to topic
        try {
            for (int i = 0; i < 20; i++) {
                User user = new User("Bob", i);
                producer.send(new ProducerRecord<String, User>(topic, Integer.toString(i), user));
                System.out.println(i + " " + user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }

    }
}

