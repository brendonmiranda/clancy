package io.github.brendonmiranda.javabot.listener.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
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

	private final AudioPlayerManager audioPlayerManager;

	private boolean ytsearch;

	public AudioLoadResultHandlerImpl(AudioPlayer audioPlayer, CommandEvent event,
			AudioPlayerManager audioPlayerManager, boolean ytsearch) {
		this.audioPlayer = audioPlayer;
		this.event = event;
		this.audioPlayerManager = audioPlayerManager;
		this.ytsearch = ytsearch;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has loaded. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		queueTracks(track);
	}

	/**
	 * Plays a track if none is being played otherwise it is enqueued.
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

			if (audioPlayer.getPlayingTrack() == null)
				audioPlayer.playTrack(track);
			else
				queue.add(track);

		}
	}

	/**
	 * Manages the loading result of noMatches method
	 * @param playlist playlist loaded
	 */
	@Override
	public void playlistLoaded(final AudioPlaylist playlist) {
		if (playlist.isSearchResult())
			queueTracks(playlist.getTracks().get(0));
		else
			event.replyError("Sorry, we are unable to load a playlist. Please, contact the bot admin.");
	}

	/**
	 * Loads a "playlist" made of the search results when the attempt of loading on
	 * PlayCmd doesn't match. In case of fail it returns a error message.
	 */
	@Override
	public void noMatches() {
		// conditional to avoid loop
		if (!ytsearch)
			audioPlayerManager.loadItem("ytsearch:" + event.getArgs(),
					new AudioLoadResultHandlerImpl(audioPlayer, event, audioPlayerManager, Boolean.TRUE));
		else
			event.replyError("Sorry, we were unable to achieve your media. Please, rephrase and try again.");
	}

	@Override
	public void loadFailed(final FriendlyException exception) {
	}

}
