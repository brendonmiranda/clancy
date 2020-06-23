package io.github.brendonmiranda.javabot.dto;

import io.github.brendonmiranda.javabot.service.AudioQueueService;
import org.springframework.amqp.core.Message;

import java.io.Serializable;

/**
 * This object is used as body of a {@link Message} in order to transfer audio track info
 * through the audio queue.
 *
 * @see Message#getBody()
 * @see AudioQueueService#enqueue
 * @see AudioQueueService#receive
 * @author brendonmiranda
 */
public class AudioTrackInfoDTO implements Serializable {

	private static final long serialVersionUID = -2774251881026920107L;

	private String title;

	private String author;

	private long length;

	private String identifier;

	private boolean isStream;

	private String uri;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean isStream() {
		return isStream;
	}

	public void setStream(boolean stream) {
		isStream = stream;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
