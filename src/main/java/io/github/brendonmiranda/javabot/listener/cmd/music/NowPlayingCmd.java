package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.brendonmiranda.javabot.listener.audio.AudioEventListener.queue;

/**
 * @author evelynvieira
 */
public class NowPlayingCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(NowPlayingCmd.class);

	public NowPlayingCmd() {
		this.name = "now";
		this.help = "shows the song that is playing in the moment";
	}

	@Override
	public void command(CommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager()
				.getSendingHandler();

		if (audioSendHandler.getAudioPlayer().getPlayingTrack() != null) {
			AudioTrack audioTrack = audioSendHandler.getAudioPlayer().getPlayingTrack();
			event.replySuccess("Now playing the track **" + audioTrack.getInfo().title + "**.");
		}
		else {
			event.replyWarning("Any track playing");
		}
	}

}
