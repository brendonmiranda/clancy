package io.github.brendonmiranda.javabot.listener.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static io.github.brendonmiranda.javabot.config.BotConfiguration.DEFAULT_ACTIVITY_VALUE;
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
	private JDA jda;

	public static List<AudioTrack> queue = new ArrayList<>();

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.info("Track has started. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		setActivity(LISTENING, audioTrackInfo.title);
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
			setActivity(LISTENING, audioTrackInfo.title);
			return;
		}

		setActivity(LISTENING, DEFAULT_ACTIVITY_VALUE);
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
		//todo: throws a custom exception
	}

	private void setActivity(ActivityType activityType, String value){
		jda.getPresence().setActivity(Activity.of(activityType, value));
	}

}
