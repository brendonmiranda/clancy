package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.brendonmiranda.javabot.listener.audio.AudioEventListener;
import io.github.brendonmiranda.javabot.listener.audio.AudioLoadResultHandlerImpl;
import io.github.brendonmiranda.javabot.service.AudioQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brendonmiranda
 */
public class PlayCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(PlayCmd.class);

	private final AudioPlayerManager audioPlayerManager;

	private final AudioEventListener audioListener; // todo remove it. Instantiate in
													// command method

	private final AudioQueueService audioQueueService;

	private final EventWaiter eventWaiter;

	public PlayCmd(AudioPlayerManager audioPlayerManager, AudioEventListener audioListener,
			AudioQueueService audioQueueService, EventWaiter eventWaiter) {
		this.audioPlayerManager = audioPlayerManager;
		this.audioListener = audioListener;
		this.audioQueueService = audioQueueService;
		this.eventWaiter = eventWaiter;
		this.name = "play";
		this.help = "plays or queue a song";
	}

	@Override
	public void command(CommandEvent event) {
		logger.debug("PlayCmd loading track: {}", event.getArgs());

		AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
		audioPlayer.addListener(audioListener);

		event.reply("Searching...", (message) -> {
			audioPlayerManager.loadItemOrdered(event.getGuild(), event.getArgs(), new AudioLoadResultHandlerImpl(
					audioPlayer, event, audioPlayerManager, eventWaiter, message, false));
		});

	}

}
