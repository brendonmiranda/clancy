package io.github.brendonmiranda.bot.clancy.listener;

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
import io.github.brendonmiranda.bot.clancy.service.AudioQueueService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author brendonmiranda
 */
public class PlayResultHandler implements AudioLoadResultHandler {

	private static final Logger logger = LoggerFactory.getLogger(PlayResultHandler.class);

	private final AudioPlayer audioPlayer;

	private final CommandEvent event;

	private final AudioPlayerManager audioPlayerManager;

	private final EventWaiter eventWaiter;

	private final Message message;

	private final OrderedMenu.Builder builder;

	private final boolean ytSearch;

	private final AudioQueueService audioQueueService;

	public PlayResultHandler(AudioPlayer audioPlayer, CommandEvent event, AudioPlayerManager audioPlayerManager,
			EventWaiter eventWaiter, Message message, boolean ytSearch, AudioQueueService audioQueueService) {
		this.audioPlayer = audioPlayer;
		this.event = event;
		this.audioPlayerManager = audioPlayerManager;
		this.eventWaiter = eventWaiter;
		this.message = message;
		this.ytSearch = ytSearch;
		this.builder = new OrderedMenu.Builder().allowTextInput(true).useNumbers().useCancelButton(true)
				.setEventWaiter(eventWaiter).setTimeout(1, TimeUnit.MINUTES);
		this.audioQueueService = audioQueueService;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has loaded. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		queueTracks(track);
	}

	/**
	 * Plays the track if none is being played otherwise it is enqueued.
	 * @param track audio track
	 */
	public void queueTracks(AudioTrack track) {

		/*
		 * Object (Guild) to be recovered when necessary. Currently it is being used on
		 * AudioEventListener
		 */
		Guild guild = event.getGuild();
		track.setUserData(guild);

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

				audioQueueService.enqueue(guild.getName(), track);

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

		// todo: log playlist loaded
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
			event.replyError("Sorry, I'm unable to load a playlist.");
		}
	}

	/**
	 * Loads a "playlist" made of the search results when the attempt of loading on
	 * PlayCmd doesn't match. In case of fail it returns a error message.
	 */
	@Override
	public void noMatches() {
		// conditional to avoid loop
		if (!ytSearch)
			audioPlayerManager.loadItem("ytsearch:" + event.getArgs(), new PlayResultHandler(audioPlayer, event,
					audioPlayerManager, eventWaiter, message, true, audioQueueService));
		else
			event.replyError("Sorry, I couldn't find your track. Please, rephrase and try again.");
	}

	@Override
	public void loadFailed(final FriendlyException exception) {
	}

}