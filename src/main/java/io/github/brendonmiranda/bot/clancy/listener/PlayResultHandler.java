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
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author brendonmiranda
 */
public class PlayResultHandler implements AudioLoadResultHandler {

	private static final Logger logger = LoggerFactory.getLogger(PlayResultHandler.class);

	private final AudioPlayer audioPlayer;

	private final Guild guild;

	private final AudioManager audioManager;

	private final CommandEvent event;

	private final AudioPlayerManager audioPlayerManager;

	private final EventWaiter eventWaiter;

	private final Message message;

	private final OrderedMenu.Builder builder;

	private final boolean ytSearch;

	private final AudioQueueService audioQueueService;

	public PlayResultHandler(AudioPlayer audioPlayer, Guild guild, AudioManager audioManager, CommandEvent event,
			AudioPlayerManager audioPlayerManager, EventWaiter eventWaiter, Message message, boolean ytSearch,
			AudioQueueService audioQueueService) {
		this.audioPlayer = audioPlayer;
		this.guild = guild;
		this.audioManager = audioManager;
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

		manageTrack(track);
	}

	/**
	 * Plays the track if none is being played otherwise it is enqueued.
	 * @param track audio track
	 */
	public void manageTrack(AudioTrack track) {

		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) audioManager.getSendingHandler();

		// Store guild to be recovered when needed as we do on AudioEventListener
		track.setUserData(guild);

		if (audioSendHandler == null) {
			audioManager.setSendingHandler(new AudioSendHandlerImpl(audioPlayer));
			playTrack(this.audioPlayer, track);
		}
		else {
			AudioPlayer audioPlayer = audioSendHandler.getAudioPlayer();

			if (audioPlayer.getPlayingTrack() != null)
				queueTrack(audioPlayer, track);
			else
				playTrack(audioPlayer, track);
		}
	}

	private void queueTrack(AudioPlayer audioPlayer, AudioTrack track) {

		if (audioPlayer.isPaused()) {
			event.reply(MessageUtil.buildMessage("Alert", "The track **" + audioPlayer.getPlayingTrack().getInfo().title
					+ "** is paused. Type `" + event.getClient().getPrefix() + "resume` to unpause!"));
		}

		audioQueueService.enqueue(guild.getName(), track);

		MessageEmbed messageEmbed = MessageUtil.buildMessage("Enqueued", track.getInfo().title);

		event.reply(messageEmbed);

	}

	private void playTrack(AudioPlayer audioPlayer, AudioTrack track) {

		audioPlayer.playTrack(track);

		MessageEmbed messageEmbed = MessageUtil.buildMessage("Playing", audioPlayer.getPlayingTrack().getInfo().title);

		event.reply(messageEmbed);

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
				manageTrack(audioTrack);
			}).setCancel((msg) -> {
			}).setUsers(event.getAuthor());

			for (int i = 0; i <= 4; i++) {
				AudioTrack audioTrack = playlist.getTracks().get(i);
				builder.addChoice(audioTrack.getInfo().title);
			}

			builder.build().display(message);

		}
		else {
			event.reply(MessageUtil.buildMessage("Sorry, I'm unable to load a playlist."));
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
			audioPlayerManager.loadItem("ytsearch:" + event.getArgs(), new PlayResultHandler(audioPlayer, guild,
					audioManager, event, audioPlayerManager, eventWaiter, message, true, audioQueueService));
		else
			event.reply(MessageUtil.buildMessage("Sorry, I couldn't find your track. Please, rephrase and try again."));
	}

	@Override
	public void loadFailed(final FriendlyException exception) {
	}

}