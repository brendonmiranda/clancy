package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class PauseCmd extends MusicCmd {

	public PauseCmd() {
		this.name = "pause";
		this.help = "pauses the current song";
	}

	public void command(SlashCommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(event.getGuild());

		if (audioSendHandler == null) {
			event.replyEmbeds(MessageUtil.buildMessage("There is no track playing to pause.")).queue();
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.isPaused()) {
			audioPlayer.setPaused(false);
			event.replyEmbeds(MessageUtil.buildMessage("Resumed", audioPlayer.getPlayingTrack().getInfo().title))
					.queue();
			return;
		}

		if (audioPlayer.getPlayingTrack() != null) {
			audioPlayer.setPaused(true);

			event.replyEmbeds(MessageUtil.buildMessage("Paused", audioPlayer.getPlayingTrack().getInfo().title
					+ ". \n\nType `" + event.getCommandString() + "resume` to unpause.")).queue();
		}
	}

}
