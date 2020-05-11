package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import static io.github.brendonmiranda.javabot.listener.audio.AudioEventListener.queue;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author evelynvieira
 */
public class SkipCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(SkipCmd.class);

	public SkipCmd() {
		this.name = "skip";
		this.help = "skips the current song";
	}

	@Override
	public void command(CommandEvent event) {

		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();
		AudioPlayer audioPlayer = audioSendHandler.getAudioPlayer();
		if (audioSendHandler != null && audioPlayer.getPlayingTrack() != null) {
			if (!queue.isEmpty()) {

				if (audioPlayer.isPaused())
					audioPlayer.setPaused(false);

				audioPlayer.stopTrack();
				audioPlayer.playTrack(queue.get(0));
				queue.remove(0);
				event.reply("Playing **" + audioPlayer.getPlayingTrack().getInfo().title + "**.");
			}
			else {
				event.replyWarning("Your queue is empty.");
			}
		}
		else {
			event.replyWarning("There is no track playing.");
		}
	}

}
