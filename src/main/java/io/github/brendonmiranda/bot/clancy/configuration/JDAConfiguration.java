package io.github.brendonmiranda.bot.clancy.configuration;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.entities.Activity.listening;

/**
 * Reworked to use JDA 5 and CommandRouter
 */
@Configuration
public class JDAConfiguration {

    @Value("${bot.token}")
    private String token;

    @Bean
    public JDA load() throws LoginException {
        // Create JDA 5 instance
        JDA jda = JDABuilder.createDefault(token)
                .setActivity(listening("type /join"))
                .build();

        // CommandRouter is a component that receives JDA via constructor injection and will
        // register commands after startup.
        return jda;
    }

    /**
     * Use a single instance of AudioPlayerManager for the app.
     * @return AudioPlayerManager
     */
    @Bean
    public AudioPlayerManager audioPlayerManager() {
        AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        return audioPlayerManager;
    }
}
