package io.github.brendonmiranda.bot.clancy.rabbit.listener;

import io.github.brendonmiranda.bot.clancy.dto.AudioTrackMessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class Listener {

	@RabbitListener(id = "foo", queues = "bar")
	public String foo(AudioTrackMessageDTO audioTrackMessage) {
		return audioTrackMessage.getAudioTrackInfoDTO().getTitle();
	}

}
