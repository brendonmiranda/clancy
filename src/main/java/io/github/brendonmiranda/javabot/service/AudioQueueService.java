package io.github.brendonmiranda.javabot.service;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.javabot.converter.AudioTrackToAudioTrackMessageDTOConverter;
import io.github.brendonmiranda.javabot.dto.AudioTrackMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AudioQueueService extends QueueService<AudioTrack, AudioTrackMessageDTO> {

	@Autowired
	private AudioTrackToAudioTrackMessageDTOConverter audioTrackToAudioTrackMessageDTOConverter;

	public void enqueue(String routingKey, AudioTrack object) {
		if (hasQueue(routingKey))
			rabbitTemplate.convertAndSend(routingKey, audioTrackToAudioTrackMessageDTOConverter.convert(object));
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

}
