package me.ryguy.discordapi.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface CommandExecutor {

    Mono<Void> execute(Message message, String alias, String[] args);

    default boolean canExecute(MessageCreateEvent e) {
        return true;
    }

}
