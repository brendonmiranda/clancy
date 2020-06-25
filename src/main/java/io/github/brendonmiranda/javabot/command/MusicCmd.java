package io.github.brendonmiranda.javabot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import static io.github.brendonmiranda.javabot.service.InactivityService.*;

/**
 * @author brendonmiranda
 */
public abstract class MusicCmd extends Command {

	public MusicCmd() {
		this.guildOnly = true;
		this.category = new Category("Music");
	}

	@Override
	protected void execute(CommandEvent event) {

		AudioManager audioManager = event.getGuild().getAudioManager();
		VoiceChannel memberVoiceChannel = event.getEvent().getMember().getVoiceState().getChannel();

		/*
		 * To execute any music command the bot needs to be in a voice channel. It
		 * validates this. A voice channel is achieved by the bot through Join command.
		 */
		if (audioManager.getConnectedChannel() == null) {
			event.replyWarning("Type `" + event.getClient().getPrefix() + "join`");
			return;
		}

		/*
		 * It validates if the member who trigger the event is present in a voice channel.
		 */
		if (memberVoiceChannel == null) {
			event.replyError("You must be in a voice channel.");
			return;
		}

		/*
		 * Cancel any disconnectByInactivityTask scheduled previously given that a command
		 * has been triggered
		 */
		timerTasksQueue.forEach(task -> task.cancel());

		command(event);
	}

	public abstract void command(CommandEvent event);

}
