package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class ResumeCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(ResumeCmd.class);

	public ResumeCmd() {
		this.name = "resume";
		this.help = "resumes the current song";
	}

	public void command(CommandEvent event) {
		if (event.getArgs().isEmpty() && event.getMessage().getAttachments().isEmpty()) {
			AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
					.getSendingHandler();

			if (audioSendHandler != null && audioSendHandler.getAudioPlayer().getPlayingTrack() != null
					&& audioSendHandler.getAudioPlayer().isPaused()) {
				audioSendHandler.getAudioPlayer().setPaused(false);
				event.replySuccess(
						"Resumed **" + audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title + "**.");
			}
		}
	}

}
