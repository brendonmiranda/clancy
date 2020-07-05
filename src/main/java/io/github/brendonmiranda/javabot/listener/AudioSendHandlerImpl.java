package io.github.brendonmiranda.javabot.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Sends audio to discord
 */
public class AudioSendHandlerImpl implements AudioSendHandler {

	private static final Logger logger = LoggerFactory.getLogger(AudioSendHandlerImpl.class);

	private final AudioPlayer audioPlayer;

	private AudioFrame lastFrame;

	private long lastRequestTime;

	public AudioSendHandlerImpl(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	@Override
	public boolean canProvide() {
		lastFrame = audioPlayer.provide();
		return lastFrame != null;
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		lastRequestTime = System.currentTimeMillis();
		return ByteBuffer.wrap(lastFrame.getData());
	}

	@Override
	public boolean isOpus() {
		return true;
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public long getLastRequestTime() {
		return lastRequestTime;
	}

}
