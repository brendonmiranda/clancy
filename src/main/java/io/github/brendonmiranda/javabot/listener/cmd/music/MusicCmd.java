package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public abstract class MusicCmd extends Command {

	public MusicCmd() {
		this.guildOnly = true;
		this.category = new Category("Music");
	}

	@Override
	protected void execute(CommandEvent event) {
		VoiceChannel voiceChannel = event.getEvent().getMember().getVoiceState().getChannel();

		if (voiceChannel == null) {
			event.replyError("You must be in a voice channel.");
			return;
		}

		AudioManager audioManager = event.getGuild().getAudioManager();
		audioManager.openAudioConnection(voiceChannel);
		command(event);
	}

	public abstract void command(CommandEvent event);

}
