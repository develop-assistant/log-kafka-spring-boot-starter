package cn.idea360.log.kafka.appender.logback;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import cn.idea360.log.kafka.key.KeyStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author cuishiying
 */
@Getter
@Setter
@Slf4j
public class LogbackKafkaAppender<E> extends UnsynchronizedAppenderBase<E> {

	protected String topic = null;

	protected Encoder<E> encoder = null;

	protected KeyStrategy<? super E> keyStrategy = null;

	protected Producer<byte[], byte[]> producer;

	static KafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory();

	private final Properties config = new Properties();

	public LogbackKafkaAppender(final Map<String, Object> properties) {
		config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		config.setProperty("batch.size", "0");
		config.putAll(properties);
	}

	@Override
	public void start() {
		if (producer == null) {
			producer = producerFactory.newKafkaProducer(config);
		}
		super.start();
	}

	@SuppressWarnings("all")
	@Override
	public void append(E event) {
		if (event == null || !isStarted()) {
			return;
		}
		log.info("send to kafka: {}", event);
		try {
			final byte[] key = keyStrategy.createKey(event);
			final byte[] payload = this.encoder.encode(event);
			if (Objects.nonNull(producer)) {
				ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, key, payload);
				producer.send(record, (metadata, exception) -> {
					if (Objects.nonNull(exception)) {
						System.err.printf("[log-kafka] Unable to send message: [%s], due to: [%s]%n\r\n",
								StandardCharsets.UTF_8.decode(ByteBuffer.wrap(payload)).toString(),
								exception.getMessage());
					}
					else {
						if (log.isDebugEnabled()) {
							System.out.printf(
									"[log-kafka] Sent message: [%s], with partition: [%s] and offset: [%s]\r\n",
									StandardCharsets.UTF_8.decode(ByteBuffer.wrap(payload)).toString(),
									metadata.partition(), metadata.offset());
						}
					}
				});
			}
		}
		catch (Exception e) {
			log.error("[log-kafka] err: [{}]", ExceptionUtils.getStackTrace(e));
		}
	}

}
