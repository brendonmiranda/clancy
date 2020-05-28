package io.github.brendonmiranda.javabot.listener.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.brendonmiranda.javabot.service.LifeCycleService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.dv8tion.jda.api.entities.Activity.ActivityType.LISTENING;

/**
 * Listener for events of audio reproducing
 *
 * @author brendonmiranda
 */
@Component
public class AudioEventListener extends AudioEventAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AudioEventListener.class);

	@Autowired
	private LifeCycleService lifeCycleService;

	public static List<AudioTrack> queue = new ArrayList<>();

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has started. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		lifeCycleService.setActivity(((Guild) track.getUserData()).getJDA(), LISTENING, audioTrackInfo.title);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has ended. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		// Plays the next track from queue
		if (!queue.isEmpty() && !endReason.equals(AudioTrackEndReason.STOPPED)) {
			player.playTrack(queue.get(0));
			queue.remove(0);
			lifeCycleService.setActivity(((Guild) track.getUserData()).getJDA(), LISTENING, audioTrackInfo.title);
			return;
		}

		lifeCycleService.scheduleDisconnectByInactivityTask((Guild) track.getUserData());
		lifeCycleService.setActivityDefault(((Guild) track.getUserData()).getJDA());
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		logger.debug("Player has paused");
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		logger.debug("Player has resumed");
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track got stuck. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());
		// todo: throws a custom exception
	}

}
