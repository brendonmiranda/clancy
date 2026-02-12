package io.github.brendonmiranda.bot.clancy.configuration;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import io.github.brendonmiranda.bot.clancy.command.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static net.dv8tion.jda.api.entities.Activity.listening;

/**
 * @author brendonmiranda
 */
@Configuration
public class JDAConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JDAConfiguration.class);

	@Value("${bot.token}")
	private String token;

	@Value("${bot.prefix}")
	private String prefix;

	@Value("${bot.owner}")
	private Long owner;

	@Value("${youtube.oauth.refresh-token:}")
	private String youtubeOauthRefreshToken;

	@Bean
	public JDA load(PlayCmd playCmd, StopCmd stopCmd, PauseCmd pauseCmd, ResumeCmd resumeCmd, SkipCmd skipCmd,
			NowPlayingCmd nowPlayingCmd, JoinCmd joinCmd) {

		JDA jda = JDABuilder
			.createDefault(token, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGES,
					GatewayIntent.GUILD_MESSAGE_REACTIONS)
			.build();

		CommandClient cmdListener = new CommandClientBuilder().setPrefix(prefix)
			.setOwnerId(Long.toString(owner))
			.addSlashCommands(playCmd, stopCmd, pauseCmd, resumeCmd, skipCmd, nowPlayingCmd, joinCmd)
			.setActivity(listening("type /join"))
			.build();
		// todo: set watching activity

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

		YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager();
		if (youtubeOauthRefreshToken != null && !youtubeOauthRefreshToken.isEmpty()) {
			ytSourceManager.useOauth2(youtubeOauthRefreshToken, true);
			logger.info("YouTube OAuth2 enabled with provided refresh token.");
		}
		else {
			ytSourceManager.useOauth2(null, false);
			logger.info("YouTube OAuth2 flow initiated. Check logs for device code instructions.");
		}
		audioPlayerManager.registerSourceManager(ytSourceManager);

		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		return audioPlayerManager;
	}

}
