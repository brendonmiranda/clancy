package io.github.brendonmiranda.javabot.listener.audio;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

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

	private final EventWaiter eventWaiter;

	private final Message message;

	private final OrderedMenu.Builder builder;

	private boolean ytsearch;

	public AudioLoadResultHandlerImpl(AudioPlayer audioPlayer, CommandEvent event,
			AudioPlayerManager audioPlayerManager, EventWaiter eventWaiter, Message message, boolean ytsearch) {
		this.audioPlayer = audioPlayer;
		this.event = event;
		this.audioPlayerManager = audioPlayerManager;
		this.eventWaiter = eventWaiter;
		this.message = message;
		this.ytsearch = ytsearch;
		this.builder = new OrderedMenu.Builder().allowTextInput(true).useNumbers().useCancelButton(true)
				.setEventWaiter(eventWaiter).setTimeout(1, TimeUnit.MINUTES);
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
		track.setUserData(event.getGuild()); // object to be recovered when necessary (Guild). Currently it is being used on AudioEventListener

		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();

		if (audioSendHandler == null) {
			event.getGuild().getAudioManager().setSendingHandler(new AudioSendHandlerImpl(audioPlayer));
			audioPlayer.playTrack(track);

			event.reply("Playing **" + audioPlayer.getPlayingTrack().getInfo().title + "**.");
		}
		else {
			AudioPlayer audioPlayer = audioSendHandler.getAudioPlayer();

			if (audioPlayer.getPlayingTrack() == null) {

				audioPlayer.playTrack(track);

				event.reply("Playing **" + audioPlayer.getPlayingTrack().getInfo().title + "**.");

			}
			else {

				if (audioPlayer.isPaused()) {
					event.replyWarning(
							"The track **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title
									+ "** is paused. Type `" + event.getClient().getPrefix() + "resume` to unpause!");
				}

				queue.add(track);
				event.reply("Enqueued **" + track.getInfo().title + "**.");
			}
		}
	}

	/**
	 * Manages the loading result of noMatches method
	 * @param playlist playlist loaded
	 */
	@Override
	public void playlistLoaded(final AudioPlaylist playlist) {

		if (playlist.isSearchResult()) {

			builder.setText("Search results for **" + event.getArgs() + "**:").setSelection((msg, i) -> {
				AudioTrack audioTrack = playlist.getTracks().get(i - 1);
				queueTracks(audioTrack);
			}).setCancel((msg) -> {
			}).setUsers(event.getAuthor());

			for (int i = 0; i <= 4; i++) {
				AudioTrack audioTrack = playlist.getTracks().get(i);
				builder.addChoice(audioTrack.getInfo().title);
			}

			builder.build().display(message);

		}
		else {
			event.replyError("Sorry, we are unable to load a playlist. Please, contact the bot admin.");
		}
	}

	/**
	 * Loads a "playlist" made of the search results when the attempt of loading on
	 * PlayCmd doesn't match. In case of fail it returns a error message.
	 */
	@Override
	public void noMatches() {
		// conditional to avoid loop
		if (!ytsearch)
			audioPlayerManager.loadItem("ytsearch:" + event.getArgs(), new AudioLoadResultHandlerImpl(audioPlayer,
					event, audioPlayerManager, eventWaiter, message, Boolean.TRUE));
		else
			event.replyError("Sorry, I couldn't find your track. Please, rephrase and try again.");
	}

	@Override
	public void loadFailed(final FriendlyException exception) {
	}

}
