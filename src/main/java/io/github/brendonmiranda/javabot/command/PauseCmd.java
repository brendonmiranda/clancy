package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.javabot.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.javabot.service.InactivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class PauseCmd extends MusicCmd {

	@Autowired
	private InactivityService inactivityService;

	public PauseCmd() {
		this.name = "pause";
		this.help = "pauses the current song";
	}

	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(event.getGuild());

		if (audioSendHandler == null) {
			event.replyError("There is no track playing to pause.");
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.isPaused()) {
			// todo: instantiate ResumeCmd and use it instead
			audioPlayer.setPaused(false);
			event.replySuccess("Resumed **" + audioPlayer.getPlayingTrack().getInfo().title + "**.");
			return;
		}

		if (audioPlayer.getPlayingTrack() != null) {
			audioPlayer.setPaused(true);
			inactivityService.scheduleDisconnectByInactivityTask(event.getGuild());
			event.replySuccess("Paused **" + audioPlayer.getPlayingTrack().getInfo().title + "**. Type `"
					+ event.getClient().getPrefix() + " resume` to unpause!");
		}
	}

}
