package io.github.brendonmiranda.javabot.listener;

import io.github.brendonmiranda.javabot.config.BotConfiguration;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Listen to the {@link net.dv8tion.jda.api.events.ReadyEvent} event
 *
 * @author brendonmiranda
 */
public class ReadyListener extends ListenerAdapter {

    private static Logger logger = LoggerFactory.getLogger(BotConfiguration.class);

    @Override
    public void onReady(@Nonnull final ReadyEvent event) {
        logger.info("Ready!");
    }

}
