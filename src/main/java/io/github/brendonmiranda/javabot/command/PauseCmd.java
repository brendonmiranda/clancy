package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
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
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();

		if (audioSendHandler == null)
			return;

		if (audioSendHandler.getAudioPlayer().isPaused()) {
			// todo: instantiate ResumeCmd and use it instead
			audioSendHandler.getAudioPlayer().setPaused(false);
			event.replySuccess(
					"Resumed **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title + "**.");
			return;
		}

		if (audioSendHandler.getAudioPlayer().getPlayingTrack() != null) {
			audioSendHandler.getAudioPlayer().setPaused(true);
			inactivityService.scheduleDisconnectByInactivityTask(event.getGuild());
			event.replySuccess("Paused **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title
					+ "**. Type `" + event.getClient().getPrefix() + "resume` to unpause!");
		}
	}

}
