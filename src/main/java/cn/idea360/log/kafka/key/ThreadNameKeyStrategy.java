package cn.idea360.log.kafka.key;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.nio.ByteBuffer;

/**
 * @author cuishiying
 */
public class ThreadNameKeyStrategy implements KeyStrategy<ILoggingEvent> {

	@Override
	public String key() {
		return "threadName";
	}

	@Override
	public byte[] createKey(ILoggingEvent e) {
		return ByteBuffer.allocate(4).putInt(e.getThreadName().hashCode()).array();
	}

}
