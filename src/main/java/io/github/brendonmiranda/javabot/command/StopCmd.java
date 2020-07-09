package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.javabot.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.javabot.service.ActivityService;
import io.github.brendonmiranda.javabot.service.AudioQueueService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	public void command(CommandEvent event) {

		stop(event.getGuild());
		event.replySuccess("The player has stopped!");
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
