package me.ryguy.discordapi.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import me.ryguy.discordapi.DiscordBot;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {
    public CommandHandler() {
        DiscordBot.getBot().getGateway().on(MessageCreateEvent.class).subscribe(e -> {
            Message message = e.getMessage();
            if (e.getMessage().getEmbeds().size() != 0) return;
            if (!e.getMessage().getContent().startsWith(DiscordBot.getBot().getPrefix())) return;

            if (!message.getAuthor().isPresent() || message.getAuthor().get().isBot())
                return;

            try {
                List<String> command = parseCommand(message.getContent());
                Command cmd = CommandManager.getCommand(command.get(0));

                if (cmd == null || !cmd.canExecute(message))
                    return;

                cmd.execute(message, command.get(0), command.subList(1, command.size()).toArray(new String[0]));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static List<String> parseCommand(String input) {
        return Arrays.stream(input.split("\\W+")).filter(it -> !it.equals("")).collect(Collectors.toList());
    }
}
