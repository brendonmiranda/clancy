package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.brendonmiranda.bot.clancy.listener.AudioEventListener;
import io.github.brendonmiranda.bot.clancy.listener.PlayResultHandler;
import io.github.brendonmiranda.bot.clancy.service.AudioQueueService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
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

			Guild guild = event.getGuild();
			AudioManager audioManager = guild.getAudioManager();
			PlayResultHandler playResultHandler = new PlayResultHandler(audioPlayer, guild, audioManager, event,
					audioPlayerManager, eventWaiter, message, false, audioQueueService);

			audioPlayerManager.loadItemOrdered(event.getGuild(), event.getArgs(), playResultHandler);

		});

	}

}
