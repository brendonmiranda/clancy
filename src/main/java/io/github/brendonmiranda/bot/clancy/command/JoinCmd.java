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
		this.help = "joins you on the channel";
	}

	@Override
	protected void execute(SlashCommandEvent event) {
		VoiceChannel memberVoiceChannel = event.getMember().getVoiceState().getChannel();

		// It validates if the member who trigger the event is present in a voice channel.
		if (memberVoiceChannel == null) {
			event.reply("You must be in a voice channel.").queue();
			return;
		}

		Guild guild = event.getGuild();
		AudioManager audioManager = getAudioManager(guild);

		audioManager.openAudioConnection(memberVoiceChannel);

		event.reply(":thumbsup:").queue();

		// todo: refactor this method and delete the execute method below
	}

	/**
	 * It was overrode in order to avoid validations from MusicCmd which must not be
	 * applied to Join Command.
	 * @param event event
	 */
	@Override
	public void execute(CommandEvent event) {
		VoiceChannel memberVoiceChannel = event.getEvent().getMember().getVoiceState().getChannel();

		// It validates if the member who trigger the event is present in a voice channel.
		if (memberVoiceChannel == null) {
			event.reply(MessageUtil.buildMessage("You must be in a voice channel."));
			return;
		}

		command(event);
	}

	@Override
	public void command(CommandEvent event) {
		VoiceChannel memberVoiceChannel = event.getEvent().getMember().getVoiceState().getChannel();
		Guild guild = getGuild(event);
		AudioManager audioManager = getAudioManager(guild);

		audioManager.openAudioConnection(memberVoiceChannel);
	}

}
