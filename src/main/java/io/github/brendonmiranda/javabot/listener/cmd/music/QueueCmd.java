package io.github.brendonmiranda.javabot.listener.cmd.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.brendonmiranda.javabot.listener.audio.AudioEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author evelynvieira
 */
@Component
public class QueueCmd extends MusicCmd {

	private static final Logger logger = LoggerFactory.getLogger(QueueCmd.class);

	public QueueCmd() {
		this.name = "queue";
		this.help = "shows the current queue";
	}

	@Override
	public void command(CommandEvent event) {
		if (!AudioEventListener.queue.isEmpty())
			if (AudioEventListener.queue.size() == 1)
				event.reply("Your queue has " + AudioEventListener.queue.size() + " song.");
			else
				event.reply("Your queue has " + AudioEventListener.queue.size() + " songs.");
		else
			event.reply("Your queue is empty.");
	}

}
