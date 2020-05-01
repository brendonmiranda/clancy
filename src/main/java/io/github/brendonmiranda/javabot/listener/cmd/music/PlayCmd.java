package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.brendonmiranda.javabot.listener.audio.AudioEventListener;
import io.github.brendonmiranda.javabot.listener.audio.AudioLoadResultHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brendonmiranda
 */
public class PlayCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(PlayCmd.class);

	private final AudioPlayerManager audioPlayerManager;

	private final AudioEventListener audioListener;

	public PlayCmd(AudioPlayerManager audioPlayerManager, AudioEventListener audioListener) {
		this.audioPlayerManager = audioPlayerManager;
		this.audioListener = audioListener;
		this.name = "play";
	}

	@Override
	public void command(CommandEvent event) {
		logger.info("Play command loading track: {}", event.getArgs());

		AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
		audioPlayer.addListener(audioListener);

		audioPlayerManager.loadItem(event.getArgs(), new AudioLoadResultHandlerImpl(audioPlayer, event));
	}

}
