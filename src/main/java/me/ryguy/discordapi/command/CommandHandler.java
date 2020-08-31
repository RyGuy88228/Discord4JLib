package me.ryguy.discordapi.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import me.ryguy.discordapi.DiscordBot;
import org.apache.commons.lang3.ArrayUtils;

public class CommandHandler {
    public CommandHandler() {
        DiscordBot.getBot().getGateway().on(MessageCreateEvent.class).subscribe(e -> {
            Message message = e.getMessage();
            Command cmd = CommandManager.getCommand(message.getContent().split(" ")[0].substring(DiscordBot.getBot().getPrefix().length()));
            if (message.getAuthor().get().isBot())
                return;
            if (cmd == null)
                return;
            if (!cmd.canExecute()) {
                return;
            }
            CommandManager.getCommand(message.getContent().split(" ")[0].substring(DiscordBot.getBot().getPrefix().length()))
                    .execute(message.getAuthor().get(), message.getChannel(), message.getContent().split(" ")[0].substring(DiscordBot.getBot().getPrefix().length()), ArrayUtils.remove(message.getContent().split(" "), 0));
        });
    }
}
