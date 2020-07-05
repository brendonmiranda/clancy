package io.github.brendonmiranda.javabot.converter;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.brendonmiranda.javabot.dto.AudioTrackInfoDTO;

import io.github.brendonmiranda.javabot.exception.ConversionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author brendonmiranda
 */
@Component
public class AudioTrackInfoToAudioTrackInfoDTOConverter implements Converter<AudioTrackInfo, AudioTrackInfoDTO> {

	@Override
	public AudioTrackInfoDTO convert(AudioTrackInfo source) {

		if (source == null)
			throw new ConversionException(
					"An error occurred while converting AudioTrackInfo to AudioTrackInfoDTO: AudioTrackInfo null.");

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
