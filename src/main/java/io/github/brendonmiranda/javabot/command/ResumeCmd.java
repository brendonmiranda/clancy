package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.javabot.listener.AudioSendHandlerImpl;
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
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(event.getGuild());

		if (audioSendHandler == null) {
			event.replyError("There is no track to resume.");
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.getPlayingTrack() != null && audioPlayer.isPaused()) {
			audioPlayer.setPaused(false);
			event.replySuccess("Resumed **" + audioPlayer.getPlayingTrack().getInfo().title + "**.");
		}

	}

}
