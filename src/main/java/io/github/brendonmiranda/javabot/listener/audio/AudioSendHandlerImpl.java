package io.github.brendonmiranda.javabot.listener.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * It sends audio to discord
 */
public class AudioSendHandlerImpl implements AudioSendHandler {

    private static Logger logger = LoggerFactory.getLogger(AudioSendHandlerImpl.class);

    private AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioSendHandlerImpl(final AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    public AudioPlayer getPlayer()
    {
        return audioPlayer;
    }

}
