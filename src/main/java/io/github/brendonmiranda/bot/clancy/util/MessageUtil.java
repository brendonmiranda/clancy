package io.github.brendonmiranda.bot.clancy.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MessageUtil {

	public static MessageEmbed buildMessage(String description) {

		return buildMessage(null, description);
	}

	public static MessageEmbed buildMessage(String title, String description) {

		EmbedBuilder builder = new EmbedBuilder();

		if (title != null)
			builder.setTitle(title);

		builder.setDescription(description);
		builder.setColor(new Color(189, 52, 235));

		return builder.build();
	}

}
