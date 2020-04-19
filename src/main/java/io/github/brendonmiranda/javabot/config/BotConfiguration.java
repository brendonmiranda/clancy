package io.github.brendonmiranda.javabot.config;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import io.github.brendonmiranda.javabot.listener.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

/**
 * Loads the initial bot configuration
 *
 * @author brendonmiranda
 */
@Configuration
public class BotConfiguration {

    @Value("${token}")
    private String token;

    @Value("${prefix}")
    private String prefix;

    @Value("${owner}")
    private Long owner;

    @Bean
    public JDA load() throws LoginException {

        CommandClient cmdListener = new CommandClientBuilder()
                .setPrefix(prefix)
                .setOwnerId(Long.toString(owner))
                .addCommands()
                .build();

        JDA jda = JDABuilder.createDefault(token)
                .build();

        jda.addEventListener(cmdListener, new ReadyListener());

        return jda;
    }

}
