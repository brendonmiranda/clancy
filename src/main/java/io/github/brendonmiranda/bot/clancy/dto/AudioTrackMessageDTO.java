package io.github.brendonmiranda.bot.clancy.dto;

import io.github.brendonmiranda.bot.clancy.service.AudioQueueService;
import org.springframework.amqp.core.Message;

import java.io.Serializable;
import java.util.Objects;

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

	@Override
	public String toString() {
		return "AudioTrackMessageDTO{" + "guildId='" + guildId + '\'' + ", audioTrackInfoDTO=" + audioTrackInfoDTO
				+ '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AudioTrackMessageDTO that = (AudioTrackMessageDTO) o;
		return Objects.equals(guildId, that.guildId) && Objects.equals(audioTrackInfoDTO, that.audioTrackInfoDTO);
	}

	@Override
	public int hashCode() {
		return Objects.hash(guildId, audioTrackInfoDTO);
	}

}
