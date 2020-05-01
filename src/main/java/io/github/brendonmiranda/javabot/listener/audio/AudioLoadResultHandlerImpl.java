package io.github.brendonmiranda.javabot.listener.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.brendonmiranda.javabot.listener.audio.AudioEventListener.queue;

/**
 * Handler for events of audio loading
 *
 * @author brendonmiranda
 */
public class AudioLoadResultHandlerImpl implements AudioLoadResultHandler {

	private static final Logger logger = LoggerFactory.getLogger(AudioLoadResultHandlerImpl.class);

	private final AudioPlayer audioPlayer;

	private final CommandEvent event;

	public AudioLoadResultHandlerImpl(AudioPlayer audioPlayer, CommandEvent event) {
		this.audioPlayer = audioPlayer;
		this.event = event;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.debug("Track has loaded. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		queueTracks(track);
	}

	/**
	 * todo: refactor that method. Plays a track if none is being played otherwise it is
	 * enqueued
	 * @param track audio track
	 */
	public void queueTracks(AudioTrack track) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();

		if (audioSendHandler == null) {
			event.getGuild().getAudioManager().setSendingHandler(new AudioSendHandlerImpl(audioPlayer));
			audioPlayer.playTrack(track);
		}
		else {
			AudioPlayer audioPlayer = audioSendHandler.getAudioPlayer();

			if (audioPlayer.getPlayingTrack() == null) {
				audioPlayer.playTrack(track);
			}
			else {
				queue.add(track);
			}

		}
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
