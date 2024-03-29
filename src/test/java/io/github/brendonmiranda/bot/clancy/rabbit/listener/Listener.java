package io.github.brendonmiranda.bot.clancy.rabbit.listener;

import io.github.brendonmiranda.bot.clancy.dto.AudioTrackMessageDTO;
import org.assertj.core.api.Assertions;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Wrapper for queue listeners. You can implement here new queue listeners in order to use
 * it on the tests.
 *
 * @author brendonmiranda
 * @see <a href=
 * "https://docs.spring.io/spring-amqp/docs/2.2.7.RELEASE/reference/html/#test-harness"/>
 */
public class Listener {

	@RabbitListener(id = "foo", queues = "bar")
	public void foo(AudioTrackMessageDTO audioTrackMessage) {

		String title = audioTrackMessage.getAudioTrackInfoDTO().getTitle();
		Assertions.assertThat(title).isNotNull();
	}

}
