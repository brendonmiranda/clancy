package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

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

		command(event);
	}

	protected AudioSendHandlerImpl getAudioSendHandler(Guild guild) {
		return (AudioSendHandlerImpl) guild.getAudioManager().getSendingHandler();
	}

	protected AudioPlayer getAudioPlayer(AudioSendHandlerImpl audioSendHandler) {
		return audioSendHandler.getAudioPlayer();
	}

	protected Guild getGuild(CommandEvent event) {
		return event.getGuild();
	}

	protected AudioManager getAudioManager(Guild guild) {
		return guild.getAudioManager();
	}

	public abstract void command(CommandEvent event);

}
