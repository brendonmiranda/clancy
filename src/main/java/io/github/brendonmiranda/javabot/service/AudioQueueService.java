package io.github.brendonmiranda.javabot.service;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.javabot.converter.AudioTrackToAudioTrackMessageDTOConverter;
import io.github.brendonmiranda.javabot.dto.AudioTrackMessageDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AudioQueueService {

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private AudioTrackToAudioTrackMessageDTOConverter audioTrackToAudioTrackMessageDTOConverter;

	public void enqueue(String routingKey, AudioTrack audioTrack) {
		if (hasQueue(routingKey))
			rabbitTemplate.convertAndSend(routingKey, audioTrackToAudioTrackMessageDTOConverter.convert(audioTrack));
		else
			throw new RuntimeException(); // todo: throw custom exception
	}

	public AudioTrackMessageDTO receive(String queueName) {
		Object object = rabbitTemplate.receiveAndConvert(queueName);

		if (object != null && object instanceof AudioTrackMessageDTO) {
			return (AudioTrackMessageDTO) object;
		}

		return null;
	}

	/**
	 * Checks if a queue exists. If the queue doesn't exist it tries to create it.
	 * @param queueName
	 * @return
	 */
	private boolean hasQueue(String queueName) {
		// getQueueProperties() can be used to determine if a queue exists on the broker
		if (rabbitAdmin.getQueueProperties(queueName) == null)
			return createQueue(queueName) == null ? false : true;
		else
			return true;
	}

	/**
	 * Creates a queue on the broker and returns the queue name if successful, otherwise
	 * it returns null.
	 * @return queueName
	 */
	private String createQueue(String queueName) {
		return rabbitAdmin.declareQueue(new Queue(queueName));
	}

}
