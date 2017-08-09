package org.aerogear.gsoc.kafkaplayground.serialization;

import org.aerogear.gsoc.kafkaplayground.model.User;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserSerde implements Serde<User> {
    final private Serializer<User> serializer = new GenericSerializer<>(User.class);
    final private Deserializer<User> deserializer = new GenericDeserializer<>(User.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer<User> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<User> deserializer() {
        return deserializer;
    }
}