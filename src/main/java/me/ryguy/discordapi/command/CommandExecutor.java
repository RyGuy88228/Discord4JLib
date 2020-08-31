package me.ryguy.discordapi.command;

import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

public interface CommandExecutor {

    Mono<Void> execute(User sender, Mono<MessageChannel> channel, String alias, String[] args);

}
