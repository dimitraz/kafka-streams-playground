package org.aerogear.gsoc.kafkaplayground;


import com.eneco.trading.kafka.connect.twitter.domain.TwitterStatus;
import org.aerogear.gsoc.kafkaplayground.serialization.GenericDeserializer;
import org.aerogear.gsoc.kafkaplayground.serialization.GenericSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;

public class TwitterStreams {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-twitter-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Initialize specific serializers to be used later
        Deserializer<TwitterStatus> twitDe = new GenericDeserializer<>();
        Serializer<TwitterStatus> twitSe = new GenericSerializer<>();

        final Serde<TwitterStatus> twitter_serde = Serdes.serdeFrom(twitSe, twitDe);
        final Serde<String> string_serde = Serdes.String();

        KStreamBuilder builder = new KStreamBuilder();

        // read from the source stream
        KStream<String, TwitterStatus> tweetFeed = builder.stream("twitter-test");

        /*KStream<String, String> splitTweets = tweetFeed.map(new KeyValueMapper<String, GenericRecord, KeyValue<String, String>>() {
            @Override
            public KeyValue<String, String> apply(String key, GenericRecord value) {
                return new KeyValue<>(value.get("id").toString(), value.get("text").toString());
            }
        }).to(string_serde, string_serde,"wordcount");
        KStream<String, TwitterStatus> splitTweets = tweetFeed.map((key, value) -> new KeyValue<>(value.get("id").toString(), value.get("text").toString()));
        splitTweets.to(string_serde, twitter_serde, "test-output");
        */

        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();
    }
}