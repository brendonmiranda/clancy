package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.service.LifeCycleService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brendonmiranda
 */
public class JoinCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(JoinCmd.class);

	private final LifeCycleService lifeCycleService;

	public JoinCmd(LifeCycleService lifeCycleService) {
		this.name = "join";
		this.help = "joins you on the channel";
		this.lifeCycleService = lifeCycleService; // todo: inject it
	}

	/**
	 * It was overrode in order to avoid validations from MusicCmd which must not be
	 * applied to JoinCmd.
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
		Guild guild = event.getGuild();
		AudioManager audioManager = guild.getAudioManager();

		audioManager.openAudioConnection(memberVoiceChannel);
		lifeCycleService.scheduleDisconnectByInactivityTask(guild);
	}

}
