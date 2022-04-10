package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author brendonmiranda
 */
@Component
public class JoinCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(JoinCmd.class);

	public JoinCmd() {
		this.name = "join";
		this.help = "it calls the bot to join the channel";
	}

	/**
	 * It has been overridden to avoid validations from MusicCmd execute method which must
	 * not be applied to Join Command.
	 * @param event event
	 */
	@Override
	protected void execute(SlashCommandEvent event) {
		logger.debug("Performing validations on join command.");

		VoiceChannel memberVoiceChannel = getChannel(event);

		// it validates if the member who triggers the event is present in a voice
		// channel.
		if (memberVoiceChannel == null) {
			event.replyEmbeds(MessageUtil.buildMessage("You must be in a voice channel.")).queue();
			return;
		}

		command(event);

	}

	@Override
	public void command(SlashCommandEvent event) {
		VoiceChannel memberVoiceChannel = getChannel(event);
		Guild guild = getGuild(event);
		AudioManager audioManager = getAudioManager(guild);

		audioManager.openAudioConnection(memberVoiceChannel);

		event.reply("What's up!").queue();
	}

}
