package io.github.brendonmiranda.javabot.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(BotConfiguration.class);

    @Value("${token}")
    private String token;

    @Bean
    public JDA load() throws LoginException, InterruptedException {

        return JDABuilder.createDefault(token)
                .build().awaitReady();
    }


}
