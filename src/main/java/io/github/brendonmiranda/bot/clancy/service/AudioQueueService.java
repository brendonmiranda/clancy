package io.github.brendonmiranda.bot.clancy.service;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.bot.clancy.configuration.RabbitConfiguration;
import io.github.brendonmiranda.bot.clancy.converter.AudioTrackToAudioTrackMessageDTOConverter;
import io.github.brendonmiranda.bot.clancy.dto.AudioTrackMessageDTO;
import io.github.brendonmiranda.bot.clancy.exception.AudioQueueException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Provides functionalities to manage audio queues through the bot. This is a
 * implementation of rabbit-mq features from Spring AMQP.
 *
 * @author brendonmiranda
 * @see <a href="https://docs.spring.io/spring-amqp/docs/2.2.7.RELEASE/reference/html"/>
 */
@Service
public class AudioQueueService {

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private AudioTrackToAudioTrackMessageDTOConverter audioTrackToAudioTrackMessageDTOConverter;

	@Value("${rabbit.queue.ttl}")
	private int ttlQueue;

	/**
	 * Enqueues an audio track message generated from the provided audio track object in
	 * the specified queue. The routing key must be the queue name given the default
	 * binding agreed in the configuration.
	 * @param routingKey queue name
	 * @param object audio track
	 * @see RabbitConfiguration#rabbitAdmin
	 * @see AudioTrackToAudioTrackMessageDTOConverter
	 * @see AudioTrackMessageDTO
	 */
	public void enqueue(String routingKey, AudioTrack object) {
		if (hasQueue(routingKey, true))
			rabbitTemplate.convertAndSend(routingKey, audioTrackToAudioTrackMessageDTOConverter.convert(object));
		else
			throw new AudioQueueException("An error occurred trying to reach or create a queue. RoutingKey: "
					+ routingKey + ", audio track title: " + object.getInfo().title);
	}

	/**
	 * Receives an audio track message from the given queue if it exists, otherwise it
	 * returns null.
	 * @param queueName
	 * @return audio track message
	 */
	public AudioTrackMessageDTO receive(String queueName) {
		Object object = hasQueue(queueName, false) ? rabbitTemplate.receiveAndConvert(queueName) : null;

		if (object != null && object instanceof AudioTrackMessageDTO) {
			return (AudioTrackMessageDTO) object;
		}

		return null;
	}

	/**
	 * Deletes a queue by the name.
	 * @param queueName queue's name
	 * @return true if the queue was deleted or it does not exist
	 */
	public boolean destroy(String queueName) {
		return rabbitAdmin.deleteQueue(queueName);
	}

	/**
	 * Checks if a queue exists. If the queue doesn't exist the createIfNotExist variable
	 * decides if it must be created or not.
	 * @param queueName
	 * @return
	 */
	private boolean hasQueue(String queueName, boolean createIfNotExist) {
		// getQueueProperties() can be used to determine if a queue exists on the broker
		if (rabbitAdmin.getQueueProperties(queueName) == null) {

			if (createIfNotExist)
				return createQueue(queueName) == null ? false : true;
			else
				return false;

		}
		else {
			return true;
		}
	}

	/**
	 * Creates a queue on the broker and returns the queue name if successful, otherwise
	 * it returns null.
	 *
	 * It creates a durable queue which will survive a server restart. Also, it sets the
	 * time (x-expires argument) that the queue can remain unused before being deleted.
	 *
	 * @see <a href="https://www.rabbitmq.com/ttl.html#queue-ttl" />
	 * @return queueName
	 */
	private String createQueue(String queueName) {
		Queue queue = QueueBuilder.durable(queueName).expires(ttlQueue).build();

		return rabbitAdmin.declareQueue(queue);
	}

}
