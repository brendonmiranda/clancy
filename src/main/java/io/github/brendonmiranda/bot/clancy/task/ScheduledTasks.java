package io.github.brendonmiranda.bot.clancy.task;

import io.github.brendonmiranda.bot.clancy.command.StopCmd;
import io.github.brendonmiranda.bot.clancy.listener.AudioSendHandlerImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author brendonmiranda
 */
@Component
public class ScheduledTasks {

	public static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	private JDA jda;

	@Autowired
	private StopCmd stopCmd;

	@Value("${inactive.threshold:3600000}")
	private long inactiveThreshold;

	/**
	 * Check guilds and remove inactive bots which is the ones that haven't played
	 * anything in a period defined by {@link this#inactiveThreshold}.
	 */
	@Scheduled(fixedDelayString = "${inactive.task.fixed.delay:3600000}")
	public void inactiveTask() {
		List<Guild> guilds = jda.getGuilds();

		for (Guild guild : guilds) {
			AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) guild.getAudioManager().getSendingHandler();

			if (audioSendHandler != null
					&& System.currentTimeMillis() - audioSendHandler.getLastRequestTime() >= inactiveThreshold) {
				logger.info("Bot has disconnected by inactivity. Guild: {}", guild.getName());
				stopCmd.stop(guild);
			}

		}
	}

}
