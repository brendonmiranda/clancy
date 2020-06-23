package io.github.brendonmiranda.javabot.config;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import io.github.brendonmiranda.javabot.listener.audio.AudioEventListener;
import io.github.brendonmiranda.javabot.listener.cmd.music.*;
import io.github.brendonmiranda.javabot.service.AudioQueueService;
import io.github.brendonmiranda.javabot.service.LifeCycleService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

import static io.github.brendonmiranda.javabot.service.LifeCycleService.DEFAULT_ACTIVITY_TYPE;
import static io.github.brendonmiranda.javabot.service.LifeCycleService.DEFAULT_ACTIVITY_VALUE;
import static net.dv8tion.jda.api.entities.Activity.of;

/**
 * @author brendonmiranda
 */
@Configuration
public class JDAConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JDAConfiguration.class);

	@Value("${token}")
	private String token;

	@Value("${prefix}")
	private String prefix;

	@Value("${owner}")
	private Long owner;

	@Bean
	public JDA load(AudioEventListener audioEventListener, LifeCycleService lifeCycleService,
			AudioQueueService audioQueueService) throws LoginException {

		// todo : implement settings discord
		JDA jda = JDABuilder.createDefault(token).build();
		EventWaiter eventWaiter = eventWaiter();

		CommandClient cmdListener = new CommandClientBuilder().setPrefix(prefix).setOwnerId(Long.toString(owner))
				.addCommands(new PlayCmd(audioPlayerManager(), audioEventListener, audioQueueService, eventWaiter),
						new StopCmd(lifeCycleService), new PauseCmd(lifeCycleService), new ResumeCmd(), new SkipCmd(),
						new QueueCmd(), new NowPlayingCmd(), new JoinCmd(lifeCycleService))
				.setActivity(of(DEFAULT_ACTIVITY_TYPE, DEFAULT_ACTIVITY_VALUE)).build();

		jda.addEventListener(cmdListener, eventWaiter);

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
		logger.debug("Registering audio player manager");

		AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		return audioPlayerManager;
	}

}
