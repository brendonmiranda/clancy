package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.brendonmiranda.javabot.listener.audio.AudioEventListener.queue;

/**
 * @author evelynvieira
 */
public class StopCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(StopCmd.class);

	public StopCmd() {
		this.name = "stop";
		this.help = "stops the current song";
	}

	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();
		AudioPlayer audioPlayer = audioSendHandler.getAudioPlayer();

		if (audioPlayer.isPaused())
			audioPlayer.setPaused(false);

		audioPlayer.stopTrack();
		event.getGuild().getAudioManager().closeAudioConnection();
		queue.clear();

		event.replySuccess("The player has stopped!");
	}

}
