package io.github.brendonmiranda.bot.clancy.command;

import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

/**
 * Minimal replacement for jdautilities' SlashCommand base used by this project.
 */
public abstract class MusicCmd {

    // command metadata (set in constructors of concrete commands)
    public String name;
    public String help;
    public boolean guildOnly = true;
    public List<OptionData> options;

    public MusicCmd() {
        this.guildOnly = true;
    }

    /**
     * Entry point called by CommandRouter when a slash command is triggered.
     */
    public void execute(SlashCommandInteractionEvent event) {

        if (this.guildOnly && event.getGuild() == null) {
            event.replyEmbeds(MessageUtil.buildMessage("This command can only be used inside a server.")).queue();
            return;
        }

        AudioManager audioManager = null;
        if (event.getGuild() != null) {
            audioManager = event.getGuild().getAudioManager();
        }

        // If this is a music command and the bot is not connected, tell user to use /join
        if (audioManager != null && audioManager.getConnectedChannel() == null && !"join".equalsIgnoreCase(name)) {
            event.replyEmbeds(MessageUtil.buildMessage("Type `/join`")).queue();
            return;
        }

        VoiceChannel memberVoiceChannel = getChannel(event);
        if (memberVoiceChannel == null && !"join".equalsIgnoreCase(name)) {
            event.replyEmbeds(MessageUtil.buildMessage("You must be in a voice channel.")).queue();
            return;
        }

        command(event);
    }

    protected AudioSendHandlerImpl getAudioSendHandler(Guild guild) {
        return (AudioSendHandlerImpl) guild.getAudioManager().getSendingHandler();
    }

    protected Guild getGuild(SlashCommandInteractionEvent event) {
        return event.getGuild();
    }

    protected AudioManager getAudioManager(Guild guild) {
        return guild.getAudioManager();
    }

    protected VoiceChannel getChannel(SlashCommandInteractionEvent event) {
        if (event.getMember() == null || event.getMember().getVoiceState() == null) return null;
        return event.getMember().getVoiceState().getChannel();
    }

    public abstract void command(SlashCommandInteractionEvent event);
}
