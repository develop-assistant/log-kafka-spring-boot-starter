package cn.idea360.log.kafka.key;

/**
 * @author cuishiying
 */
public interface KeyStrategy<E> {

	/**
	 * 分片key
	 * @return key变量
	 */
	String key();

	/**
	 * kafka分片策略
	 * @param e 业务ID, 可以为spanId, 保证同一业务顺序性
	 * @return key
	 */
	byte[] createKey(E e);

}
