package io.github.brendonmiranda.javabot.exception;

public class AudioQueueException extends RuntimeException {

	public AudioQueueException(String message) {
		super(message);
	}

	public AudioQueueException(String message, Throwable cause) {
		super(message, cause);
	}

}
