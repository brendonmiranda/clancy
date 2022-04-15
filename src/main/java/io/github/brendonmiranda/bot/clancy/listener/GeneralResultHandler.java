package io.github.brendonmiranda.bot.clancy.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.bot.clancy.exception.UnexpectedFlowException;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brendonmiranda
 */
public class GeneralResultHandler implements AudioLoadResultHandler {

	private static final Logger logger = LoggerFactory.getLogger(GeneralResultHandler.class);

	private final AudioPlayer audioPlayer;

	private final Guild guild;

	public GeneralResultHandler(AudioPlayer audioPlayer, Guild guild) {
		this.audioPlayer = audioPlayer;
		this.guild = guild;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		track.setUserData(guild);
		audioPlayer.playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		throw new UnexpectedFlowException("Unexpected flow. Class: GeneralResultHandler, method: playlistLoaded.");
	}

	@Override
	public void noMatches() {
		throw new UnexpectedFlowException("Unexpected flow. Class: GeneralResultHandler, method: noMatches.");
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		logger.error("Loading audio has failed. Severity: {}.", exception.severity);
		throw exception;
	}

}
