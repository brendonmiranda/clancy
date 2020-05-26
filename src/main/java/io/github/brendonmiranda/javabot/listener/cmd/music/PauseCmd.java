package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import io.github.brendonmiranda.javabot.service.LifeCycleService;

/**
 * @author evelynvieira
 */
public class PauseCmd extends MusicCmd {

	private final LifeCycleService lifeCycleService;

	public PauseCmd(LifeCycleService lifeCycleService) {
		this.lifeCycleService = lifeCycleService;
		this.name = "pause";
		this.help = "pauses the current song";
	}

	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();

		if (audioSendHandler == null)
			return;

		if (audioSendHandler.getAudioPlayer().isPaused()) {
			audioSendHandler.getAudioPlayer().setPaused(false);
			event.replySuccess(
					"Resumed **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title + "**.");
			return;
		}

		audioSendHandler.getAudioPlayer().setPaused(true);
		lifeCycleService.scheduleDisconnectByInactivityTask(event.getGuild());
		event.replySuccess("Paused **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title
				+ "**. Type `" + event.getClient().getPrefix() + "resume` to unpause!");
	}

}
