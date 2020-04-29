package io.github.brendonmiranda.javabot.listener.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author evelynvieira
 */
public class StopCmd extends Command {

    private static Logger logger = LoggerFactory.getLogger(StopCmd.class);

    public StopCmd(){
        this.name = "stop";
        this.help = "stops the current song";
        this.guildOnly = true;
    }

    protected void execute(final CommandEvent event){
        AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl)event.getGuild().getAudioManager().getSendingHandler();
        audioSendHandler.getAudioPlayer().stopTrack();
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reply(event.getClient().getSuccess()+" The player has stopped");
    }
}
