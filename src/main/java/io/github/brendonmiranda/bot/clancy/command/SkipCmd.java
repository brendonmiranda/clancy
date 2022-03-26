package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.brendonmiranda.bot.clancy.dto.AudioTrackMessageDTO;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.listener.GeneralResultHandler;
import io.github.brendonmiranda.bot.clancy.service.AudioQueueService;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class SkipCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(SkipCmd.class);

	@Autowired
	private AudioQueueService audioQueueService;

	@Autowired
	private AudioPlayerManager audioPlayerManager;

	public SkipCmd() {
		this.name = "skip";
		this.help = "skips the current song";
	}

	@Override
	protected void execute(SlashCommandEvent slashCommandEvent) {

	}

	@Override
	public void command(CommandEvent event) {

		Guild guild = getGuild(event);
		AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(guild);

		if (audioSendHandler == null) {
			event.reply(MessageUtil.buildMessage("There is no track playing to skip."));
			return;
		}

		AudioPlayer audioPlayer = getAudioPlayer(audioSendHandler);

		if (audioPlayer.getPlayingTrack() != null) {

			AudioTrackMessageDTO audioTrackMessageDTO = audioQueueService.receive(guild.getName());

			if (audioTrackMessageDTO != null) {

				if (audioPlayer.isPaused())
					audioPlayer.setPaused(false);

				audioPlayer.stopTrack();
				audioPlayerManager.loadItem(audioTrackMessageDTO.getAudioTrackInfoDTO().getIdentifier(),
						new GeneralResultHandler(audioPlayer, guild));
			}
			else {
				event.reply(MessageUtil.buildMessage("The queue is empty."));
			}

		}
	}

}