package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
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

	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(event.getGuild());

		if (audioSendHandler == null) {
			event.reply(MessageUtil.buildMessage("There is no track playing to pause."));
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.isPaused()) {
			// todo: instantiate ResumeCmd and use it instead
			audioPlayer.setPaused(false);
			event.reply(MessageUtil.buildMessage("Resumed", audioPlayer.getPlayingTrack().getInfo().title));
			return;
		}

		if (audioPlayer.getPlayingTrack() != null) {
			audioPlayer.setPaused(true);

			event.reply(MessageUtil.buildMessage("Paused", audioPlayer.getPlayingTrack().getInfo().title + ". Type `"
					+ event.getClient().getPrefix() + "resume` to unpause!"));
		}
	}

}
