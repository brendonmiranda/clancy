package io.github.brendonmiranda.javabot.service;

import io.github.brendonmiranda.javabot.dto.AudioTrackMessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class Listener {

	@RabbitListener(id = "foo", queues = "bar")
	public String foo(AudioTrackMessageDTO audioTrackMessage) {
		return audioTrackMessage.getAudioTrackInfoDTO().getTitle();
	}

}
