package io.github.brendonmiranda.bot.clancy.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import io.github.brendonmiranda.bot.clancy.service.AudioQueueService;
import io.github.brendonmiranda.bot.clancy.util.MessageUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.brendonmiranda.bot.clancy.command.PlayCmd.MUSIC_ARG;

/**
 * @author brendonmiranda
 */
public class PlayResultHandler implements AudioLoadResultHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayResultHandler.class);

    private final AudioPlayer audioPlayer;

    private final Guild guild;

    private final AudioManager audioManager;

    private final SlashCommandInteractionEvent event;

    private final AudioPlayerManager audioPlayerManager;

    private final Message message;

    private final boolean ytSearch;

    private final AudioQueueService audioQueueService;

    public PlayResultHandler(AudioPlayer audioPlayer, Guild guild, AudioManager audioManager, SlashCommandInteractionEvent event,
                             AudioPlayerManager audioPlayerManager, Message message, boolean ytSearch,
                             AudioQueueService audioQueueService) {
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.audioManager = audioManager;
        this.event = event;
        this.audioPlayerManager = audioPlayerManager;
        this.message = message;
        this.ytSearch = ytSearch;
        this.audioQueueService = audioQueueService;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        AudioTrackInfo audioTrackInfo = track.getInfo();
        logger.info("Track has loaded. Title: {}, author: {}, identifier: {}, source: {}", audioTrackInfo.title,
                audioTrackInfo.author, audioTrackInfo.identifier, track.getSourceManager());

        manageTrack(track);
    }

    /**
     * Plays the track if none is being played otherwise it is enqueued.
     * @param track audio track
     */
    public void manageTrack(AudioTrack track) {

        AudioSendHandlerImpl audioSendHandler = (AudioSendHandlerImpl) audioManager.getSendingHandler();

        // Store guild to be recovered when needed as we do on AudioEventListener
        track.setUserData(guild);

        if (audioSendHandler == null) {
            audioManager.setSendingHandler(new AudioSendHandlerImpl(audioPlayer));
            playTrack(this.audioPlayer, track);
        }
        else {
            AudioPlayer audioPlayer = audioSendHandler.getAudioPlayer();

            if (audioPlayer.getPlayingTrack() != null)
                queueTrack(audioPlayer, track);
            else
                playTrack(audioPlayer, track);
        }
    }

    private void queueTrack(AudioPlayer audioPlayer, AudioTrack track) {

        audioQueueService.enqueue(guild.getName(), track);

        event.getChannel().sendMessageEmbeds(MessageUtil.buildMessage("Enqueued", track.getInfo().title)).queue();

        if (audioPlayer.isPaused()) {
            event.getChannel()
                .sendMessageEmbeds(MessageUtil.buildMessage("Alert",
                        "The track `" + audioPlayer.getPlayingTrack().getInfo().title
                                + "` is paused. \n\nType `/resume` to unpause."))
                .queue();
        }
    }

    private void playTrack(AudioPlayer audioPlayer, AudioTrack track) {

        audioPlayer.playTrack(track);

        event.getChannel()
            .sendMessageEmbeds(MessageUtil.buildMessage("Playing", audioPlayer.getPlayingTrack().getInfo().title))
            .queue();
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        OptionMapping option = event.getOption(MUSIC_ARG);
        String args = option != null ? option.getAsString() : "";

        // simplified behavior: list top results
        if (playlist.isSearchResult()) {
            StringBuilder sb = new StringBuilder("Search `").append(args).append("`:\n");
            for (int i = 0; i < Math.min(5, playlist.getTracks().size()); i++) {
                sb.append(i + 1).append(") ").append(playlist.getTracks().get(i).getInfo().title).append("\n");
            }
            sb.append("\nTo play, run `/play <query>`.");
            event.getChannel().sendMessage(sb.toString()).queue();
        }
        else {
            event.getChannel()
                .sendMessageEmbeds(MessageUtil.buildMessage("Sorry, I'm unable to load a playlist."))
                .queue();
        }
    }

    @Override
    public void noMatches() {
        OptionMapping option = event.getOption(MUSIC_ARG);
        String args = option != null ? option.getAsString() : "";

        // conditional to avoid loop
        if (!ytSearch)
            audioPlayerManager.loadItem("ytsearch:" + args, new PlayResultHandler(audioPlayer, guild, audioManager,
                    event, audioPlayerManager, message, true, audioQueueService));
        else
            event.getChannel()
                .sendMessageEmbeds(
                        MessageUtil.buildMessage("Sorry, I couldn't find your track. Please, rephrase and try again."))
                .queue();
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        logger.error("Loading audio has failed. Severity: {}.", exception.severity);
        throw exception;
    }
}
