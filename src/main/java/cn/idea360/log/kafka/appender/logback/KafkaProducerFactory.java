package cn.idea360.log.kafka.appender.logback;

import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

/**
 * @author cuishiying
 */
public interface KafkaProducerFactory {

	Producer<byte[], byte[]> newKafkaProducer(Properties config);

}
