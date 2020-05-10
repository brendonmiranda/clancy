package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;

/**
 * @author evelynvieira
 */
public class PauseCmd extends MusicCmd {

	public PauseCmd() {
		this.name = "pause";
		this.help = "pauses the current song";
	}

	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();

		if (audioSendHandler.getAudioPlayer().isPaused()) {
			audioSendHandler.getAudioPlayer().setPaused(false);
			event.replySuccess(
					"Unpaused **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title + "**.");
			return;
		}

		audioSendHandler.getAudioPlayer().setPaused(true);
		event.replySuccess("Paused **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title
				+ "**. Type `" + event.getClient().getPrefix() + "resume` to unpause!");
	}

}
