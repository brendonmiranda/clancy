package io.github.brendonmiranda.bot.clancy.exception;

public class AudioQueueException extends RuntimeException {

	public AudioQueueException(String message) {
		super(message);
	}

	public AudioQueueException(String message, Throwable cause) {
		super(message, cause);
	}

}
