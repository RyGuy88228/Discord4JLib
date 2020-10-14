package me.ryguy.discordapi.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import me.ryguy.discordapi.DiscordBot;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CommandHandler {
    public CommandHandler() {
        AtomicReference<Command> cmd = new AtomicReference<>();
        DiscordBot.getBot().getGateway().on(MessageCreateEvent.class).subscribe(e -> {
            Message message = e.getMessage();
            if (e.getMessage().getEmbeds().size() != 0) return;
            if (!e.getMessage().getContent().startsWith(DiscordBot.getBot().getPrefix())) return;

            if (!message.getAuthor().isPresent() || message.getAuthor().get().isBot())
                return;

            try {
                List<String> command = parseCommand(message.getContent());
                cmd.set(CommandManager.getCommand(command.get(0)));

                if (cmd.get() == null || !cmd.get().canExecute(e.getMessage(), true))
                    return;
                DiscordBot.getBot().checkCommandCancellation.accept(cmd.get(), message);
                if (!cmd.get().isCancelled()) {
                    cmd.get().execute(message, command.get(0), command.subList(1, command.size()).toArray(new String[0])).doOnError(error -> {
                        if (DiscordBot.getBot() != null) {
                            DiscordBot.getBot().commandException.accept(error, cmd.get());
                        } else {
                            System.out.println("Bot is null in handling command! (this shouldn't be running, purely debug");
                        }
                    }).block();
                } else {
                    System.out.println("Command " + cmd.get().getName() + " was cancelled!");
                }
            } catch (Exception ex) {
                DiscordBot.getBot().commandException.accept(ex, cmd.get());
            }
        });
    }

    public static List<String> parseCommand(String input) {
        return Arrays.stream(input.split("\\W+")).filter(it -> !it.equals("")).collect(Collectors.toList());
    }
}