package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.javabot.service.AudioQueueService;
import io.github.brendonmiranda.javabot.service.LifeCycleService;
import net.dv8tion.jda.api.entities.Guild;
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

	@Autowired
	private LifeCycleService lifeCycleService;

	@Autowired
	private AudioQueueService audioQueueService;

	public StopCmd() {
		this.name = "stop";
		this.help = "stops the current song";
	}

	public void command(CommandEvent event) {

		stop(event.getGuild());

		event.replySuccess("The player has stopped!");
		lifeCycleService.setActivityDefault(event.getJDA());
	}

	public void stop(Guild guild) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) guild.getAudioManager().getSendingHandler();

		if (audioSendHandler != null) {
			audioSendHandler.getAudioPlayer().stopTrack();

			// Pausing a song to not starts the new song paused
			if (audioSendHandler.getAudioPlayer().isPaused())
				audioSendHandler.getAudioPlayer().setPaused(false);
		}

		guild.getAudioManager().closeAudioConnection();
		audioQueueService.destroy(guild.getName());
	}

}
