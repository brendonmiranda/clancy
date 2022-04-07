package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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
public class ResumeCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(ResumeCmd.class);

	public ResumeCmd() {
		this.name = "resume";
		this.help = "resumes the current song";
	}

	public void command(SlashCommandEvent event) {
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(event.getGuild());

		if (audioSendHandler == null) {
			event.reply("There is no track to resume.").queue();
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.getPlayingTrack() != null && audioPlayer.isPaused()) {
			audioPlayer.setPaused(false);
			event.reply("Resumed: " + audioPlayer.getPlayingTrack().getInfo().title).queue();
		}

	}

}
