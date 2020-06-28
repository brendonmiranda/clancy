package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.javabot.listener.AudioSendHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class NowPlayingCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(NowPlayingCmd.class);

	public NowPlayingCmd() {
		this.name = "now";
		this.help = "shows the song which is playing in the moment";
	}

	@Override
	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(event.getGuild());

		if (audioSendHandler == null) {
			event.replyError("There is no track playing");
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.getPlayingTrack() != null) {
			AudioTrack audioTrack = audioPlayer.getPlayingTrack();
			event.replySuccess("Now playing the track **" + audioTrack.getInfo().title + "**.");
		}
		else {
			event.replyWarning("There is no track playing");
		}
	}

}
