package io.github.brendonmiranda.bot.clancy.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MessageUtil {

	public static Color DEFAULT_COLOR = new Color(189, 52, 235);

	public static MessageEmbed buildMessage(String description) {

		return buildMessage(null, description, null);
	}

	public static MessageEmbed buildMessage(String title, String description) {
		return buildMessage(title, description, null);
	}

	public static MessageEmbed buildMessage(String title, String description, String footer) {

		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(description);
		builder.setColor(DEFAULT_COLOR);

		if (title != null)
			builder.setTitle(title);

		if (footer != null)
			builder.setFooter(footer);

		return builder.build();
	}

}
