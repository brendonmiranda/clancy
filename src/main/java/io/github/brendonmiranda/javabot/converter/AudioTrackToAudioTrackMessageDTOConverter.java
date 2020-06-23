package io.github.brendonmiranda.javabot.converter;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.javabot.dto.AudioTrackMessageDTO;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AudioTrackToAudioTrackMessageDTOConverter implements Converter<AudioTrack, AudioTrackMessageDTO> {

	@Autowired
	private AudioTrackInfoToAudioTrackInfoDTOConverter audioTrackInfoToAudioTrackInfoDTOConverter;

	@Override
	public AudioTrackMessageDTO convert(AudioTrack source) {

		if (source == null)
			throw new RuntimeException(); // todo: throw exception

		Object object = source.getUserData();

		if (object == null || !(object instanceof Guild))
			throw new RuntimeException(); // todo: throw exception

		Guild guild = (Guild) object;

		AudioTrackMessageDTO audioTrackMessageDTO = new AudioTrackMessageDTO();
		audioTrackMessageDTO.setAudioTrackInfoDTO(audioTrackInfoToAudioTrackInfoDTOConverter.convert(source.getInfo()));
		audioTrackMessageDTO.setGuildId(guild.getId());

		return audioTrackMessageDTO;
	}

}
