package io.github.brendonmiranda.javabot.listener.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.brendonmiranda.javabot.service.LifeCycleService;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
		logger.debug("Track has started. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		AudioTrackInfo audioTrackInfo = track.getInfo();
		logger.debug("Track has ended. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
				audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

		// Plays the next track from queue
		if (!queue.isEmpty() && !endReason.equals(AudioTrackEndReason.STOPPED)) {
			player.playTrack(queue.get(0));
			queue.remove(0);
			return;
		}
		else {
			lifeCycleService.scheduleDisconnectByInactivityTask((Guild) track.getUserData());
		}

	}

	@Override
	public void onPlayerPause(AudioPlayer player) {

	}

	@Override
	public void onPlayerResume(AudioPlayer player) {

	}

}
