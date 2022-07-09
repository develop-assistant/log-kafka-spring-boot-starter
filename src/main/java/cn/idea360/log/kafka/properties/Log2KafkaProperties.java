package cn.idea360.log.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.idea360.log.kafka.properties.Log2KafkaProperties.KAFKA_PREFIX;

/**
 * @author cuishiying
 */
@Getter
@Setter
@ConfigurationProperties(value = KAFKA_PREFIX)
public class Log2KafkaProperties {

	public static final String KAFKA_PREFIX = "log-kafka";

	/**
	 * kafka集群地址
	 */
	private String brokers;

	/**
	 * kafka生产日志topic
	 */
	private String topic;

	/**
	 * 日志格式
	 */
	private String pattern;

	/**
	 * 日志包名
	 */
	private String loggerName;

	/**
	 * 日志分片规则, 默认 "${mdc:spanId}"
	 */
	private String key = "${mdc:spanId}";

}
