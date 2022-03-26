package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class StopCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(StopCmd.class);

	public StopCmd() {
		this.name = "stop";
		this.help = "stops the current song";
	}

	@Override
	protected void execute(SlashCommandEvent slashCommandEvent) {

	}

	public void command(CommandEvent event) {

		stop(event.getGuild());
		event.reply(MessageUtil.buildMessage("The player has stopped."));
	}

	public void stop(Guild guild) {

		AudioManager audioManager = getAudioManager(guild);
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(guild);

		if (audioSendHandler != null) {
			AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

			audioPlayer.stopTrack();

			// pause music to prevent the next one from starting paused
			if (audioPlayer.isPaused())
				audioPlayer.setPaused(false);
		}

		audioManager.closeAudioConnection();
	}

}
