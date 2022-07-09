package cn.idea360.log.kafka.enhancer;

import cn.idea360.log.kafka.properties.Log2KafkaProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author cuishiying
 */
public abstract class AbstractLogEnhancer implements LogEnhancer {

	protected Map<String, Object> producerConfigs(Log2KafkaProperties log2KafkaProperties) {
		Map<String, Object> props = new HashMap<>(16);
		// 指定多个kafka集群多个地址 127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				Optional.ofNullable(log2KafkaProperties.getBrokers()).orElse("127.0.0.1:9092"));
		// 重试次数，0为不启用重试机制
		props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
		// sacks=0 把消息发送到kafka就认为发送成功
		// sacks=1 把消息发送到kafka leader分区，并且写入磁盘就认为发送成功
		// sacks=all 把消息发送到kafka leader分区，并且leader分区的副本follower对消息进行了同步就任务发送成功
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		// 生产者空间不足时，send()被阻塞的时间，默认60s
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 6000);
		// 控制批处理大小，单位为字节
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 4096);
		// 批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		// 生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 40960);
		// 消息的最大大小限制,也就是说send的消息大小不能超过这个限制, 默认1048576(1MB)
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1048576);
		// 客户端id
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "log_" + UUID.randomUUID().toString().replace("-", ""));
		// 键的序列化方式
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
		// 值的序列化方式
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
		// 压缩消息，支持四种类型，分别为：none、lz4、gzip、snappy，默认为none。
		// 消费者默认支持解压，所以压缩设置在生产者，消费者无需设置。
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
		return props;
	}

}
