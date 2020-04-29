package io.github.brendonmiranda.javabot.listener.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for events of audio loading
 *
 * @author brendonmiranda
 */
public class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {

	private static Logger logger = LoggerFactory.getLogger(AudioLoadResultHandlerImpl.class);

	private AudioPlayer audioPlayer;

	public AudioLoadResultHandlerImpl(final AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	/**
	 * It plays a track loaded
	 * @param track
	 */
	@Override
	public void trackLoaded(final AudioTrack track) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has loaded: title {}, author {}, identifier {}, source {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		audioPlayer.playTrack(track);
	}

	@Override
	public void playlistLoaded(final AudioPlaylist playlist) {

	}

	@Override
	public void noMatches() {

	}

	@Override
	public void loadFailed(final FriendlyException exception) {

	}

}
