package io.github.brendonmiranda.javabot.listener.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author evelynvieira
 */
public class PauseCmd extends Command {

    private static Logger logger = LoggerFactory.getLogger(PauseCmd.class);

    public PauseCmd(){
        this.name = "pause";
        this.help = "pauses the current song";
        this.guildOnly = true;
    }

    protected void execute(final CommandEvent event){
        AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) event.getGuild().getAudioManager().getSendingHandler();

        if(audioSendHandler.getAudioPlayer().isPaused()){
            event.replyWarning("I know this song is sucks but it is already paused! Type `"+event.getClient().getPrefix()+"play` to give it another chance.");
            return;
        }

        audioSendHandler.getAudioPlayer().setPaused(true);
        event.replySuccess("Is it too bad? Ok, paused **"+audioSendHandler.getAudioPlayer().getPlayingTrack().getInfo().title+"**.");
        }
}
