package io.github.brendonmiranda.javabot.service;

import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author brendonmiranda
 */
@Service
public class LifeCycleService {

	private static final Logger logger = LoggerFactory.getLogger(LifeCycleService.class);

	@Value("${bot.inactivity.time}")
	private long botInactivityTime;

	/**
	 * Schedules task to disconnect inactive bot
	 * @param guild
	 */
	public void scheduleDisconnectByInactivityTask(Guild guild) {
		logger.debug("DisconnectByInactivity task scheduled. Guild: {}", guild.getName());

		Timer timer = new Timer(guild.getName());
		TimerTask disconnectByInactivityTask = new TimerTask() {
			public void run() {
				AudioManager audioManager = guild.getAudioManager();
				AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) audioManager.getSendingHandler();

				if (audioSendHandler == null || audioSendHandler.getAudioPlayer().getPlayingTrack() == null
						|| audioSendHandler.getAudioPlayer().isPaused() || (audioManager.getConnectedChannel() != null
								&& audioManager.getConnectedChannel().getMembers().size() == 1)) {

					logger.debug("Disconnected by inactivity. Guild: {}", guild.getName());
					audioManager.closeAudioConnection();
				}
			}
		};

		timer.schedule(disconnectByInactivityTask, botInactivityTime);
	}

}
