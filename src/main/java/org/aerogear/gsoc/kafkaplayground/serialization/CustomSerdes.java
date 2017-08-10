package org.aerogear.gsoc.kafkaplayground.serialization;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Factory for creating serializers / deserializers.
 */
public class CustomSerdes extends Serdes {

    private static final Logger logger = LoggerFactory.getLogger(CustomSerdes.class);

    static public final class GenericSerde<T> extends WrapperSerde<T> {
        public GenericSerde(Class<T> type) {
            super(new GenericSerializer<T>(type), new GenericDeserializer<T>(type));
        }
    }

    static public <T> Serde<T> Generic(Class<T> type) {
        return new GenericSerde(type);
    }

    static public <T> Serde<T> serdeFrom(Class<T> type) {

        // look up default Kafka SerDes
        // if the class type is not supported an exception is thrown
        try {
            return Serdes.serdeFrom(type);
        }
        // If an exception is thrown, use custom generic serdes
        catch (IllegalArgumentException e) {
            logger.warn("Class type is not supported. Using generic serdes");
            return (Serde<T>) Generic(type);
        }

    }

}