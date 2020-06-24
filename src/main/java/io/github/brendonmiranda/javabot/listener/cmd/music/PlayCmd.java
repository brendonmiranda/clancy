package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.brendonmiranda.javabot.listener.audio.AudioEventListener;
import io.github.brendonmiranda.javabot.listener.audio.PlayResultHandler;
import io.github.brendonmiranda.javabot.service.AudioQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author brendonmiranda
 */
@Component
public class PlayCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(PlayCmd.class);

	@Autowired
	private AudioQueueService audioQueueService;

	@Autowired
	private AudioPlayerManager audioPlayerManager;

	@Autowired
	private EventWaiter eventWaiter;

	@Autowired
	private AudioEventListener audioEventListener;

	public PlayCmd() {
		this.name = "play";
		this.help = "plays or queue a song";
	}

	@Override
	public void command(CommandEvent event) {
		logger.debug("PlayCmd loading track: {}", event.getArgs());

		AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
		audioPlayer.addListener(audioEventListener);

		event.reply("Searching...", (message) -> {
			audioPlayerManager.loadItemOrdered(event.getGuild(), event.getArgs(), new PlayResultHandler(audioPlayer,
					event, audioPlayerManager, eventWaiter, message, false, audioQueueService));
		});

	}

}
