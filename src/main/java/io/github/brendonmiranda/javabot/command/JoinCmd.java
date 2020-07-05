package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
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

	/**
	 * It was overrode in order to avoid validations from MusicCmd which must not be
	 * applied to Join Command.
	 * @param event
	 */
	@Override
	public void execute(CommandEvent event) {
		VoiceChannel memberVoiceChannel = event.getEvent().getMember().getVoiceState().getChannel();

		// It validates if the member who trigger the event is present in a voice channel.
		if (memberVoiceChannel == null) {
			event.replyError("You must be in a voice channel.");
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
