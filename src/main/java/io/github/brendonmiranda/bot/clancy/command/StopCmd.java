package io.github.brendonmiranda.bot.clancy.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class StopCmd extends MusicCmd {

    public StopCmd() {
        this.name = "stop";
        this.help = "stops the current song";
    }

    public void command(SlashCommandInteractionEvent event) {

        stop(event.getGuild());
        event.replyEmbeds(MessageUtil.buildMessage("The player has stopped.")).queue();
    }

    // stop method has been separated to allow reuse of it in the code
    public void stop(Guild guild) {

        AudioManager audioManager = getAudioManager(guild);
        AudioSendHandlerImpl audioSendHandler = getAudioSendHandler(guild);

        if (audioSendHandler != null) {
            AudioPlayer audioPlayer = audioSendHandler.getAudioPlayer();

            audioPlayer.stopTrack();

            // pause music to prevent the next one from starting paused
            if (audioPlayer.isPaused())
                audioPlayer.setPaused(false);
        }

        audioManager.closeAudioConnection();
    }
}
