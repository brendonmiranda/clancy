package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.brendonmiranda.bot.clancy.listener.AudioEventListener;
import io.github.brendonmiranda.bot.clancy.listener.PlayResultHandler;
import io.github.brendonmiranda.bot.clancy.service.AudioQueueService;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.function.Consumer;

/**
 * @author brendonmiranda
 */
@Component
public class PlayCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(PlayCmd.class);

	public static final String MUSIC_ARG = "search";

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
		this.help = "it plays or queues a song";

		this.options = Collections
				.singletonList(new OptionData(OptionType.STRING, MUSIC_ARG, "What you wanna play ?").setRequired(true));
	}

	@Override
	public void command(SlashCommandEvent event) {
		OptionMapping option = event.getOption(MUSIC_ARG);
		String args = option.getAsString();
		logger.debug("PlayCmd loading track: {}", args);

		AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
		audioPlayer.addListener(audioEventListener);

		Consumer<Message> success = (message) -> {

			Guild guild = event.getGuild();
			AudioManager audioManager = guild.getAudioManager();
			PlayResultHandler playResultHandler = new PlayResultHandler(audioPlayer, guild, audioManager, event,
					audioPlayerManager, eventWaiter, message, false, audioQueueService);

			audioPlayerManager.loadItemOrdered(event.getGuild(), args, playResultHandler);

		};

		event.replyEmbeds(MessageUtil.buildMessage("Searching...")).queue(interactionHook -> {
			interactionHook.retrieveOriginal().queue(success);
		});

	}

}
