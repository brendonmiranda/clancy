package io.github.brendonmiranda.javabot.converter;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.brendonmiranda.javabot.dto.AudioTrackInfoDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AudioTrackInfoToAudioTrackInfoDTOConverter implements Converter<AudioTrackInfo, AudioTrackInfoDTO> {

	@Override
	public AudioTrackInfoDTO convert(AudioTrackInfo source) {

		if (source == null)
			throw new RuntimeException(); // todo: throw exception

		AudioTrackInfoDTO audioTrackInfoDTO = new AudioTrackInfoDTO();
		audioTrackInfoDTO.setTitle(source.title);
		audioTrackInfoDTO.setAuthor(source.author);
		audioTrackInfoDTO.setLength(source.length);
		audioTrackInfoDTO.setIdentifier(source.identifier);
		audioTrackInfoDTO.setStream(source.isStream);
		audioTrackInfoDTO.setUri(source.uri);

		return audioTrackInfoDTO;
	}

}
