package io.github.brendonmiranda.javabot.service;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.javabot.converter.AudioTrackToAudioTrackMessageDTOConverter;
import io.github.brendonmiranda.javabot.dto.AudioTrackInfoDTO;
import io.github.brendonmiranda.javabot.dto.AudioTrackMessageDTO;
import io.github.brendonmiranda.javabot.rabbit.listener.Listener;
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

	@Test
	public void enqueue() throws InterruptedException {
		LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(2);
		AudioTrackMessageDTO audioTrackMessageDTO = getAudioTrackMessageDTO();
		String queueName = "bar";

		rabbitAdmin.purgeQueue(queueName);

		Listener listener = harness.getSpy("foo");
		assertThat(listener).isNotNull();

		doAnswer(answer).when(listener).foo(audioTrackMessageDTO);
		given(audioTrackToAudioTrackMessageDTOConverter.convert(ArgumentMatchers.any(AudioTrack.class)))
				.willReturn(audioTrackMessageDTO);

		service.enqueue(queueName, mock(AudioTrack.class));
		service.enqueue(queueName, mock(AudioTrack.class));

		assertThat(answer.getLatch().await(10, TimeUnit.SECONDS)).isTrue();

		verify(listener, times(2)).foo(audioTrackMessageDTO);
	}

	@Test
	public void destroy_whenQueueExists() {

		Queue queue = QueueBuilder.nonDurable().build();
		String queueName = queue.getName();

		assertThat(rabbitAdmin.declareQueue(queue)).isEqualTo(queueName);
		assertThat(rabbitAdmin.getQueueProperties(queueName)).isNotNull();
		assertThat(service.destroy(queueName)).isTrue();
		assertThat(rabbitAdmin.getQueueProperties(queueName)).isNull();

	}

	@Test
	public void destroy_whenQueueDoesNotExist() {

		String queueName = createRandomQueue();

		assertThat(rabbitAdmin.getQueueProperties(queueName)).isNull();
		assertThat(service.destroy(queueName)).isTrue();
		assertThat(rabbitAdmin.getQueueProperties(queueName)).isNull();

	}

	private String createRandomQueue(){
		Queue queue = QueueBuilder.nonDurable().build();
		return queue.getName();
	}

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
