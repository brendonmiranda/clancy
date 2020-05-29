package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import io.github.brendonmiranda.javabot.service.LifeCycleService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.brendonmiranda.javabot.listener.audio.AudioEventListener.queue;

/**
 * @author evelynvieira
 */
public class StopCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(StopCmd.class);

	private final LifeCycleService lifeCycleService;

	public StopCmd(LifeCycleService lifeCycleService) {
		this.lifeCycleService = lifeCycleService;
		this.name = "stop";
		this.help = "stops the current song";
	}

	public void command(CommandEvent event) {

		stop(event.getGuild());

		event.replySuccess("The player has stopped!");
		lifeCycleService.setActivityDefault(event.getJDA());
	}

	public static void stop(Guild guild) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) guild.getAudioManager().getSendingHandler();

		if (audioSendHandler != null) {
			audioSendHandler.getAudioPlayer().stopTrack();

			// Pausing a song to not starts the new song paused
			if (audioSendHandler.getAudioPlayer().isPaused())
				audioSendHandler.getAudioPlayer().setPaused(false);
		}

		guild.getAudioManager().closeAudioConnection();
		queue.clear();
	}

}
