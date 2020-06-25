package io.github.brendonmiranda.javabot.service;

import io.github.brendonmiranda.javabot.command.StopCmd;
import io.github.brendonmiranda.javabot.listener.AudioSendHandlerImpl;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author brendonmiranda
 */
@Service
public class InactivityService {

	public static final Logger logger = LoggerFactory.getLogger(InactivityService.class);

	@Autowired
	private ActivityService activityService;

	@Autowired
	private StopCmd stopCmd;

	@Value("${bot.inactivity.time}")
	private long botInactivityTime;

	// todo: a queue of tasks must be implemented properly by guild in order to scale it
	public static final List<TimerTask> timerTasksQueue = new ArrayList<>();

	/**
	 * Schedules task to disconnect inactive bot
	 * @param guild guild where trigger the task
	 */
	public void scheduleDisconnectByInactivityTask(Guild guild) {
		logger.debug("DisconnectByInactivity task scheduled. Guild: {}", guild.getName());

		/*
		 * TODO: in the future the Timer must be replaced for
		 * java.util.concurrent.ScheduledThreadPoolExecutor given that Timer has only one
		 * execution thread and ScheduledThreadPoolExecutor can be configured with any
		 * number of threads. There is a good explanation about it in the description of
		 * the Timer class, you can open the class to read. Also there are two articles
		 * attached on the DJB-7 story.
		 */
		Timer timer = new Timer(guild.getName());
		TimerTask disconnectByInactivityTask = new TimerTask() {

			public void run() {
				AudioManager audioManager = guild.getAudioManager();
				AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) audioManager.getSendingHandler();

				if (audioSendHandler == null || audioSendHandler.getAudioPlayer().getPlayingTrack() == null
						|| audioSendHandler.getAudioPlayer().isPaused() || (audioManager.getConnectedChannel() != null
								&& audioManager.getConnectedChannel().getMembers().size() == 1)) {
					logger.info("Disconnected by inactivity. Guild: {}", guild.getName());

					// same behavior from stop cmd
					stopCmd.stop(guild);
					activityService.setActivityDefault(guild.getJDA());
				}
			}
		};

		timer.schedule(disconnectByInactivityTask, botInactivityTime);
		timerTasksQueue.add(disconnectByInactivityTask);
	}

}
