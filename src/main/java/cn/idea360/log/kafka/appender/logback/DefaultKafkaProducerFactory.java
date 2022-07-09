package cn.idea360.log.kafka.appender.logback;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

/**
 * @author cuishiying
 */
public class DefaultKafkaProducerFactory implements KafkaProducerFactory {

	@Override
	public Producer<byte[], byte[]> newKafkaProducer(final Properties config) {
		return new KafkaProducer<>(config);
	}

}
