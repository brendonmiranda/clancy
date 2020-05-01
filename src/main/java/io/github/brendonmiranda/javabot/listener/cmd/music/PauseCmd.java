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
			event.replyWarning("I know this song is sucks but it is already paused! Type `"
					+ event.getClient().getPrefix() + "play` to give it another chance.");
			return;
		}

		audioSendHandler.getAudioPlayer().setPaused(true);
		event.replySuccess("Is it too bad? Ok, paused **"
				+ audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title + "**.");
	}

}
