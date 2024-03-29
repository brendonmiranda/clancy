package io.github.brendonmiranda.bot.clancy.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.brendonmiranda.bot.clancy.dto.AudioTrackMessageDTO;
import io.github.brendonmiranda.bot.clancy.exception.AudioTrackException;
import io.github.brendonmiranda.bot.clancy.service.AudioQueueService;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener for events of audio reproducing
 *
 * @author brendonmiranda
 */
@Component
public class AudioEventListener extends AudioEventAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AudioEventListener.class);

	@Autowired
	private AudioQueueService audioQueueService;

	@Autowired
	private AudioPlayerManager audioPlayerManager;

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has started. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has ended. Reason: {}, title: {}, author: {}, identifier: {}, source: {}", endReason.name(),
				audioTrackInfo.title, audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		Guild guild = (Guild) track.getUserData();

		if (endReason.equals(AudioTrackEndReason.LOAD_FAILED))
			logger.error("Unable to load the audio, this means that the track failed to start.");

		if (!endReason.equals(AudioTrackEndReason.STOPPED)) {
			// get the next audio in the queue if there is one
			AudioTrackMessageDTO audioTrackMessage = audioQueueService.receive(guild.getName());

			if (audioTrackMessage != null) {
				logger.info("Loading the next track in the queue. Title: {}, author: {}, identifier: {}",
						audioTrackMessage.getAudioTrackInfoDTO().getTitle(),
						audioTrackMessage.getAudioTrackInfoDTO().getAuthor(),
						audioTrackMessage.getAudioTrackInfoDTO().getIdentifier());
				// plays it
				audioPlayerManager.loadItem(audioTrackMessage.getAudioTrackInfoDTO().getIdentifier(),
						new GeneralResultHandler(player, guild));
			}
		}

	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		logger.debug("Player has paused");
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		logger.debug("Player has resumed");
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track got stuck. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		throw new AudioTrackException("Track got stuck. Audio track title: " + audioTrackInfo.title);
	}

}