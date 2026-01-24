package io.github.brendonmiranda.bot.clancy.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Small router to register and dispatch slash commands to MusicCmd implementations.
 */
@Component
public class CommandRouter extends ListenerAdapter {

    private final JDA jda;
    private final List<MusicCmd> commands;

    public CommandRouter(JDA jda, List<MusicCmd> commands) {
        this.jda = jda;
        this.commands = commands;
    }

    @PostConstruct
    public void registerCommands() {
        // Build command data for all discovered commands
        var commandData = commands.stream()
            .map(cmd -> Commands.slash(cmd.name, cmd.help))
            .collect(Collectors.toList());

        // Register as global commands (or adjust for a test guild for immediate testing)
        jda.updateCommands().addCommands(commandData).queue();

        // register this listener
        jda.addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String name = event.getName();
        for (MusicCmd cmd : commands) {
            if (cmd.name != null && cmd.name.equalsIgnoreCase(name)) {
                cmd.execute(event);
                return;
            }
        }
        // If not found, reply gracefully
        event.reply("Unknown command.").setEphemeral(true).queue();
    }
}
