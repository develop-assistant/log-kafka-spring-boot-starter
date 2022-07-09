package cn.idea360.log.kafka.configuration;

import ch.qos.logback.core.encoder.Encoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cuishiying
 */
public class KafkaAppenderConfig {

	protected String topic = null;

	protected Encoder encoder = null;

	protected Map<String, Object> producerConfig = new HashMap();

}
