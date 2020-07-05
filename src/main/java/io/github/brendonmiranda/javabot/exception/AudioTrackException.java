package io.github.brendonmiranda.javabot.exception;

public class AudioTrackException extends RuntimeException {

	public AudioTrackException(String message) {
		super(message);
	}

	public AudioTrackException(String message, Throwable cause) {
		super(message, cause);
	}

}
