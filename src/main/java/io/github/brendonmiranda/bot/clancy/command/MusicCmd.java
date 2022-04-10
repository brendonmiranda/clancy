package io.github.brendonmiranda.bot.clancy.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * @author brendonmiranda
 */
public abstract class MusicCmd extends SlashCommand {

	public MusicCmd() {
		this.guildOnly = true;
		this.category = new Category("Music");
	}

	@Override
	protected void execute(SlashCommandEvent event) {

		AudioManager audioManager = getAudioManager(event.getGuild());
		VoiceChannel memberVoiceChannel = getChannel(event);

		/*
		 * To execute any music command the bot needs to be in a voice channel. It
		 * validates this. A voice channel is reached by the bot through the Join command.
		 */
		if (audioManager.getConnectedChannel() == null) {
			event.replyEmbeds(MessageUtil.buildMessage("Type `/join`")).queue();
			return;
		}

		/*
		 * It validates if the member who trigger the event is present in a voice channel.
		 */
		if (memberVoiceChannel == null) {
			event.replyEmbeds(MessageUtil.buildMessage("You must be in a voice channel.")).queue();
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

	protected Guild getGuild(SlashCommandEvent event) {
		return event.getGuild();
	}

	protected AudioManager getAudioManager(Guild guild) {
		return guild.getAudioManager();
	}

	protected VoiceChannel getChannel(SlashCommandEvent event) {
		return event.getMember().getVoiceState().getChannel();
	}

	public abstract void command(SlashCommandEvent event);

}
