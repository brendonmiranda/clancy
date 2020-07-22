package io.github.brendonmiranda.bot.clancy.converter;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.bot.clancy.dto.AudioTrackMessageDTO;
import io.github.brendonmiranda.bot.clancy.exception.ConversionException;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author brendonmiranda
 */
@Component
public class AudioTrackToAudioTrackMessageDTOConverter implements Converter<AudioTrack, AudioTrackMessageDTO> {

	@Autowired
	private AudioTrackInfoToAudioTrackInfoDTOConverter audioTrackInfoToAudioTrackInfoDTOConverter;

	@Override
	public AudioTrackMessageDTO convert(AudioTrack source) {

		if (source == null)
			throw new ConversionException(
					"An error occurred while converting AudioTrack to AudioTrackMessageDTO: AudioTrack null.");

		Object object = source.getUserData();

		if (object == null || !(object instanceof Guild))
			throw new ConversionException(
					"An error occurred while converting AudioTrack to AudioTrackMessageDTO: Unexpected UserData object.");

		Guild guild = (Guild) object;

		AudioTrackMessageDTO audioTrackMessageDTO = new AudioTrackMessageDTO();
		audioTrackMessageDTO.setAudioTrackInfoDTO(audioTrackInfoToAudioTrackInfoDTOConverter.convert(source.getInfo()));
		audioTrackMessageDTO.setGuildId(guild.getId());

		return audioTrackMessageDTO;
	}

}
