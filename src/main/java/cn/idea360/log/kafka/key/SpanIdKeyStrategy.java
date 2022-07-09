package cn.idea360.log.kafka.key;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author cuishiying
 */
public class SpanIdKeyStrategy implements KeyStrategy<ILoggingEvent> {

	@Override
	public String key() {
		return "${mdc:spanId}";
	}

	@Override
	public byte[] createKey(ILoggingEvent e) {
		final String traceId;
		Map<String, String> mdcPropertyMap = e.getMDCPropertyMap();
		if (mdcPropertyMap == null) {
			traceId = "";
		}
		else {
			traceId = mdcPropertyMap.getOrDefault("spanId", "");
		}
		return ByteBuffer.allocate(4).putInt(traceId.hashCode()).array();
	}

}
