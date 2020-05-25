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
public class JoinCmd extends Command {

    private static final Logger logger = LoggerFactory.getLogger(JoinCmd.class);

    private final LifeCycleService lifeCycleService;

    public JoinCmd(LifeCycleService lifeCycleService) {
        this.name = "join";
        this.help = "joins you on the channel";
        this.lifeCycleService = lifeCycleService; // todo: inject it
    }

    @Override
    protected void execute(CommandEvent event) {

        VoiceChannel voiceChannel = event.getEvent().getMember().getVoiceState().getChannel();

        if (voiceChannel == null) {
            event.replyError("You must be in a voice channel.");
            return;
        }

        Guild guild = event.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(voiceChannel);

        lifeCycleService.scheduleDisconnectByInactivityTask(guild);
    }
}
