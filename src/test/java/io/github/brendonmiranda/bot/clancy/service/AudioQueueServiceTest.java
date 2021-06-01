package io.github.brendonmiranda.bot.clancy.service;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.bot.clancy.converter.AudioTrackToAudioTrackMessageDTOConverter;
import io.github.brendonmiranda.bot.clancy.dto.AudioTrackInfoDTO;
import io.github.brendonmiranda.bot.clancy.dto.AudioTrackMessageDTO;
import io.github.brendonmiranda.bot.clancy.rabbit.configuration.Config;
import io.github.brendonmiranda.bot.clancy.rabbit.listener.Listener;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Integration tests for @{@link AudioQueueService} which is implementing rabbit-mq from
 * Spring AQMP in order to provide message queuing functionalities.
 *
 * @author brendonmiranda
 * @see <a href=
 * "https://docs.spring.io/spring-amqp/docs/2.2.7.RELEASE/reference/html/#testing"/>
 */
@SpringBootTest
public class AudioQueueServiceTest {

	@Autowired
	private AudioQueueService service;

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitListenerTestHarness harness;

	@MockBean
	private AudioTrackToAudioTrackMessageDTOConverter audioTrackToAudioTrackMessageDTOConverter;

	/**
	 * Enqueues two messages in a previously created queue {@link Config#myQueue()} and
	 * ensure that was successfully sent by receiving them in the queue listener
	 * {@link Config#listener()}.
	 *
	 * The goal is test {@link AudioQueueService#enqueue} which works asynchronously, so
	 * we are using some Spring AMQP exclusive objects which is helping us to test it that
	 * way.
	 * @throws InterruptedException
	 * @see <a href=
	 * "https://docs.spring.io/spring-amqp/docs/2.2.7.RELEASE/reference/html/#mockito-answer"/>
	 */
	@Test
	public void enqueue() throws InterruptedException {
		LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(2);
		AudioTrackMessageDTO audioTrackMessageDTO = getAudioTrackMessageDTO();
		String queueName = "bar";

		rabbitAdmin.purgeQueue(queueName);

		Listener listener = harness.getSpy("foo");
		assertThat(listener).isNotNull();

		doAnswer(answer).when(listener).foo(audioTrackMessageDTO); // count down
		given(audioTrackToAudioTrackMessageDTOConverter.convert(ArgumentMatchers.any(AudioTrack.class)))
				.willReturn(audioTrackMessageDTO);

		service.enqueue(queueName, mock(AudioTrack.class));
		service.enqueue(queueName, mock(AudioTrack.class));

		assertThat(answer.getLatch().await(10, TimeUnit.SECONDS)).isTrue(); // latch

		// ensure the messages has been received
		verify(listener, times(2)).foo(audioTrackMessageDTO);
	}

	/**
	 * It declares a random queue, destroy it in order to test
	 * {@link AudioQueueService#destroy} and assert that it was deleted.
	 */
	@Test
	public void destroy_whenQueueExists() {

		Queue queue = QueueBuilder.nonDurable().build();
		String queueName = queue.getName();

		assertThat(rabbitAdmin.declareQueue(queue)).isEqualTo(queueName);

		// determines if the queue exists on the broker
		assertThat(rabbitAdmin.getQueueProperties(queueName)).isNotNull();

		assertThat(service.destroy(queueName)).isTrue();
		assertThat(rabbitAdmin.getQueueProperties(queueName)).isNull();

	}

	/**
	 * Tries to destroy a queue which does not exist in order to assert the return from
	 * {@link AudioQueueService#destroy}.
	 */
	@Test
	public void destroy_whenQueueDoesNotExist() {

		String queueName = generateUniqueQueueName();

		assertThat(rabbitAdmin.getQueueProperties(queueName)).isNull();
		assertThat(service.destroy(queueName)).isTrue();

	}

	/**
	 * It generates a unique queue name.
	 * @return queue name
	 */
	private String generateUniqueQueueName() {
		Queue queue = QueueBuilder.nonDurable().build();
		return queue.getName();
	}

	/**
	 * Stub audio track message object for testing.
	 * @return stub audio track message dto object
	 */
	private AudioTrackMessageDTO getAudioTrackMessageDTO() {
		AudioTrackInfoDTO trackInfo = new AudioTrackInfoDTO();
		trackInfo.setTitle("Tentative");
		trackInfo.setAuthor("System of a Down");
		trackInfo.setLength(201600L);
		trackInfo.setIdentifier("Qxxum5ungsA");
		trackInfo.setStream(true);
		trackInfo.setUri("");

		AudioTrackMessageDTO trackMessage = new AudioTrackMessageDTO();
		trackMessage.setAudioTrackInfoDTO(trackInfo);
		trackMessage.setGuildId("0");

		return trackMessage;
	}

}
