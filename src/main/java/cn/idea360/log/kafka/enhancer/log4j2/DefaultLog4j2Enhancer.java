package cn.idea360.log.kafka.enhancer.log4j2;

import cn.idea360.log.kafka.enhancer.AbstractLogEnhancer;
import cn.idea360.log.kafka.properties.Log2KafkaProperties;
import cn.idea360.log.kafka.utils.LogEnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author cuishiying
 */
@Slf4j
public class DefaultLog4j2Enhancer extends AbstractLogEnhancer {

	private static final String CONSOLE_LOG_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

	protected Log2KafkaProperties log2KafkaProperties;

	private final LoggerContext context = this.getLoggerContext();

	public DefaultLog4j2Enhancer(Log2KafkaProperties log2KafkaProperties) {
		this.log2KafkaProperties = log2KafkaProperties;
		this.enhancer();
	}

	@Override
	public void enhancer() {
		log.info("log4j2 is loading: {}", LogEnvUtils.isLog4j2Usable());

		final Configuration configuration = context.getConfiguration();

		// set layout
		final Layout<? extends Serializable> layout = PatternLayout.newBuilder()
				.withPattern(Optional.of(CONSOLE_LOG_PATTERN).orElse(DefaultConfiguration.DEFAULT_PATTERN))
				.withConfiguration(configuration).build();

		// set kafka config
		Map<String, Object> producerConfigs = producerConfigs(log2KafkaProperties);
		List<Property> props = new ArrayList<>();
		producerConfigs.forEach((k, v) -> props.add(Property.createProperty(k, String.valueOf(v))));

		// set appender
		KafkaAppender appender = KafkaAppender.newBuilder().setConfiguration(configuration).setName("KAFKA")
				.setTopic(log2KafkaProperties.getTopic()).setPropertyArray(props.toArray(new Property[0]))
				.setKey(log2KafkaProperties.getKey()).setLayout(layout).setSyncSend(false).build();
		appender.start();
		configuration.addAppender(appender);

		// xml中不用再配置appender
		Logger logger = this.context
				.getLogger(Optional.ofNullable(log2KafkaProperties.getLoggerName()).orElse("cn.idea360"));
		logger.setLevel(Level.INFO);
		logger.addAppender(appender);
	}

	private LoggerContext getLoggerContext() {
		return (LoggerContext) LogManager.getContext(false);
	}

}
