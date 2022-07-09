package cn.idea360.log.kafka.key;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.objenesis.instantiator.util.ClassUtils;

/**
 * @author cuishiying
 */
public enum KeyStrategyEnums {

	/**
	 * 基于${mdc:spanId}
	 */
	SPAN_ID(SpanIdKeyStrategy.class),

	/**
	 * 基于线程名
	 */
	THREAD_NAME(ThreadNameKeyStrategy.class);

	/**
	 * 分片策略
	 */
	private final Class<? extends KeyStrategy<ILoggingEvent>> keyStrategyClass;

	KeyStrategyEnums(Class<? extends KeyStrategy<ILoggingEvent>> keyStrategyClass) {
		this.keyStrategyClass = keyStrategyClass;
	}

	public KeyStrategy<ILoggingEvent> newInstance() {
		return ClassUtils.newInstance(this.keyStrategyClass);
	}

}
