package me.ryguy.discordapi.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import me.ryguy.discordapi.DiscordBot;
import org.apache.commons.lang3.ArrayUtils;

public class CommandHandler {
    public CommandHandler() {
        DiscordBot.getBot().getGateway().on(MessageCreateEvent.class).subscribe(e -> {
            Message message = e.getMessage();
            if (e.getMessage().getEmbeds().size() != 0) return;
            if (!e.getMessage().getContent().startsWith(DiscordBot.getBot().getPrefix())) return;
            Command cmd; //really lazy af way of making sure a command is valid and avoiding errors
            try {
                cmd = CommandManager.getCommand(message.getContent()
                        .split(" ")[0]
                        .substring(DiscordBot.getBot().getPrefix().length())
                        .split("\n")[0]);
            } catch (Exception ex) {
                return;
            }
            if (message.getAuthor().get().isBot())
                return;
            if (cmd == null)
                return;
            if (!cmd.canExecute(message)) {
                return;
            }
            CommandManager.getCommand(message.getContent().split(" ")[0].split("\n")[0].substring(DiscordBot.getBot().getPrefix().length()))
                    .execute(message, message.getContent().split(" ")[0].substring(DiscordBot.getBot().getPrefix().length()), ArrayUtils.remove(message.getContent().split(" "), 0));
        });
    }
}
