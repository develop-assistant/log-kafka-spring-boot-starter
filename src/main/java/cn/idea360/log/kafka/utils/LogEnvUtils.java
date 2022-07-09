package cn.idea360.log.kafka.utils;

/**
 * @author cuishiying
 */
public class LogEnvUtils {

	private static final String LOGBACK_SPI = "ch.qos.logback.classic.LoggerContext";

	private static final String LOG4J2_SPI = "org.apache.logging.slf4j.Log4jLoggerFactory";

	private LogEnvUtils() {
	}

	private static boolean isPresent(String className) {
		try {
			Class.forName(className, false, LogEnvUtils.class.getClassLoader());
			return true;
		}
		catch (ClassNotFoundException ex) {
			return false;
		}
	}

	public static boolean isLogbackUsable() {
		return isPresent(LOGBACK_SPI);
	}

	public static boolean isLog4j2Usable() {
		return isPresent(LOG4J2_SPI);
	}

}
