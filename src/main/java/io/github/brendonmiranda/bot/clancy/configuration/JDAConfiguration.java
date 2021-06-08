package io.github.brendonmiranda.bot.clancy.configuration;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import io.github.brendonmiranda.bot.clancy.command.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.entities.Activity.listening;

/**
 * @author brendonmiranda
 */
@Configuration
public class JDAConfiguration {

	@Value("${bot.token}")
	private String token;

	@Value("${bot.prefix}")
	private String prefix;

	@Value("${bot.owner}")
	private Long owner;

	@Bean
	public JDA load(PlayCmd playCmd, StopCmd stopCmd, PauseCmd pauseCmd, ResumeCmd resumeCmd, SkipCmd skipCmd,
			NowPlayingCmd nowPlayingCmd, JoinCmd joinCmd) throws LoginException {

		JDA jda = JDABuilder.createDefault(token).build();

		CommandClient cmdListener = new CommandClientBuilder().setPrefix(prefix).setOwnerId(Long.toString(owner))
				.addCommands(playCmd, stopCmd, pauseCmd, resumeCmd, skipCmd, nowPlayingCmd, joinCmd)
				.setActivity(listening("type " + prefix + "help")).build();

		jda.addEventListener(cmdListener, eventWaiter());

		return jda;
	}

	@Bean
	public EventWaiter eventWaiter() {
		return new EventWaiter();
	}

	/**
	 * If possible, you should use a single instance of a player manager for your whole
	 * application. A player manager manages several thread pools which make no sense to
	 * duplicate.
	 * @return AudioPlayerManager
	 */
	@Bean
	public AudioPlayerManager audioPlayerManager() {

		AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		return audioPlayerManager;
	}

}
