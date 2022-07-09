package cn.idea360.log.kafka;

import cn.idea360.log.kafka.enhancer.LogEnhancer;
import cn.idea360.log.kafka.enhancer.log4j2.DefaultLog4j2Enhancer;
import cn.idea360.log.kafka.enhancer.logback.DefaultLogbackEnhancer;
import cn.idea360.log.kafka.properties.Log2KafkaProperties;
import cn.idea360.log.kafka.utils.LogEnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cuishiying
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(Log2KafkaProperties.class)
public class Log2KafkaAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean({ LogEnhancer.class })
	public LogEnhancer logEnhancer(Log2KafkaProperties log2KafkaProperties) {
		if (LogEnvUtils.isLogbackUsable()) {
			return new DefaultLogbackEnhancer(log2KafkaProperties);
		}
		else if (LogEnvUtils.isLog4j2Usable()) {
			return new DefaultLog4j2Enhancer(log2KafkaProperties);
		}
		else {
			throw new IllegalStateException("slf4j to kafka config error");
		}
	}

}
