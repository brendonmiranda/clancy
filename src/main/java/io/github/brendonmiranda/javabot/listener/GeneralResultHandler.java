package io.github.brendonmiranda.javabot.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;

public class GeneralResultHandler implements AudioLoadResultHandler {

	private final AudioPlayer audioPlayer;

	private final Guild guild;

	public GeneralResultHandler(AudioPlayer audioPlayer, Guild guild) {
		this.audioPlayer = audioPlayer;
		this.guild = guild;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		track.setUserData(guild);
		audioPlayer.playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {

	}

	@Override
	public void noMatches() {

	}

	@Override
	public void loadFailed(FriendlyException exception) {

	}

}
