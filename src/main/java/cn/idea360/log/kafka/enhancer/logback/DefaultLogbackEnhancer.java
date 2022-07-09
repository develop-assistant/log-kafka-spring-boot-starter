package cn.idea360.log.kafka.enhancer.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.OptionHelper;
import cn.idea360.log.kafka.appender.logback.LogbackKafkaAppender;
import cn.idea360.log.kafka.enhancer.AbstractLogEnhancer;
import cn.idea360.log.kafka.key.KeyStrategy;
import cn.idea360.log.kafka.key.KeyStrategyEnums;
import cn.idea360.log.kafka.key.SpanIdKeyStrategy;
import cn.idea360.log.kafka.properties.Log2KafkaProperties;
import cn.idea360.log.kafka.utils.LogEnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.ILoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cuishiying
 */
@Slf4j
public class DefaultLogbackEnhancer extends AbstractLogEnhancer {

	private static final String CONSOLE_LOG_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

	private static final String LOG_NAME = org.slf4j.Logger.ROOT_LOGGER_NAME;

	private final LoggerContext context = this.getLoggerContext();

	protected Log2KafkaProperties log2KafkaProperties;

	private final Map<String, KeyStrategy<ILoggingEvent>> keyStrategyMap = new ConcurrentHashMap<>();

	public DefaultLogbackEnhancer(Log2KafkaProperties log2KafkaProperties) {
		this.log2KafkaProperties = log2KafkaProperties;
		for (KeyStrategyEnums keyStrategyEnums : KeyStrategyEnums.values()) {
			KeyStrategy<ILoggingEvent> keyStrategy = keyStrategyEnums.newInstance();
			keyStrategyMap.put(keyStrategy.key(), keyStrategy);
		}
		this.enhancer();
	}

	@Override
	public void enhancer() {
		log.info("logback is loading: {}", LogEnvUtils.isLogbackUsable());

		// set encoder
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		String logPattern = Optional.ofNullable(log2KafkaProperties.getPattern()).orElse(CONSOLE_LOG_PATTERN);
		encoder.setPattern(OptionHelper.substVars(logPattern, context));
		encoder.setContext(context);
		encoder.start();

		// set appender
		LogbackKafkaAppender<ILoggingEvent> appender = new LogbackKafkaAppender<>(producerConfigs(log2KafkaProperties));
		appender.setEncoder(encoder);
		appender.setName("KAFKA");
		appender.setContext(context);
		appender.start();

		// set kafka config
		appender.setTopic(log2KafkaProperties.getTopic());
		appender.setKeyStrategy(keyStrategyMap.getOrDefault(log2KafkaProperties.getKey(), new SpanIdKeyStrategy()));

		// xml中不用再配置appender
		Logger logger = this.context
				.getLogger(Optional.ofNullable(log2KafkaProperties.getLoggerName()).orElse("cn.idea360"));
		logger.setLevel(Level.INFO);
		logger.addAppender(appender);
	}

	private LoggerContext getLoggerContext() {
		ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();

		if (factory instanceof LoggerContext) {

			return (LoggerContext) factory;
		}
		throw new IllegalStateException(
				"ILoggerFactory is not a Logback LoggerContext, but Logback is on the classpath.");
	}

	public void addKeyStrategy(KeyStrategy<ILoggingEvent> keyStrategy) {
		keyStrategyMap.put(keyStrategy.key(), keyStrategy);
	}

}
