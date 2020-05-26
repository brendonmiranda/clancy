package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.brendonmiranda.javabot.config.BotConfiguration.DEFAULT_ACTIVITY_TYPE;
import static io.github.brendonmiranda.javabot.config.BotConfiguration.DEFAULT_ACTIVITY_VALUE;
import static io.github.brendonmiranda.javabot.listener.audio.AudioEventListener.queue;

/**
 * @author evelynvieira
 */
public class StopCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(StopCmd.class);

	private final JDA jda;

	public StopCmd(JDA jda) {
		this.jda = jda;
		this.name = "stop";
		this.help = "stops the current song";
	}

	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();

		if (audioSendHandler != null) {
			audioSendHandler.getAudioPlayer().stopTrack();

			// Pausing a song to not starts the new song paused
			if (audioSendHandler.getAudioPlayer().isPaused())
				audioSendHandler.getAudioPlayer().setPaused(false);
		}

		event.getGuild().getAudioManager().closeAudioConnection();

		queue.clear();

		event.replySuccess("The player has stopped!");

		jda.getPresence().setActivity(Activity.of(DEFAULT_ACTIVITY_TYPE, DEFAULT_ACTIVITY_VALUE));
	}

}
