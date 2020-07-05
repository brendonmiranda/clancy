package io.github.brendonmiranda.javabot.dto;

import io.github.brendonmiranda.javabot.service.AudioQueueService;
import org.springframework.amqp.core.Message;

import java.io.Serializable;

/**
 * This object is used as body of a {@link Message} in order to transfer audio track info
 * through the audio queue.
 *
 * @see Message#getBody()
 * @see AudioQueueService#enqueue
 * @see AudioQueueService#receive
 * @author brendonmiranda
 */
public class AudioTrackMessageDTO implements Serializable {

	private static final long serialVersionUID = 4828276047419855403L;

	private String guildId;

	private AudioTrackInfoDTO audioTrackInfoDTO;

	public String getGuildId() {
		return guildId;
	}

	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}

	public AudioTrackInfoDTO getAudioTrackInfoDTO() {
		return audioTrackInfoDTO;
	}

	public void setAudioTrackInfoDTO(AudioTrackInfoDTO audioTrackInfoDTO) {
		this.audioTrackInfoDTO = audioTrackInfoDTO;
	}

}
