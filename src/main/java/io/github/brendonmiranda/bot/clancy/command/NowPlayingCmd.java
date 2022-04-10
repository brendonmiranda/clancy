package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
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
	public void command(SlashCommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(event.getGuild());

		if (audioSendHandler == null) {
			event.replyEmbeds(MessageUtil.buildMessage("There is no track playing.")).queue();
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.getPlayingTrack() != null) {
			AudioTrack audioTrack = audioPlayer.getPlayingTrack();
			event.replyEmbeds(MessageUtil.buildMessage("Playing", audioTrack.getInfo().title)).queue();
		}
		else {
			event.replyEmbeds(MessageUtil.buildMessage("There is no track playing.")).queue();
		}
	}

}
