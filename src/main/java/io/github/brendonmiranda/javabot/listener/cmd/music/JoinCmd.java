package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinCmd extends MusicCmd{

    private static final Logger logger = LoggerFactory.getLogger(JoinCmd.class);

    public JoinCmd() {
        this.name = "join";
        this.help = "joins you on the channel";
    }

    @Override
    public void command(CommandEvent event) {

    }
}
