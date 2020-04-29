package io.github.brendonmiranda.javabot.listener.music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.github.brendonmiranda.javabot.listener.audio.AudioEventListener;
import io.github.brendonmiranda.javabot.listener.audio.AudioLoadResultHandlerImpl;
import io.github.brendonmiranda.javabot.listener.audio.AudioSendHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brendonmiranda
 */
public class PlayCmd extends Command {

	private static Logger logger = LoggerFactory.getLogger(PlayCmd.class);

	private AudioPlayerManager audioPlayerManager;

	private AudioEventListener audioListener;

	public PlayCmd(AudioPlayerManager audioPlayerManager, AudioEventListener audioListener) {
		this.audioPlayerManager = audioPlayerManager;
		this.audioListener = audioListener;

		this.name = "play";
		this.guildOnly = true;
	}

	@Override
	protected void execute(final CommandEvent event) {
		AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
		audioPlayer.addListener(audioListener);
		logger.info("Play command loading track: {}", event.getArgs());
		event.getGuild().getAudioManager()
				.openAudioConnection(event.getGuild().getVoiceChannelsByName("Geral", true).get(0));
		event.getGuild().getAudioManager().setSendingHandler(new AudioSendHandlerImpl(audioPlayer));
		audioPlayerManager.loadItem("https://www.youtube.com/watch?v=EBLF26-Irdc",
				new AudioLoadResultHandlerImpl(audioPlayer));
	}

}
