package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.service.LifeCycleService;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brendonmiranda
 */
public class JoinCmd extends MusicCmd{

    private static final Logger logger = LoggerFactory.getLogger(JoinCmd.class);

    private final LifeCycleService lifeCycleService;

    public JoinCmd(LifeCycleService lifeCycleService) {
        this.name = "join";
        this.help = "joins you on the channel";
        this.lifeCycleService = lifeCycleService; // todo: inject it
    }

    @Override
    public void command(CommandEvent event) {
		AudioManager audioManager = event.getGuild().getAudioManager();
		audioManager.openAudioConnection(voiceChannel);
        lifeCycleService.scheduleDisconnectByInactivityTask(event);
    }
}
